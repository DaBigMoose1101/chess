package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

public class WebSocketService {
    private final AuthDAO authDataAccess;
    private final GameDAO gameDataAccess;
    public WebSocketService(AuthDAO authDataAccess, GameDAO gameDataAccess){
        this.authDataAccess = authDataAccess;
        this. gameDataAccess = gameDataAccess;
    }

    public ServerMessage connectPlayer(Session session, UserGameCommand com){
        int gameId = com.getGameID();
        String authToken = com.getAuthToken();


        return null;
    }

}
