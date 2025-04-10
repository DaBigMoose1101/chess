package client;

import chess.ChessMove;
import chess.ChessPosition;
import records.*;
import chess.ChessGame;
import com.google.gson.Gson;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;


public class ServerFacade {
    private final String serverURL;
    private final WebSocketFacade socket;

    private ClientCommunicator getCommunicator(String path, String method){
        return new ClientCommunicator(serverURL + path, method);
    }

    public ServerFacade(String serverURL, WebSocketObserver observer){
        this.serverURL = serverURL;
        try {
            this.socket = new WebSocketFacade(serverURL, observer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void send(UserGameCommand com){
        try {
            socket.send(new Gson().toJson(com));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    public void connect(String authToken, Integer gameID) {
        UserGameCommand com = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
        send(com);
    }

    public void makeMove(String authToken, Integer gameID, ChessMove move){
        MakeMoveCommand com = new MakeMoveCommand(authToken, gameID, move);
        send(com);
    }

    public void leave(String authToken, Integer gameID){
        UserGameCommand com = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
        send(com);
    }

    public void resign(String authToken, Integer gameID){
        UserGameCommand com = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
        send(com);
    }

    public Object register(String username, String password, String email){
        String path = "/user";
        Gson serializer = new Gson();
        RegisterRequest req = new RegisterRequest(username, password, email);
        String body = serializer.toJson(req);
        ClientCommunicator comm = getCommunicator(path, "POST");
        String responseString;
        try{
            responseString = comm.executeRequest("", body);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (responseString.contains("message")) {
            return new Gson().fromJson(responseString, ErrorResponse.class);
        } else {
            return new Gson().fromJson(responseString, RegisterResponse.class);
        }
    }

    public Object login(String username, String password){
        String path = "/session";
        Gson serializer = new Gson();
        LoginRequest req = new LoginRequest(username, password);
        String body = serializer.toJson(req);
        ClientCommunicator comm = getCommunicator(path, "POST");
        String responseString;
        try{
            responseString = comm.executeRequest("", body);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (responseString.contains("message")) {
            return new Gson().fromJson(responseString, ErrorResponse.class);
        } else {
            return new Gson().fromJson(responseString, LoginResponse.class);
        }
    }

    public Object logout(String authToken){
        String path = "/session";
        ClientCommunicator comm = getCommunicator(path, "DELETE");
        String responseString;
        try{
            responseString = comm.executeRequest(authToken, "");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (responseString.contains("message")) {
            return new Gson().fromJson(responseString, ErrorResponse.class);
        } else {
            return new Gson().fromJson(responseString, LogoutResponse.class);
        }
    }

    public Object createGame(String authToken, String gameName){
        String path = "/game";
        Gson serializer = new Gson();
        CreateGameRequest req = new CreateGameRequest(gameName);
        String body = serializer.toJson(req);
        ClientCommunicator comm = getCommunicator(path, "POST");
        String responseString;
        try{
            responseString = comm.executeRequest(authToken, body);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (responseString.contains("message")) {
            return new Gson().fromJson(responseString, ErrorResponse.class);
        } else {
            return new Gson().fromJson(responseString, CreateGameResponse.class);
        }
    }

    public Object getGameList(String authToken){
        String path = "/game";
        ClientCommunicator comm = getCommunicator(path, "GET");
        String responseString;
        try{
            responseString = comm.executeRequest(authToken, "");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (responseString.contains("message")) {
            return new Gson().fromJson(responseString, ErrorResponse.class);
        } else{
            return new Gson().fromJson(responseString, GamesListResponse.class);
        }

    }

    public Object joinGame(String authToken, ChessGame.TeamColor color, int gameId){
        String path = "/game";
        Gson serializer = new Gson();
        JoinGameRequest req = new JoinGameRequest(color, gameId);
        String body = serializer.toJson(req);
        ClientCommunicator comm = getCommunicator(path, "PUT");
        String responseString;
        try{
            responseString = comm.executeRequest(authToken, body);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (responseString.contains("message")) {
            return new Gson().fromJson(responseString, ErrorResponse.class);
        } else {
            connect(authToken, gameId);
            return serializer.fromJson(responseString, JoinGameResponse.class);
        }
    }

    public Object deleteDB(){
        String path = "/db";
        Gson serializer = new Gson();
        ClearRequest req = new ClearRequest();
        String body = serializer.toJson(req);
        ClientCommunicator comm = getCommunicator(path, "DELETE");
        String responseString;
        try{
            responseString = comm.executeRequest("",body);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (responseString.contains("message")) {
            return new Gson().fromJson(responseString, ErrorResponse.class);
        } else {
            return new Gson().fromJson(responseString, ClearResponse.class);
        }
    }
}
