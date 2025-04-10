package handler;

import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.GameData;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import server.ConnectionManager;
import service.WebSocketService;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;

@WebSocket
public class WebSocketHandler {
    static AuthDAO authDataAccess;
    static GameDAO gameDataAccess;
    static ConnectionManager connections = new ConnectionManager();
    private Gson serializer = new Gson();

    public static void initialize(AuthDAO auth, GameDAO game){
        authDataAccess = auth;
        gameDataAccess = game;
    }
    @OnWebSocketMessage
    public void onMessage(Session session, String message){
        WebSocketService service = new WebSocketService(authDataAccess, gameDataAccess);
        UserGameCommand com = serializer.fromJson(message, UserGameCommand.class);
        int gameId = com.getGameID();
        ServerMessage serverMessage;
        String user;
        GameData game;
        try {
            switch (com.getCommandType()) {
                case CONNECT:
                    serverMessage = service.connectPlayer(session, com);
                    user = service.getUser();
                    if(serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME){
                        game = ((LoadGameMessage) serverMessage).getGame();
                        connections.addConnection(gameId, session);
                        String position = "observer";
                        if(game.whiteUsername().equals(user)){
                            position = "white";
                        }
                        else if(game.blackUsername().equals(user)){
                            position = "black";
                        }
                        NotificationMessage note = new NotificationMessage(user + " joined the game as " + position);
                        notifyConnections(note, session, gameId);
                        sendMessage(session, serverMessage);
                    }
                    else{
                        sendMessage(session, serverMessage);
                    }
                    break;
                case LEAVE:
                    serverMessage = service.leave(session, com);
                    if(serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION){
                        connections.removeSession(gameId, session);
                        notifyConnections(serverMessage, session, gameId);
                    }
                    else{
                        sendMessage(session, serverMessage);
                    }
                    break;
                case MAKE_MOVE:
                    serverMessage = service.makeMove(session, (MakeMoveCommand) com);
                    user = service.getUser();
                    ChessMove move = ((MakeMoveCommand)com).getMove();
                    if(serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME){
                        NotificationMessage note = new NotificationMessage(user + parseMove(move));
                        notifyConnections(serverMessage, session, gameId);
                        notifyConnections(note, session, gameId);
                        sendMessage(session, serverMessage);
                        sendMessage(session, new NotificationMessage("You" + parseMove(move)));
                    }
                    else{
                        sendMessage(session, serverMessage);
                    }
                    break;
                case RESIGN:
                    serverMessage = service.resign(session, com);
                    if(serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION){
                        notifyConnections(serverMessage, session, gameId);
                        sendMessage(session, serverMessage);
                    }
                    else{
                        sendMessage(session, serverMessage);
                    }
                    break;
                default:
                    serverMessage = new ErrorMessage("Error: Invalid request");
                    sendMessage(session, serverMessage);
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @OnWebSocketConnect
     public void onConnect(Session session) {
        // Handle new WebSocket connections
        System.out.println("Client connected: " + session.getRemoteAddress());
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        System.out.println("Connection closed: " + reason);
    }

    private void sendMessage(Session session, ServerMessage message) throws IOException {
        String response = serializer.toJson(message);
        session.getRemote().sendString(response);
    }

  private void notifyConnections(ServerMessage message,
                                  Session session, int gameId) throws IOException {
        ArrayList<Session> sessions = connections.getSessions(gameId);
        for (Session ses : sessions){
            if(ses != session){
                sendMessage(ses, message);
            }
        }
    }

    private String parseMove(ChessMove move){
        String res = " moved ";
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        res += start.getRow() + convert(start.getColumn()) + " to " + end.getRow() + convert(end.getColumn());
        return res;
    }

    private String convert(int i) {
        return switch (i) {
            case 1 -> "a";
            case 2 -> "b";
            case 3 -> "c";
            case 4 -> "d";
            case 5 -> "e";
            case 6 -> "f";
            case 7 -> "g";
            case 8 -> "h";
            default -> "err";
        };
    }

}
