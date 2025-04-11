package dataaccess;
import java.util.ArrayList;

import chess.ChessGame;
import model.GameData;

public interface GameDAO {

    public int createGame(GameData gameData) throws DataAccessException;
    public GameData getGame(int gameID) throws DataAccessException;
    public ArrayList<GameData> listGames() throws DataAccessException;
    public void updateGameColor(GameData game, String userName, ChessGame.TeamColor color) throws DataAccessException;
    public void deleteGame(int gameID) throws DataAccessException;
    public void joinGame(int gameID, ChessGame.TeamColor color, String username) throws DataAccessException;
    public int getGameListSize() throws DataAccessException;
    public void deleteDB() throws DataAccessException;
    void updateGame(GameData game) throws DataAccessException;
}
