package handler;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import service.WebSocketService;
import websocket.commands.UserGameCommand;

public class WebSocketHandler {
    static AuthDAO authDataAccess;
    static GameDAO gameDataAccess;
    public final WebSocketService service = new WebSocketService(authDataAccess, gameDataAccess);

    static void initialize(AuthDAO auth, GameDAO game){
        authDataAccess = auth;
        gameDataAccess = game;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message){

        Gson serializer = new Gson();
        UserGameCommand com = serializer.fromJson(message, UserGameCommand.class);
        switch(com.getCommandType()){
            case CONNECT -> service.connectPlayer(session, com);
        }
    }

}
