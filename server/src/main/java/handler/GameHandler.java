package handler;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import records.*;
import service.GameService;

public class GameHandler {
    final private GameDAO gameDataAccess;
    final private AuthDAO authDataAccess;

    public GameHandler(AuthDAO authDataAccess, GameDAO gameDataAccess){
        this.authDataAccess = authDataAccess;
        this.gameDataAccess = gameDataAccess;
    }

    public String createGame(String authToken, String body){
        var serializer = new Gson();
        CreateGameRequest req = serializer.fromJson(body, CreateGameRequest.class);
        GameService service = new GameService(authDataAccess, gameDataAccess);
        return serializer.toJson(service.createGame(req, authToken));
    }

    public String joinGame(String authToken, String body){
        var serializer = new Gson();
        JoinGameRequest req = serializer.fromJson(body, JoinGameRequest.class);
        GameService service = new GameService(authDataAccess, gameDataAccess);
        return serializer.toJson(service.joinGame(req, authToken));
    }

    public String getGameList(String authToken){
        var serializer = new Gson();
        GamesListRequest req = new GamesListRequest(authToken);
        GameService service = new GameService(authDataAccess, gameDataAccess);
        return serializer.toJson(service.getGameList(req));
    }

}
