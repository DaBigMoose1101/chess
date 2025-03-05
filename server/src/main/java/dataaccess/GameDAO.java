package dataaccess;
import java.util.ArrayList;

import chess.ChessGame;
import model.GameData;

public interface GameDAO {

    public void createGame(GameData gameData) throws DataAccessException;
    public GameData getGame(int gameID) throws DataAccessException;
    public ArrayList<GameData> listGames();
    public void updateGameName(GameData game,String newName);
    public void updateGameColor(GameData game, String userName, ChessGame.TeamColor color) throws DataAccessException;
    public void updateGame(GameData game,ChessGame updatedGame);
    public void deleteGame(GameData game)throws DataAccessException;
    public void joinGame(int gameID, ChessGame.TeamColor color, String username) throws DataAccessException;
    public int getGameListSize();
    public void deleteDB();
}
