package service;
import java.util.UUID;
import dataaccess.UserDAO;
import records.RegisterRequest;
import records.RegisterResponse;

public class UserService {
    String username;
    String password;
    String email;
    UserDAO userDataAccess;
    public UserService(RegisterRequest req, UserDAO userDataAccess){
        this.username = req.username();
        this.password = req.password();
        this.email = req.email();
        this.userDataAccess = userDataAccess;
    }

    public RegisterResponse registerUser(){

    }
}
