package handler;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataaccess.UserDAO;
import records.LoginRequest;
import records.LogoutRequest;
import records.RegisterRequest;
import service.UserService;

import java.util.Vector;

public class UserHandler {
    private UserDAO userDataAccess;
    public UserHandler(UserDAO userDataAccess){
        this.userDataAccess = userDataAccess;
    }
    public Object login(){

        return null;
    }
    public Object register(String body){
        RegisterRequest req = new Gson().fromJson(body, RegisterRequest.class);
        UserService service = new UserService(req, userDataAccess);
        return null;
    }
    public Object logout(){

        return null;
    }
}
