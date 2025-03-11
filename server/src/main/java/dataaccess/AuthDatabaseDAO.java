package dataaccess;

import model.AuthData;

import java.sql.SQLException;
import java.util.Vector;

public class AuthDatabaseDAO implements AuthDAO{
    private Vector<AuthData> authDataVector;

    public AuthDatabaseDAO() throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()){
            conn.setCatalog("chess");
            var authTableStatement = """
                        CREATE TABLE IF NOT EXISTS authtokens(
                        auth_id INT NOT NULL AUTO_INCREMENT
                        token VARCHAR(500) NOT NULL UNIQUE,
                        user VARCHAR(255) NOT NULL UNIQUE,
                        PRIMARY KEY (auth_id)
                        )""";
            try(var statement = conn.prepareStatement(authTableStatement)){
                statement.executeUpdate();
            }
        }
        catch(DataAccessException | SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void addAuthToken(AuthData token) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()){
            conn.setCatalog("chess");
            String authAdd = "INSERT INTO authtokens (token, user) VALUES( ?, ?)";
            try(var statement = conn.prepareStatement(authAdd)){
                statement.setString(1, token.authToken());
                statement.setString(2, token.username());
                statement.executeUpdate();
            }

        }
        catch(DataAccessException | SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public AuthData getAuthToken(String token) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()){
            conn.setCatalog("chess");
            String authGet = "SELECT token, user FROM authtokens WHERE token = ?";
            try(var statement = conn.prepareStatement(authGet)){
                statement.setString(1, token);
                var res = statement.executeQuery();
                if(res.next()){
                    String authToken = res.getString("token");
                    String username = res.getString("user");
                    return new AuthData(authToken, username);
                }
            }
        }
        catch(DataAccessException | SQLException e){
            throw new DataAccessException(e.getMessage());
        }
        return null;
    }

    @Override
    public void deleteAuthToken(AuthData token) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()){
            conn.setCatalog("chess");
            String authDelete = "DELETE FROM authtokens WHERE token = ?";
            try(var statement = conn.prepareStatement(authDelete)){
                statement.setString(1, token.authToken());
                statement.executeUpdate();
            }

        }
        catch(DataAccessException | SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    public void deleteDB() throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()){
            conn.setCatalog("chess");
            String authTruncate = "TRUNCATE TABLE authtokens";
            try(var statement = conn.prepareStatement(authTruncate)){
                statement.executeUpdate();
            }
        }
        catch(DataAccessException | SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    public int getDataBaseSize() throws DataAccessException {
        int res = 0;
        try(var conn = DatabaseManager.getConnection()){
            conn.setCatalog("chess");
            String getTokens = "SELECT token, user FROM authtokens";
            try(var statement = conn.prepareStatement(getTokens)){
                var queryRes = statement.executeQuery();
                while(queryRes.next()){
                    res++;
                }
            }
        }
        catch(DataAccessException | SQLException e){
            throw new DataAccessException(e.getMessage());
        }
        return res;
    }
}
