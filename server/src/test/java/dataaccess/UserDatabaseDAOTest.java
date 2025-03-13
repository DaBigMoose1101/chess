package dataaccess;

import model.UserData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class UserDatabaseDAOTest {
    UserDAO userDataAccess;

    @AfterEach
    void clear() throws DataAccessException{
        userDataAccess.deleteDB();
    }

    @BeforeEach
    void setUp() throws DataAccessException {
        userDataAccess = new UserDatabaseDAO();
        userDataAccess.deleteDB();
    }

    @Test
    void updateUsername() throws DataAccessException{
        String expectedUsername = "Bob";
        String expectedPassword = "password";
        String expectedEmail = "email";
        UserData user = new UserData("Bill", expectedPassword, expectedEmail);
        userDataAccess.addUser(user);
        int iD = userDataAccess.getUserID("Bill");
        userDataAccess.updateUsername(expectedUsername, iD);
        UserData actual = userDataAccess.getUser(expectedUsername);
        assertEquals(expectedUsername, actual.username());

    }

    @Test
    void updateUsernameBadRequest() throws DataAccessException{
        String expectedUsername = "Bob";
        String expectedPassword = "password";
        String expectedEmail = "email";
        UserData user = new UserData("Bill", expectedPassword, expectedEmail);
        userDataAccess.addUser(user);
        userDataAccess.updateUsername(expectedUsername, 0);
        assertNull(userDataAccess.getUser(expectedUsername));
    }

    @Test
    void updatePassword() throws DataAccessException{
        String expectedUsername = "Bob";
        String expectedPassword = "password";
        String expectedEmail = "email";
        UserData user = new UserData(expectedUsername, "abcde", expectedEmail);
        userDataAccess.addUser(user);
        int iD = userDataAccess.getUserID(expectedUsername);
        userDataAccess.updatePassword(expectedPassword, iD);
        UserData actual = userDataAccess.getUser(expectedUsername);
        assertEquals(expectedPassword, actual.password());
    }

    @Test
    void updatePasswordBadRequest() throws DataAccessException{
        String expectedUsername = "Bob";
        String expectedPassword = "password";
        String expectedEmail = "email";
        UserData user = new UserData(expectedUsername, "abcde", expectedEmail);
        userDataAccess.addUser(user);
        int iD = userDataAccess.getUserID(expectedUsername);
        userDataAccess.updatePassword(expectedPassword, 0);
        UserData actual = userDataAccess.getUser(expectedUsername);
        assertNotEquals(expectedPassword, actual.password());
    }

    @Test
    void updateEmail() throws DataAccessException{
        String expectedUsername = "Bob";
        String expectedPassword = "password";
        String expectedEmail = "email";
        UserData user = new UserData(expectedUsername, expectedPassword, "expectedEmail");
        userDataAccess.addUser(user);
        int iD = userDataAccess.getUserID(expectedUsername);
        userDataAccess.updateEmail(expectedEmail, iD);
        UserData actual = userDataAccess.getUser(expectedUsername);
        assertEquals(expectedEmail, actual.email());
    }

    @Test
    void updateEmailBadRequest() throws DataAccessException{
        String expectedUsername = "Bob";
        String expectedPassword = "password";
        String expectedEmail = "email";
        UserData user = new UserData(expectedUsername, expectedPassword, "expectedEmail");
        userDataAccess.addUser(user);
        int iD = userDataAccess.getUserID(expectedUsername);
        userDataAccess.updateEmail(expectedEmail, 0);
        UserData actual = userDataAccess.getUser(expectedUsername);
        assertNotEquals(expectedEmail, actual.email());

    }

    @Test
    void addUser() throws DataAccessException {
        String expectedUsername = "Bob";
        String expectedPassword = "password";
        String expectedEmail = "email";
        UserData user = new UserData(expectedUsername, expectedPassword, expectedEmail);
        userDataAccess.addUser(user);
        try(var conn = DatabaseManager.getConnection()){
            try(var statement = conn.prepareStatement("SELECT username FROM users WHERE username = ?")){
                statement.setString(1, expectedUsername);
                var rs = statement.executeQuery();
                rs.next();
                assertEquals(expectedUsername, rs.getString("username"));
            }
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void addDuplicateUser() throws DataAccessException{
        String expectedUsername = "Bob";
        String expectedPassword = "password";
        String expectedEmail = "email";
        UserData user = new UserData(expectedUsername, expectedPassword, expectedEmail);
        userDataAccess.addUser(user);
        try {
            userDataAccess.addUser(user);
        } catch (DataAccessException e) {
            assertEquals("Duplicate entry 'Bob' for key 'users.username'", e.getMessage());
        }
    }

    @Test
    void getUser() throws DataAccessException{
        String expectedUsername = "Bob";
        String expectedPassword = "password";
        String expectedEmail = "email";
        UserData user = new UserData(expectedUsername, expectedPassword, expectedEmail);
        userDataAccess.addUser(user);
        UserData actual = userDataAccess.getUser(expectedUsername);
        assertNotNull(actual);
        assertEquals(expectedUsername, actual.username());
    }

    @Test
    void getNoUser() throws DataAccessException{
        assertNull(userDataAccess.getUser("Bob"));
    }

    @Test
    void getUserID() throws DataAccessException{
        String expectedUsername = "Bob";
        String expectedPassword = "password";
        String expectedEmail = "email";
        UserData user = new UserData(expectedUsername, expectedPassword, expectedEmail);
        userDataAccess.addUser(user);
        int actual = userDataAccess.getUserID(expectedUsername);
        assertEquals(1, actual);
    }

    @Test
    void getUserIdWrongUsername() throws DataAccessException{
        String expectedUsername = "Bob";
        String expectedPassword = "password";
        String expectedEmail = "email";
        UserData user = new UserData(expectedUsername, expectedPassword, expectedEmail);
        userDataAccess.addUser(user);
        try{
            userDataAccess.getUserID("bill");
        }catch (DataAccessException e){
            assertEquals("Error: Bad request", e.getMessage());
        }
    }

    @Test
    void deleteDB() throws DataAccessException{
        String expectedUsername = "Bob";
        String expectedPassword = "password";
        String expectedEmail = "email";
        UserData user = new UserData(expectedUsername, expectedPassword, expectedEmail);
        userDataAccess.addUser(user);
        userDataAccess.deleteDB();
        assertEquals(0, userDataAccess.getDataBaseSize());
    }

    @Test
    void isAvailable() throws DataAccessException {
        assertTrue(userDataAccess.isAvailable("username"));
    }

    @Test
    void isNotAvailable() throws DataAccessException {
        String expectedUsername = "Bob";
        String expectedPassword = "password";
        String expectedEmail = "email";
        UserData user = new UserData(expectedUsername, expectedPassword, expectedEmail);
        userDataAccess.addUser(user);
        assertFalse(userDataAccess.isAvailable("Bob"));
    }

    @Test
    void getDataBaseSize() throws DataAccessException {
        String expectedUsername = "Bob";
        String expectedPassword = "password";
        String expectedEmail = "email";
        UserData user = new UserData(expectedUsername, expectedPassword, expectedEmail);
        userDataAccess.addUser(user);

        assertEquals(1, userDataAccess.getDataBaseSize());

    }
}