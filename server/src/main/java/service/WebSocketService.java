package service;

import chess.ChessMove;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import org.eclipse.jetty.websocket.api.Session;
import records.ErrorResponse;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.ServerMessage;

public class WebSocketService {
    private final AuthDAO authDataAccess;
    private final GameDAO gameDataAccess;
    private int gameId;
    private String authToken;
    private String user;

    public WebSocketService(AuthDAO authDataAccess, GameDAO gameDataAccess){
        this.authDataAccess = authDataAccess;
        this. gameDataAccess = gameDataAccess;
    }

    private void getInfo(UserGameCommand com) throws DataAccessException {
        gameId = com.getGameID();
        authToken= com.getAuthToken();
        user = authDataAccess.getAuthToken(authToken).username();
    }

    public ServerMessage connectPlayer(Session session, UserGameCommand com){
        try {
            getInfo(com);

        } catch (DataAccessException e) {
            return new ErrorMessage(handleError(e).message());
        }
        return null;
    }

    public ServerMessage leave(Session session, UserGameCommand com){
        try {
            getInfo(com);
        } catch (DataAccessException e) {
            return new ErrorMessage(handleError(e).message());
        }
        return null;
    }

    public ServerMessage makeMove(Session session, MakeMoveCommand com){
        try {
            getInfo(com);
        } catch (DataAccessException e) {
            return new ErrorMessage(handleError(e).message());
        }
        return null;
    }

    public ServerMessage resign(Session session, UserGameCommand com){
        try {
            getInfo(com);
        } catch (DataAccessException e) {
            return new ErrorMessage(handleError(e).message());
        }
        return null;
    }

    public String getUser(){
        return user;
    }

    private ErrorResponse handleError(DataAccessException e){
        return new ErrorHandler(e).handleError();
    }

}
