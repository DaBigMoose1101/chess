package dataaccess;
import java.util.ArrayList;
import chess.ChessGame;

public interface GameDAO {

    public void createGame();
    public void getGame();
    public ArrayList<ChessGame> listGames();
    public void updateGame();
    public void deleteGame();
    public void joinGame(ChessGame game);
}
