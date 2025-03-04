package dataaccess;
import java.util.ArrayList;

import chess.ChessGame;
import model.GameData;

public interface GameDAO {

    public void createGame(GameData gameData) throws DataAccessException;
    public GameData getGame(int gameID);
    public ArrayList<GameData> listGames();
    public void updateGame(int gameID);
    public void deleteGame(int gameID);
    public void joinGame(int gameID, ChessGame.TeamColor color, String username) throws DataAccessException;
    public int getGameListSize();
}
