package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

public class GameDatabaseDAO implements GameDAO{
    public GameDatabaseDAO() throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()){
            var gameTableStatement = """
                    CREATE TABLE IF NOT EXISTS games(
                    game_id INT NOT NULL AUTO_INCREMENT,
                    white_user VARCHAR(255),
                    black_user VARCHAR(255),
                    game_name VARCHAR(255) NOT NULL,
                    game TEXT NOT NULL,
                    PRIMARY KEY (game_id)
                    )""";
            PreparedStatement statement = conn.prepareStatement(gameTableStatement);
            statement.executeUpdate();
        } catch (DataAccessException | SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public int createGame(GameData gameData) throws DataAccessException {
        String gameName = gameData.gameName();
        String game = new Gson().toJson(gameData.game());
        try(var conn = DatabaseManager.getConnection()){
            try(var statement =
                        conn.prepareStatement("INSERT INTO games (white_user," +
                                " black_user, game_name, game) VALUES(?,?,?,?)", Statement.RETURN_GENERATED_KEYS)){
                if(gameData.gameName() != null){
                    statement.setString(3, gameName);
                }
                else{
                    throw new DataAccessException("Error: Bad request");
                }
                statement.setNull(1, Types.VARCHAR);
                statement.setNull(2, Types.VARCHAR);
                statement.setString(4, game);
                statement.executeUpdate();

                var res = statement.getGeneratedKeys();
                if(res.next()){
                    return res.getInt(1);
                }
            }
        } catch (DataAccessException | SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return 0;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()){
            try(var statement =
                        conn.prepareStatement("SELECT * FROM games WHERE game_id = ?")){
                statement.setInt(1, gameID);
                try(var rs = statement.executeQuery()){
                    if(rs.next()){
                        int id = rs.getInt("game_id");
                        String white = rs.getString("white_user");
                        String black = rs.getString("black_user");
                        String gameName = rs.getString("game_name");
                        ChessGame game = new Gson().fromJson(rs.getString("game"), ChessGame.class);
                        return new GameData(id, white, black, gameName, game);
                    }
                }
            }
        } catch (DataAccessException | SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        ArrayList<GameData> games = new ArrayList<>();
        try(var conn = DatabaseManager.getConnection()){
            try(var statement =
                        conn.prepareStatement("SELECT *  FROM games")){
                try(var rs = statement.executeQuery()){
                    while(rs.next()){
                        int id = rs.getInt("game_id");
                        String white = rs.getString("white_user");
                        String black = rs.getString("black_user");
                        String gameName = rs.getString("game_name");
                        ChessGame game = new Gson().fromJson(rs.getString("game"), ChessGame.class);
                        games.add(new GameData(id, white, black, gameName, game));
                    }
                }
            }
        } catch (DataAccessException | SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return games;
    }

    @Override
    public int getGameListSize() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try(PreparedStatement statement
                        = conn.prepareStatement("SELECT COUNT(*) FROM games")) {
                var rs = statement.executeQuery();
                rs.next();
                return rs.getInt(1);
            }

        }
        catch (SQLException | DataAccessException e) {
            String message = e.getMessage();
            throw new DataAccessException(message);
        }
    }


    @Override
    public void updateGameColor(GameData game, String username, ChessGame.TeamColor color) throws DataAccessException{

        try(var conn = DatabaseManager.getConnection()){
            switch(color){
                case BLACK:
                    String updateStatementB = "UPDATE games SET black_user = ? WHERE game_id = ?";
                    try(var statement = conn.prepareStatement(updateStatementB)){
                        if(username.matches("[a-zA-Z1-9]+")){
                            statement.setString(1, username);
                        }
                        statement.setInt(2, game.gameID());
                        statement.executeUpdate();
                    }
                    break;
                case WHITE:
                    String updateStatementW = "UPDATE games SET white_user = ? WHERE game_id = ?";
                    try(var statement = conn.prepareStatement(updateStatementW)){
                        if(username.matches("[a-zA-Z1-9]+")){
                            statement.setString(1, username);
                        }
                        statement.setInt(2, game.gameID());
                        statement.executeUpdate();
                    }
                    break;
                default:
                    throw new DataAccessException("Error: Invalid Color");
            }
        } catch (DataAccessException | SQLException e) {
            throw new DataAccessException(e.getMessage());
        }

    }

    @Override
    public void deleteGame(int gameID) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()){
            try(var statement = conn.prepareStatement("DELETE FROM games WHERE game_id = ?")){
                statement.setInt(1, gameID);
                statement.executeUpdate();
            }
        } catch (DataAccessException | SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void joinGame(int gameID, ChessGame.TeamColor color, String username) throws DataAccessException{
        GameData game = getGame(gameID);
        if(game == null || color == null){
            throw new DataAccessException("Error: Bad request");
        }
        if( !hasSpace(game)){
            throw new DataAccessException("Error: Game is full");
        }
        switch(color){
            case BLACK:
                if(game.blackUsername() == null){
                    updateGameColor(game, username, color);
                    break;
                }
                else{
                    throw new DataAccessException("Error: Already Taken");
                }
            case WHITE:
                if(game.whiteUsername() == null) {
                    updateGameColor(game, username, color);
                }
                else{
                    throw new DataAccessException("Error: Already Taken");
                }
                break;
            default:
                throw new DataAccessException("Error: Already Taken");
        }
    }

    public void deleteDB() throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()){
            try(var statement = conn.prepareStatement("TRUNCATE TABLE games")){
                statement.executeUpdate();
            }

        } catch (DataAccessException | SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private boolean hasSpace(GameData game){
        return game.whiteUsername() == null || game.blackUsername() == null;
    }
}
