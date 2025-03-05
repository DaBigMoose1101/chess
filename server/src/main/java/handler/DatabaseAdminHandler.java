package handler;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import records.CreateGameResponse;
import records.ErrorResponse;
import service.ClearDatabaseService;
import spark.Response;

public class DatabaseAdminHandler {
    final private AuthDAO authDataAccess;
    final private UserDAO userDataAccess;
    final private GameDAO gameDataAccess;
    final private Response res;

    public DatabaseAdminHandler(AuthDAO authDataAccess, UserDAO userDataAccess, GameDAO gameDataAccess, Response res){
        this.authDataAccess = authDataAccess;
        this.userDataAccess = userDataAccess;
        this.gameDataAccess = gameDataAccess;
        this.res = res;
    }

    public Response deleteDB(){
        Gson serializer = new Gson();
        ClearDatabaseService service = new ClearDatabaseService(authDataAccess, userDataAccess, gameDataAccess);
        return getResponse(service.deleteDB(), serializer);
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
