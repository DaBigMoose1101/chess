package service;
import java.util.UUID;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import model.UserData;
import model.AuthData;
import records.*;

public class UserService {
    final private UserDAO userDataAccess;
    final private AuthDAO authDataAccess;

    public UserService(UserDAO userDataAccess, AuthDAO authDataAccess){
        this.userDataAccess = userDataAccess;
        this.authDataAccess = authDataAccess;
    }

    public RegisterResponse registerUser(RegisterRequest req){
        String username = req.username();
        String password = req.password();
        String email = req.email();
        UserData user = new UserData(username, password, email);
        if(userDataAccess.getUser(user.username()) == null){
            userDataAccess.addUser(user);
            String authToken = generateToken();
            AuthData authData = new AuthData(authToken, username);
            authDataAccess.addAuthToken(authData);
            return new RegisterResponse(username, authToken);
        }
        return null;
    }

    public LoginResponse login(LoginRequest req){
        String username = req.username();
        String password = req.password();
        UserData user = userDataAccess.getUser(username);
        if(user != null && user.password().equals(password)){
            String authToken = generateToken();
            AuthData authData = new AuthData(authToken, username);
            authDataAccess.addAuthToken(authData);
            return new LoginResponse(username, authToken);
        }

        return null;
    }

    public LogoutResponse logout(LogoutRequest req){
        String authToken = req.authToken();
        AuthData data = authDataAccess.getAuthToken(authToken);
        if(data != null){
            authDataAccess.deleteAuthToken(data);
            return new LogoutResponse(null);
        }
        return null;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
