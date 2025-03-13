package service;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.UUID;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
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

    public Object registerUser(RegisterRequest req){
        String username = req.username();
        String password = req.password();
        String email = req.email();
        try {
            validatePassword(password);
            isAvailable(username, password, email);
            password = hashPassword(req.password());
            UserData user = new UserData(username, password, email);
            userDataAccess.addUser(user);
            String authToken = generateToken();
            validateToken(authToken);
            AuthData authData = new AuthData(authToken, username);
            authDataAccess.addAuthToken(authData);
            return new RegisterResponse(username, authToken);
        }
        catch (DataAccessException e){
            return handleError(e);
        }
    }

    public Object login(LoginRequest req){
        String username = req.username();
        String password = req.password();

        try {
            UserData user = userDataAccess.getUser(username);
            validateUsername(username);
            validateUser(user, password);
            String authToken = generateToken();
            AuthData authData = new AuthData(authToken, username);
            authDataAccess.addAuthToken(authData);
            return new LoginResponse(username, authToken);
        }
        catch(DataAccessException e){
            return handleError(e);
        }
    }

    public Object logout(LogoutRequest req){
        String authToken = req.authToken();
        try{
            validateAuthToken(authToken);
            AuthData token = authDataAccess.getAuthToken(authToken);
            authDataAccess.deleteAuthToken(token);
            return new LogoutResponse("");
        }
        catch (DataAccessException e){
            return handleError(e);
        }
    }

    private String hashPassword(String plainTextPassword) {
        return  BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    private static String generateToken() {
        return UUID.randomUUID().toString();
    }

    private void validateAuthToken(String authToken) throws DataAccessException{
        AuthData data = authDataAccess.getAuthToken(authToken);
        if(data == null) {
            throw new DataAccessException("Error: Unauthorized");
        }
    }

    private void validateUser(UserData user, String password) throws DataAccessException{
        if(!BCrypt.checkpw(password, user.password())) {
            throw new DataAccessException("Error: Unauthorized");
        }
    }

    private void validateUsername(String username) throws DataAccessException{
        if(userDataAccess.isAvailable(username)){
            throw new DataAccessException("Error: Unauthorized");
        }
    }

    private void validatePassword(String password) throws DataAccessException{
        if(password == null || password.isEmpty()){
            throw new DataAccessException("Error: Bad request");
        }
    }

    private void isAvailable(String username, String password, String email) throws DataAccessException{
        if(!userDataAccess.isAvailable(username) || !userDataAccess.isAvailable(password)
                || !userDataAccess.isAvailable(email)){
            throw new DataAccessException("Error: Already Taken");
        }
    }

    private String validateToken(String token) throws DataAccessException {
        if(authDataAccess.getAuthToken(token) == null){
            return token;
        }
        String newToken = generateToken();
        return validateToken(newToken);
    }

    private ErrorResponse handleError(DataAccessException e){
        String message = e.getMessage();

        return switch (message) {
            case "Error: Bad request" -> new ErrorResponse(400, message);
            case "Error: Unauthorized" -> new ErrorResponse(401, message);
            case "Error: Already Taken" -> new ErrorResponse(403, message);
            default -> new ErrorResponse(500, message);
        };
    }
}
