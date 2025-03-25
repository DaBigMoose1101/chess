package client;

import chess.ChessGame;
import com.google.gson.Gson;
import records.*;


public class ServerFacade {
    private final String serverURL;
    private final String port;

    private ClientCommunicator getCommunicator(String path, String method){
        return new ClientCommunicator(serverURL + port + path, method);
    }

    public ServerFacade(int port, String serverURL){
        this.serverURL = serverURL;
        this.port = Integer.toString(port);
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
        Object response = new Gson().fromJson(responseString, Object.class);
        if(response instanceof RegisterResponse){
            return ((RegisterResponse) response);
        }
        else{
            return ((ErrorResponse) response);
        }
    }

    public Object login(String username, String password){
        String path = "/session";
        Gson serializer = new Gson();
        LoginRequest req = new LoginRequest(username, password);
        String body = serializer.toJson(req);
        ClientCommunicator comm = getCommunicator(path, "PUT");
        String responseString;
        try{
            responseString = comm.executeRequest("", body);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Object response = new Gson().fromJson(responseString, Object.class);
        if(response instanceof LoginResponse){
            return ((LoginResponse) response);
        }
        else{
            return ((ErrorResponse) response);
        }
    }

    public Object logout(String authToken){
        String path = "/session";
        Gson serializer = new Gson();
        LogoutRequest req = new LogoutRequest(authToken);
        String header = serializer.toJson(req);
        ClientCommunicator comm = getCommunicator(path, "DELETE");
        String responseString;
        try{
            responseString = comm.executeRequest(header, "");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Object response = new Gson().fromJson(responseString, Object.class);
        if(response instanceof LogoutResponse){
            return ((LogoutResponse) response);
        }
        else{
            return ((ErrorResponse) response);
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
        Object response = new Gson().fromJson(responseString, Object.class);
        if(response instanceof CreateGameResponse){
            return ((CreateGameResponse) response);
        }
        else{
            return ((ErrorResponse) response);
        }
    }

    public Object getGameList(String authToken){
        String path = "/game";
        Gson serializer = new Gson();
        GamesListRequest req = new GamesListRequest(authToken);
        String header = serializer.toJson(req);
        ClientCommunicator comm = getCommunicator(path, "GET");
        String responseString;
        try{
            responseString = comm.executeRequest(authToken, "");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Object response = new Gson().fromJson(responseString, Object.class);
        if(response instanceof GamesListResponse){
            return ((GamesListResponse) response);
        }
        else{
            return ((ErrorResponse) response);
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
        Object response = new Gson().fromJson(responseString, Object.class);
        if(response instanceof JoinGameResponse){
            return ((JoinGameResponse) response);
        }
        else{
            return ((ErrorResponse) response);
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
        Object response = new Gson().fromJson(responseString, Object.class);
        if(response instanceof ClearResponse){
            return ((ClearResponse) response);
        }
        else{
            return ((ErrorResponse) response);
        }

    }
}
