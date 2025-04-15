package service;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import records.ErrorResponse;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

public class WebSocketService {
    private final AuthDAO authDataAccess;
    private final GameDAO gameDataAccess;
    private int gameId;
    private String authToken;
    private String user;
    private GameData game;

    public WebSocketService(AuthDAO authDataAccess, GameDAO gameDataAccess){
        this.authDataAccess = authDataAccess;
        this. gameDataAccess = gameDataAccess;
    }

    private void getInfo(UserGameCommand com) throws DataAccessException {
        gameId = com.getGameID();
        authToken= com.getAuthToken();
        AuthData auth = authDataAccess.getAuthToken(authToken);
        user = auth.username();
       game = gameDataAccess.getGame(gameId);
    }



    public ServerMessage connectPlayer(Session session, UserGameCommand com){
        try {
            getInfo(com);
            return new LoadGameMessage(game, gameId);
        } catch (DataAccessException e) {
            return new ErrorMessage(handleError(e).message());
        }
    }

    public ServerMessage leave(Session session, UserGameCommand com){
        try {
            getInfo(com);
            if(game.blackUsername() != null && game.blackUsername().equals(user)){
                gameDataAccess.updateGameColor(game,"", ChessGame.TeamColor.BLACK);
            }
            else if(game.whiteUsername() != null && game.whiteUsername().equals(user)){
                gameDataAccess.updateGameColor(game,"", ChessGame.TeamColor.WHITE);
            }
            return new NotificationMessage(user + " left the game.");
        } catch (DataAccessException e) {
            return new ErrorMessage(handleError(e).message());
        }
    }

    public ServerMessage makeMove(Session session, MakeMoveCommand com){
        try {
            getInfo(com);
            ChessGame chess = game.game();
            validatePlayer(chess);
            chess.makeMove(com.getMove());
            gameDataAccess.updateGame(game);
            return new LoadGameMessage(game, gameId);
        } catch (DataAccessException e) {
            return new ErrorMessage(handleError(e).message());
        } catch (InvalidMoveException e) {
            return new ErrorMessage("Invalid Move");
        }

    }

    public ServerMessage resign(Session session, UserGameCommand com){
        try {
            getInfo(com);
            ChessGame chess = game.game();
            if(!game.blackUsername().equals(user) && !game.whiteUsername().equals(user) || chess.isGameOver()){
                throw new InvalidMoveException();
            }
            chess.resign();
            gameDataAccess.updateGame(game);
            return new NotificationMessage(user + " resigned.");
        } catch (DataAccessException e) {
            return new ErrorMessage(handleError(e).message());
        } catch (InvalidMoveException e) {
            return new ErrorMessage("Invalid Move");
        }
    }

    public String getUser(){
        return user;
    }

    public GameData getGame(){
        return game;
    }

    private ErrorResponse handleError(DataAccessException e){
        return new ErrorHandler(e).handleError();
    }

    private void validatePlayer(ChessGame chess) throws InvalidMoveException {
        ChessGame.TeamColor userColor;
        if(game.whiteUsername().equals(user)){
            userColor = ChessGame.TeamColor.WHITE;
        }
        else if(game.blackUsername().equals(user)){
            userColor = ChessGame.TeamColor.BLACK;
        }
        else{
            throw new InvalidMoveException();
        }
        ChessGame.TeamColor color = chess.getTeamTurn();
        if(userColor != color){
            throw new InvalidMoveException();
        }
    }

}
