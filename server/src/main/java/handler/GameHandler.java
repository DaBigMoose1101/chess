package handler;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import service.GameService;
import spark.Response;
import records.*;

public class GameHandler {
    final private GameDAO gameDataAccess;
    final private AuthDAO authDataAccess;
    final private Response res;

    public GameHandler(AuthDAO authDataAccess, GameDAO gameDataAccess, Response res){
        this.authDataAccess = authDataAccess;
        this.gameDataAccess = gameDataAccess;
        this.res = res;
    }

    public Object createGame(String authToken, String body){
        Gson serializer = new Gson();
        CreateGameRequest req = serializer.fromJson(body, CreateGameRequest.class);
        GameService service = new GameService(authDataAccess, gameDataAccess);
        Object response = service.createGame(req, authToken);
        if(response instanceof ErrorResponse){
            ErrorResponse temp = (ErrorResponse) response;
            res.status(temp.code());
            return serializer.toJson(temp);
        }
        else{
            CreateGameResponse temp = (CreateGameResponse) response;
            res.status(200);
            return serializer.toJson(temp);
        }
    }

    public Object joinGame(String authToken, String body){
        var serializer = new Gson();
        JoinGameRequest req = serializer.fromJson(body, JoinGameRequest.class);
        GameService service = new GameService(authDataAccess, gameDataAccess);
        Object response = service.joinGame(req, authToken);
        if(response instanceof ErrorResponse){
            ErrorResponse temp = (ErrorResponse) response;
            res.status(temp.code());
            return serializer.toJson(temp);
        }
        else{
            JoinGameResponse temp = (JoinGameResponse) response;
            res.status(200);
            return serializer.toJson(temp);
        }
    }

    public Object getGameList(String authToken){
        var serializer = new Gson();
        GamesListRequest req = new GamesListRequest(authToken);
        GameService service = new GameService(authDataAccess, gameDataAccess);
        Object response = service.getGameList(req);
        if(response instanceof ErrorResponse){
            ErrorResponse temp = (ErrorResponse) response;
            res.status(temp.code());
            return serializer.toJson(temp);
        }
        else{
            GamesListResponse temp = (GamesListResponse) response;
            res.status(200);
            return serializer.toJson(temp);
        }
    }

}
