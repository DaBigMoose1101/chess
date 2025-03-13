package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import com.mysql.cj.xdevapi.Type;
import model.GameData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameDatabaseDAOTest {
    GameDAO gameDataAccess;
    ChessGame chess;

    @AfterEach
    void clear() throws DataAccessException{
        gameDataAccess.deleteDB();
    }

    @BeforeEach
    void setUp() throws DataAccessException {
        gameDataAccess = new GameDatabaseDAO();
        gameDataAccess.deleteDB();
        chess = new ChessGame();
    }

    @Test
    void createGame() throws DataAccessException {
        GameData game = new GameData(0, null, null, "game", chess);
        int actual = gameDataAccess.createGame(game);

        try(var conn = DatabaseManager.getConnection()){
            try(var statement = conn.prepareStatement("SELECT * FROM games")){
                try(var rs = statement.executeQuery()){
                    if(rs.next()) {
                        int expected = rs.getInt(1);
                        assertEquals(expected, actual);
                    }
                }
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Test
    void createGameBadRequest() {
        GameData gameData = new GameData(0, null, null, null, chess);
        try{
            gameDataAccess.createGame(gameData);
        }
        catch(DataAccessException e){
            assertEquals("Error: Bad request", e.getMessage());
        }
    }


    @Test
    void getGame() {
        try(var conn = DatabaseManager.getConnection()){
            try(var statement = conn.prepareStatement("INSERT INTO games (white_user," +
                    " black_user, game_name, game) VALUES(?,?,?,?)", Statement.RETURN_GENERATED_KEYS)){
                statement.setNull(1, Types.VARCHAR);
                statement.setNull(2, Types.VARCHAR);
                statement.setString(3,"game");
                statement.setString(4,new Gson().toJson(chess, ChessGame.class));
                statement.executeUpdate();
            }
            GameData actual = gameDataAccess.getGame(1);
            assertNotNull(actual);
            assertEquals(1, actual.gameID());
            assertEquals("game", actual.gameName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void getGameBadRequest(){

        try(var conn = DatabaseManager.getConnection()){
            try(var statement = conn.prepareStatement("INSERT INTO games (white_user," +
                    " black_user, game_name, game) VALUES(?,?,?,?)", Statement.RETURN_GENERATED_KEYS)){
                statement.setNull(1, Types.VARCHAR);
                statement.setNull(2, Types.VARCHAR);
                statement.setString(3,"game");
                statement.setString(4,new Gson().toJson(chess, ChessGame.class));
                statement.executeUpdate();
            }
            GameData actual = gameDataAccess.getGame(2);
            assertNull(actual);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void listGames() throws DataAccessException {
        GameData game1 = new GameData(0, null, null, "game1", chess);
        GameData game2 = new GameData(0, null, null, "game2", chess);
        GameData game3 = new GameData(0, null, null, "game3", chess);
        GameData game4 = new GameData(0, null, null, "game4", chess);
        gameDataAccess.createGame(game1);
        gameDataAccess.createGame(game2);
        gameDataAccess.createGame(game3);
        gameDataAccess.createGame(game4);

        ArrayList<GameData> games = gameDataAccess.listGames();
        int i = 1;
        String s1 = "game";
        for(GameData game : games){
            String actual = game.gameName();
            String s2 = Integer.toString(i);
            String expected = s1+s2;
            assertEquals(expected, actual);
            i++;
        }
    }

    @Test
    void getGameListSize() throws DataAccessException {
        GameData game1 = new GameData(0, null, null, "game1", chess);
        GameData game2 = new GameData(0, null, null, "game2", chess);
        GameData game3 = new GameData(0, null, null, "game3", chess);
        GameData game4 = new GameData(0, null, null, "game4", chess);
        gameDataAccess.createGame(game1);
        gameDataAccess.createGame(game2);
        gameDataAccess.createGame(game3);
        gameDataAccess.createGame(game4);

        assertEquals(4, gameDataAccess.getGameListSize());
    }

    @Test
    void deleteGame()throws DataAccessException {
        GameData game1 = new GameData(0, null, null, "game1", chess);
        GameData game2 = new GameData(0, null, null, "game2", chess);
        GameData game3 = new GameData(0, null, null, "game3", chess);
        GameData game4 = new GameData(0, null, null, "game4", chess);
        int iD1 = gameDataAccess.createGame(game1);
        int iD2 = gameDataAccess.createGame(game2);
        int iD3 = gameDataAccess.createGame(game3);
        int iD4 = gameDataAccess.createGame(game4);
        gameDataAccess.deleteGame(iD1);
        assertEquals(3, gameDataAccess.getGameListSize());
        gameDataAccess.deleteGame(iD2);
        assertEquals(2, gameDataAccess.getGameListSize());
        gameDataAccess.deleteGame(iD3);
        assertEquals(1, gameDataAccess.getGameListSize());
        gameDataAccess.deleteGame(iD4);
        assertEquals(0, gameDataAccess.getGameListSize());
    }

    @Test
    void joinGame() throws DataAccessException{
        GameData game1 = new GameData(0, null, null, "game1", chess);
        int gameID = gameDataAccess.createGame(game1);
        String usernameB = "bob";
        String usernameW = "billy";
        gameDataAccess.joinGame(gameID, ChessGame.TeamColor.BLACK, usernameB);
        gameDataAccess.joinGame(gameID, ChessGame.TeamColor.WHITE, usernameW);
        String actualB = gameDataAccess.getGame(gameID).blackUsername();
        String actualW = gameDataAccess.getGame(gameID).whiteUsername();
        assertEquals(usernameB, actualB);
        assertEquals(usernameW, actualW);
    }

    @Test
    void joinGameWrongID() throws DataAccessException{
        GameData game1 = new GameData(0, null, null, "game1", chess);
        int gameID = gameDataAccess.createGame(game1);
        String usernameB = "bob";
        try{
            gameDataAccess.joinGame(0, ChessGame.TeamColor.BLACK, usernameB);
        } catch (DataAccessException e) {
            assertEquals("Error: Bad request", e.getMessage());
        }

    }
}