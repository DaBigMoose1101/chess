package service;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
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
        user = authDataAccess.getAuthToken(authToken).username();
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
            if(game.blackUsername().equals(user)){
                gameDataAccess.updateGameColor(game,"", ChessGame.TeamColor.BLACK);
            }
            else if(game.whiteUsername().equals(user)){
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
            game.game().resign();
            return new NotificationMessage(user + " resigned.");
        } catch (DataAccessException e) {
            return new ErrorMessage(handleError(e).message());
        }
    }

    public String getUser(){
        return user;
    }

    private ErrorResponse handleError(DataAccessException e){
        return new ErrorHandler(e).handleError();
    }

}
