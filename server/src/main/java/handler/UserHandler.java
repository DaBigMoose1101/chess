package handler;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import records.*;
import service.UserService;
import spark.Response;

public class UserHandler {
    final private UserDAO userDataAccess;
    final private AuthDAO authDataAccess;
    final private Response res;

    public UserHandler(UserDAO userDataAccess, AuthDAO authDataAccess, Response res){
        this.userDataAccess = userDataAccess;
        this.authDataAccess = authDataAccess;
        this.res = res;
    }

    public Response login(String body){
        Gson serializer = new Gson();
        LoginRequest req = serializer.fromJson(body, LoginRequest.class);
        UserService service = new UserService(userDataAccess, authDataAccess);
        return getResponse(service.login(req), serializer);
    }

    public Response register(String body){
        Gson serializer = new Gson();
        RegisterRequest req = serializer.fromJson(body, RegisterRequest.class);
        UserService service = new UserService(userDataAccess, authDataAccess);
        return getResponse(service.registerUser(req), serializer);
    }

    public Response logout(String header){
        Gson serializer = new Gson();
        LogoutRequest req = serializer.fromJson(header, LogoutRequest.class);
        UserService service = new UserService(userDataAccess, authDataAccess);
        return getResponse(service.logout(req), serializer);
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
