package handler;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import records.LoginRequest;
import records.LogoutRequest;
import records.RegisterRequest;
import service.UserService;

import java.util.Vector;

public class UserHandler {
    final private UserDAO userDataAccess;
    final private AuthDAO authDataAccess;
    public UserHandler(UserDAO userDataAccess, AuthDAO authDataAccess){
        this.userDataAccess = userDataAccess;
        this.authDataAccess = authDataAccess;
    }
    public Object login(String body){
        LoginRequest req = new Gson().fromJson(body, LoginRequest.class);
        UserService service = new UserService(userDataAccess, authDataAccess);
        return service.login(req);
    }
    public Object register(String body){
        RegisterRequest req = new Gson().fromJson(body, RegisterRequest.class);
        UserService service = new UserService(userDataAccess, authDataAccess);
        return service.registerUser(req);
    }
    public Object logout(String header){
        Object res;
        LogoutRequest req = new Gson().fromJson(header, LogoutRequest.class);
        UserService service = new UserService(userDataAccess, authDataAccess);
        res = service.logout(req);
        return res;
    }
}
