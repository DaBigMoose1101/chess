package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import records.*;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    final private AuthDAO authDataAccess = new AuthMemoryDAO();
    final private UserDAO userDataAccess = new UserMemoryDAO();
    private UserService service;

    @BeforeEach
    void setUp() {
        authDataAccess.deleteDB();
        userDataAccess.deleteDB();
        service = new UserService(userDataAccess, authDataAccess);

    }

    @Test
    void registerUser() {
        RegisterRequest req = new RegisterRequest("BillyBoy", "Qwerty", "billyboy@gmail.com");
        RegisterResponse actual = (RegisterResponse) service.registerUser(req);
        UserData actualUser = userDataAccess.getUser("BillyBoy");

        UserData userExpected = new UserData("BillyBoy", "Qwerty", "billyboy@gmail.com");
        String expectedUsername = "BillyBoy";
        String expectedPassword = "Qwerty";
        String expectedEmail = "billyboy@gmail.com";

        assertEquals(expectedUsername, actual.username(), "Username's should match");
        assertEquals(expectedPassword, actualUser.password(), "Passwords should match");
        assertEquals(expectedEmail, actualUser.email(), "Emails should match");
        assertEquals(userExpected, actualUser, "User should be in Database");
        assertEquals(expectedUsername, authDataAccess.getAuthToken(actual.authToken()).username(),
                "AuthToken should map to user");
    }

    @Test
    void registerMultipleUsers(){
        RegisterRequest req1 = new RegisterRequest("BillyBoy", "Qwerty", "billyboy@gmail.com");
        RegisterRequest req2 = new RegisterRequest("JoeDoe", "Abc123", "JoeDoe@gmail.com");
        RegisterRequest req3 = new RegisterRequest("JaneLee", "password1", "fox@gmail.com");

        RegisterResponse actualRes1 = (RegisterResponse) service.registerUser(req1);
        RegisterResponse actualRes2 = (RegisterResponse) service.registerUser(req2);
        RegisterResponse actualRes3 = (RegisterResponse) service.registerUser(req3);

        UserData expectedUser1 = new UserData("BillyBoy", "Qwerty", "billyboy@gmail.com");
        UserData expectedUser2 = new UserData("JoeDoe", "Abc123", "JoeDoe@gmail.com");
        UserData expectedUser3 = new UserData("JaneLee", "password1", "fox@gmail.com");

        assertEquals(expectedUser1, userDataAccess.getUser(actualRes1.username()),
                "User should be in database");
        assertEquals(expectedUser2, userDataAccess.getUser(actualRes2.username()),
                "User should be in database");
        assertEquals(expectedUser3, userDataAccess.getUser(actualRes3.username()),
                "User should be in database");

        assertEquals(expectedUser1.username(), authDataAccess.getAuthToken(actualRes1.authToken()).username(),
                "Token should be in database");
        assertEquals(expectedUser2.username(), authDataAccess.getAuthToken(actualRes2.authToken()).username(),
                "Token should be in database");
        assertEquals(expectedUser3.username(), authDataAccess.getAuthToken(actualRes3.authToken()).username(),
                "Token should be in database");

    }

    @Test
    void registerUserUsernameTaken(){
        RegisterRequest req1 = new RegisterRequest("BillyBoy", "Qwerty", "billyboy@gmail.com");
        RegisterRequest req2 = new RegisterRequest("BillyBoy", "password", "billy@gmail.com");

        RegisterResponse actualRes1 = (RegisterResponse) service.registerUser(req1);
        assertInstanceOf(ErrorResponse.class, service.registerUser(req2), "Should throw Already Taken Error");
    }

    @Test
    void registerUserPasswordTaken(){
        RegisterRequest req1 = new RegisterRequest("BillyBoy", "Qwerty", "billyboy@gmail.com");
        RegisterRequest req2 = new RegisterRequest("Billy", "Qwerty", "billy@gmail.com");

        RegisterResponse actualRes1 = (RegisterResponse) service.registerUser(req1);
        assertInstanceOf(ErrorResponse.class, service.registerUser(req2), "Should throw Already Taken Error");
    }

    @Test
    void registerUserEmailTaken(){
        RegisterRequest req1 = new RegisterRequest("BillyBoy", "Qwerty", "billyboy@gmail.com");
        RegisterRequest req2 = new RegisterRequest("Billy", "Password", "billyboy@gmail.com");

        RegisterResponse actualRes1 = (RegisterResponse) service.registerUser(req1);
        assertInstanceOf(ErrorResponse.class, service.registerUser(req2), "Should throw Already Taken Error");
    }

    @Test
    void login() {
        UserData user1 = new UserData("BillyBoy", "Qwerty", "billyboy@gmail.com");
        UserData user2 = new UserData("Billy", "password", "billy@gmail.com");
        UserData user3 = new UserData("JoelLee", "abc123", "jojo@gmail.com");

        userDataAccess.addUser(user1);
        userDataAccess.addUser(user2);
        userDataAccess.addUser(user3);

        LoginRequest req = new LoginRequest("BillyBoy", "Qwerty");
        LoginResponse actual = (LoginResponse) service.login(req);

        assertEquals(user1.username(), authDataAccess.getAuthToken(actual.authToken()).username(),
                "Token should be in database");
    }

    @Test
    void loginInvalidUserName(){
        UserData user1 = new UserData("BillyBoy", "Qwerty", "billyboy@gmail.com");
        UserData user2 = new UserData("Billy", "password", "billy@gmail.com");
        UserData user3 = new UserData("JoelLee", "abc123", "jojo@gmail.com");

        LoginRequest req = new LoginRequest("BillyBo", "Qwerty");

        userDataAccess.addUser(user1);
        userDataAccess.addUser(user2);
        userDataAccess.addUser(user3);

        assertInstanceOf(ErrorResponse.class, service.login(req));
    }

    @Test
    void loginInvalidPassword(){
        UserData user1 = new UserData("BillyBoy", "Qwerty", "billyboy@gmail.com");
        UserData user2 = new UserData("Billy", "password", "billy@gmail.com");
        UserData user3 = new UserData("JoelLee", "abc123", "jojo@gmail.com");

        LoginRequest req = new LoginRequest("BillyBoy", "Qwert");

        userDataAccess.addUser(user1);
        userDataAccess.addUser(user2);
        userDataAccess.addUser(user3);

        assertInstanceOf(ErrorResponse.class, service.login(req));
    }

    @Test
    void logout() {
        AuthData token1 = new AuthData("1234abcd", "BillyBoy");

        authDataAccess.addAuthToken(token1);
        LogoutRequest req = new LogoutRequest("1234abcd");

        assertInstanceOf(LogoutResponse.class, service.logout(req), "Should return successful response");
        assertNull(authDataAccess.getAuthToken("1234abcd"), "authToken should be removed from database");
    }

    @Test
    void logoutMultipleUsers(){
        AuthData token1 = new AuthData("1234abcd", "BillyBoy");
        AuthData token2 = new AuthData("12345abcdf", "Billy");
        AuthData token3 = new AuthData("1234abcdrp", "Joel");

        authDataAccess.addAuthToken(token1);
        authDataAccess.addAuthToken(token2);
        authDataAccess.addAuthToken(token3);

        LogoutRequest req1 = new LogoutRequest("1234abcd");
        assertInstanceOf(LogoutResponse.class, service.logout(req1), "Should return successful response");
        assertNull(authDataAccess.getAuthToken("1234abcd"), "authToken should be removed from database");

        LogoutRequest req2 = new LogoutRequest("12345abcdf");
        assertInstanceOf(LogoutResponse.class, service.logout(req2), "Should return successful response");
        assertNull(authDataAccess.getAuthToken("12345abcdf"), "authToken should be removed from database");

        LogoutRequest req3 = new LogoutRequest("1234abcdrp");
        assertInstanceOf(LogoutResponse.class, service.logout(req3), "Should return successful response");
        assertNull(authDataAccess.getAuthToken("1234abcdrp"), "authToken should be removed from database");
    }

    @Test
    void logoutInvalidAuthToken(){
        AuthData token1 = new AuthData("1234abcd", "BillyBoy");
        AuthData token2 = new AuthData("12345abcdf", "Billy");
        AuthData token3 = new AuthData("1234abcdrp", "Joel");

        authDataAccess.addAuthToken(token1);
        authDataAccess.addAuthToken(token2);
        authDataAccess.addAuthToken(token3);

        LogoutRequest req = new LogoutRequest("1234abc");
        assertInstanceOf(ErrorResponse.class, service.logout(req));
    }

    @Test
    void registerLogoutLogin(){
        RegisterRequest reqR1 = new RegisterRequest("BillyBoy", "Qwerty", "billyboy@gmail.com");
        RegisterRequest reqR2 = new RegisterRequest("JoeDoe", "Abc123", "JoeDoe@gmail.com");
        RegisterRequest reqR3 = new RegisterRequest("JaneLee", "password1", "fox@gmail.com");
        UserData billy = new UserData("BillyBoy", "Qwerty", "billyboy@gmail.com");
        UserData joe = new UserData("JoeDoe", "Abc123", "JoeDoe@gmail.com");
        UserData jane = new UserData("JaneLee", "password1", "fox@gmail.com");

        RegisterResponse resR1 = (RegisterResponse )service.registerUser(reqR1);
        assertEquals(billy.username(), resR1.username());
        String authToken1 = resR1.authToken();

        RegisterResponse resR2 = (RegisterResponse) service.registerUser(reqR2);
        assertEquals(joe.username(), resR2.username());
        String authToken2 = resR2.authToken();

        RegisterResponse resR3 = (RegisterResponse) service.registerUser(reqR3);
        assertEquals(jane.username(), resR3.username());
        String authToken3 = resR3.authToken();

        LogoutRequest reqOut1 = new LogoutRequest(authToken1);
        assertInstanceOf(LogoutResponse.class, service.logout(reqOut1), "Should return successful response");
        assertNull(authDataAccess.getAuthToken(authToken1), "authToken should be removed from database");

        LogoutRequest reqOut2 = new LogoutRequest(authToken2);
        assertInstanceOf(LogoutResponse.class, service.logout(reqOut2), "Should return successful response");
        assertNull(authDataAccess.getAuthToken(authToken2), "authToken should be removed from database");

        LogoutRequest reqOut3 = new LogoutRequest(authToken3);
        assertInstanceOf(LogoutResponse.class, service.logout(reqOut3), "Should return successful response");
        assertNull(authDataAccess.getAuthToken(authToken3), "authToken should be removed from database");

        LoginRequest reqIn1 = new LoginRequest("BillyBoy", "Qwerty");
        LoginResponse resIn1 = (LoginResponse) service.login(reqIn1);
        assertInstanceOf(AuthData.class, authDataAccess.getAuthToken(resIn1.authToken()));

        LoginRequest reqIn2 = new LoginRequest("JoeDoe", "Abc123");
        LoginResponse resIn2 = (LoginResponse) service.login(reqIn2);
        assertInstanceOf(AuthData.class, authDataAccess.getAuthToken(resIn2.authToken()));

        LoginRequest reqIn3 = new LoginRequest("JaneLee", "password1");
        LoginResponse resIn3 = (LoginResponse) service.login(reqIn3);
        assertInstanceOf(AuthData.class, authDataAccess.getAuthToken(resIn3.authToken()));
    }

}