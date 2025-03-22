package client;

import chess.ChessGame;
import client.ClientCommunicator;
import com.google.gson.Gson;
import records.*;



public class ServerFacade {
    String serverURL;
    public ServerFacade(){
        this.serverURL = "example url";
    }

    public Object register(String username, String password, String email){
        String path = "/user";
        Gson serializer = new Gson();
        RegisterRequest req = new RegisterRequest(username, password, email);
        String body = serializer.toJson(req);
        ClientCommunicator comm = new ClientCommunicator();
        try{
            comm.postRequest(serverURL + path, null, body);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        return null;
    }

    public Object login(String username, String password){
        String path = "/user";
        Gson serializer = new Gson();
        LoginRequest req = new LoginRequest(username, password);
        String body = serializer.toJson(req);

        return null;
    }

    public Object logout(String authToken){
        String path = "/user";
        Gson serializer = new Gson();
        LogoutRequest req = new LogoutRequest(authToken);
        String body = serializer.toJson(req);

        return null;
    }

    public Object createGame(String authToken, String gameName){
        String path = "/game";
        Gson serializer = new Gson();
        CreateGameRequest req = new CreateGameRequest(gameName);
        String body = serializer.toJson(req);
        ClientCommunicator comm = new ClientCommunicator();
        try{
            comm.postRequest(serverURL + path, authToken, body);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public Object getGameList(String authToken){
        String path = "/game";
        Gson serializer = new Gson();
        GamesListRequest req = new GamesListRequest(authToken);
        String header = serializer.toJson(req);

        return null;
    }

    public Object joinGame(String authToken, ChessGame.TeamColor color, int gameId){
        String path = "/game";
        Gson serializer = new Gson();
        JoinGameRequest req = new JoinGameRequest(color, gameId);
        String body = serializer.toJson(req);

        return null;
    }

    public Object deleteDB(){
        String path = "/db";

        return null;
    }
}
