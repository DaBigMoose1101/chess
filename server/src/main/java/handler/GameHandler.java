package handler;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import records.*;
import service.GameService;
import spark.Response;

public class GameHandler {
    final private GameDAO gameDataAccess;
    final private AuthDAO authDataAccess;
    final private Response res;

    public GameHandler(AuthDAO authDataAccess, GameDAO gameDataAccess, Response res){
        this.authDataAccess = authDataAccess;
        this.gameDataAccess = gameDataAccess;
        this.res = res;
    }

    public Response createGame(String authToken, String body){
        Gson serializer = new Gson();
        CreateGameRequest req = serializer.fromJson(body, CreateGameRequest.class);
        GameService service = new GameService(authDataAccess, gameDataAccess);
        return getResponse(service.createGame(req, authToken), serializer);
    }

    public Response joinGame(String authToken, String body){
        var serializer = new Gson();
        JoinGameRequest req = serializer.fromJson(body, JoinGameRequest.class);
        GameService service = new GameService(authDataAccess, gameDataAccess);
        return getResponse(service.joinGame(req, authToken), serializer);
    }

    public Response getGameList(String authToken){
        var serializer = new Gson();
        GamesListRequest req = new GamesListRequest(authToken);
        GameService service = new GameService(authDataAccess, gameDataAccess);
        return getResponse(service.getGameList(req), serializer);
    }

    private Response getResponse(Object response, Gson serializer){
        int code;
        String responseBody;
        if(response.getClass() == ErrorResponse.class){
            ErrorResponse error = (ErrorResponse) response;
            code = error.code();
            responseBody = serializer.toJson(error);
        }
        else{
            CreateGameResponse createGame = (CreateGameResponse) response;
            code = 200;
            responseBody = serializer.toJson(createGame);
        }
        res.status(code);
        res.body(responseBody);
        return res;
    }

}
