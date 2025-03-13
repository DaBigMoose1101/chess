package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class AuthDatabaseDAOTest {
    AuthDAO authDataAccess;

    @BeforeEach
    void setUp() throws DataAccessException {
        authDataAccess = new AuthDatabaseDAO();
        authDataAccess.deleteDB();
    }

    @Test
    void addAuthToken() throws DataAccessException {
        String token = "aabb";
        String username = "Bob";
        AuthData data = new AuthData(token, username);
        authDataAccess.addAuthToken(data);
        try(var conn = DatabaseManager.getConnection()){
            try(var statement = conn.prepareStatement("SELECT token FROM authtokens WHERE token = ?")){
                statement.setString(1, token);
                var rs = statement.executeQuery();
                if(rs.next()){
                    assertEquals(token, rs.getString("token"));
                }
            }
        } catch (DataAccessException | SQLException e){
            throw new DataAccessException(e.getMessage());
        }

    }

    @Test
    void addDuplicateToken() throws DataAccessException {
        String token = "aabb";
        String username = "Bob";
        AuthData data = new AuthData(token, username);
        authDataAccess.addAuthToken(data);
        try{
            authDataAccess.addAuthToken(data);
        } catch (DataAccessException e){
            assertEquals("Duplicate entry 'aabb' for key 'authtokens.token'", e.getMessage());
        }
    }

    @Test
    void getAuthToken() throws DataAccessException {
        String token = "aabb";
        String username = "Bob";
        AuthData data = new AuthData(token, username);
        authDataAccess.addAuthToken(data);
        AuthData actual = authDataAccess.getAuthToken(token);
        assertEquals(token, actual.authToken());
    }

    @Test
    void getInvalidAuthToken() throws DataAccessException {
        String token = "aabb";
        String username = "Bob";
        AuthData data = new AuthData(token, username);
        authDataAccess.addAuthToken(data);
        assertNull(authDataAccess.getAuthToken("ab"));
    }

    @Test
    void deleteAuthToken() throws DataAccessException {
        String token = "aabb";
        String username = "Bob";
        AuthData data = new AuthData(token, username);
        authDataAccess.addAuthToken(data);
        assertEquals(1, authDataAccess.getDataBaseSize());
        authDataAccess.deleteAuthToken(data);
        assertEquals(0, authDataAccess.getDataBaseSize());
    }

    @Test
    void deleteInvalidAuthToken() throws DataAccessException {
        String token = "aabb";
        String username = "Bob";
        AuthData data = new AuthData(token, username);
        AuthData deleteMe = new AuthData("hehe", "boo");
        authDataAccess.addAuthToken(data);
        assertEquals(1, authDataAccess.getDataBaseSize());
        authDataAccess.deleteAuthToken(deleteMe);
        assertEquals(1, authDataAccess.getDataBaseSize());
    }

    @Test
    void deleteDB() throws DataAccessException {
        AuthData data1 = new AuthData("aabb", "Bob");
        AuthData data2 = new AuthData("a", "username");
        AuthData data3 = new AuthData("wow", "Earl");
        authDataAccess.addAuthToken(data1);
        authDataAccess.addAuthToken(data2);
        authDataAccess.addAuthToken(data3);
        assertEquals(3, authDataAccess.getDataBaseSize());
        authDataAccess.deleteDB();
        assertEquals(0, authDataAccess.getDataBaseSize());
    }

    @Test
    void getDataBaseSize() throws DataAccessException {
        AuthData data1 = new AuthData("aabb", "Bob");
        authDataAccess.addAuthToken(data1);
        assertEquals(1, authDataAccess.getDataBaseSize());
        AuthData data2 = new AuthData("a", "username");
        authDataAccess.addAuthToken(data2);
        assertEquals(2, authDataAccess.getDataBaseSize());
        AuthData data3 = new AuthData("wow", "Earl");
        authDataAccess.addAuthToken(data3);
        assertEquals(3, authDataAccess.getDataBaseSize());
        authDataAccess.deleteDB();
        assertEquals(0, authDataAccess.getDataBaseSize());
    }
}