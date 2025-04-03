package handler;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import service.WebSocketService;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;

public class WebSocketHandler {
    static AuthDAO authDataAccess;
    static GameDAO gameDataAccess;
    private final Gson serializer = new Gson();

    public static void initialize(AuthDAO auth, GameDAO game){
        authDataAccess = auth;
        gameDataAccess = game;

    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message){
        WebSocketService service = new WebSocketService(authDataAccess, gameDataAccess);
        UserGameCommand com = serializer.fromJson(message, UserGameCommand.class);
        ServerMessage serverMessage;
        try {
            switch (com.getCommandType()) {
                case CONNECT:
                    serverMessage = service.connectPlayer(session, com);
                    break;
                case LEAVE:
                    serverMessage = service.leave(session, com);
                    break;
                case MAKE_MOVE:
                    serverMessage = service.makeMove(session, (MakeMoveCommand) com);
                    break;
                case RESIGN:
                    serverMessage = service.resign(session, com);
                    break;
                default:
                    serverMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR,
                            "Error: Invalid request");
            }
            sendMessage(session, serverMessage);
        }
        catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void sendMessage(Session session, ServerMessage message) throws IOException {
        String response = serializer.toJson(message);
        session.getRemote().sendString(response);
    }

}
