package dataaccess;
import java.util.ArrayList;

import chess.ChessGame;
import model.GameData;

public interface GameDAO {

    public void createGame(GameData gameData);
    public GameData getGame(int gameID);
    public ArrayList<GameData> listGames();
    public void updateGameColor(GameData game, String userName, ChessGame.TeamColor color) throws DataAccessException;
    public void deleteGame(GameData game);
    public void joinGame(int gameID, ChessGame.TeamColor color, String username) throws DataAccessException;
    public int getGameListSize();
    public void deleteDB();
}
