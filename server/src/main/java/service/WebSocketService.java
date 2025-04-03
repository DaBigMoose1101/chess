package service;

import chess.ChessMove;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

public class WebSocketService {
    private final AuthDAO authDataAccess;
    private final GameDAO gameDataAccess;
    int gameId;
    String authToken;
    public WebSocketService(AuthDAO authDataAccess, GameDAO gameDataAccess){
        this.authDataAccess = authDataAccess;
        this. gameDataAccess = gameDataAccess;
    }

    private void getInfo(UserGameCommand com){
        gameId = com.getGameID();
        authToken= com.getAuthToken();
    }

    public ServerMessage connectPlayer(Session session, UserGameCommand com){
        getInfo(com);
        return null;
    }

    public ServerMessage leave(Session session, UserGameCommand com){
        getInfo(com);
        return null;
    }

    public ServerMessage makeMove(Session session, MakeMoveCommand com){
        getInfo(com);
        ChessMove move = com.getMove();
        return null;
    }

    public ServerMessage resign(Session session, UserGameCommand com){
        getInfo(com);
        return null;
    }

}
