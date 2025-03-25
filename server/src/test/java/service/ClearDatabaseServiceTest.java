package service;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class ClearDatabaseServiceTest {
    private AuthDAO authDataAccess;
    private UserDAO userDataAccess;
    private GameDAO gameDataAccess;
    private ClearDatabaseService service;

    @BeforeEach
    void setUp() throws DataAccessException {
        this.authDataAccess = new AuthDatabaseDAO();
        this.userDataAccess = new UserDatabaseDAO();
        this.gameDataAccess = new GameDatabaseDAO();
        this.service = new ClearDatabaseService(authDataAccess, userDataAccess, gameDataAccess);
        AuthData token = new AuthData("abc", "bob");
        UserData user = new UserData("bob", "gameboy", "gmail");
        GameData game = new GameData(1, null,
                null, "game", new ChessGame());

        authDataAccess.addAuthToken(token);
        userDataAccess.addUser(user);
        gameDataAccess.createGame(game);
    }


    @Test
    void deleteAllDBs() throws DataAccessException {
        service.deleteDB();
        assertEquals(0, authDataAccess.getDataBaseSize());
        assertEquals(0, userDataAccess.getDataBaseSize());
        assertEquals(0, gameDataAccess.getGameListSize());
    }
}