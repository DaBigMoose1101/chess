package dataaccess;

import chess.ChessGame;
import model.GameData;
import java.util.ArrayList;

public class GameMemoryDAO implements GameDAO{
    private ArrayList<GameData> gameList;

    @Override
    public void createGame(GameData gameData) throws DataAccessException{
        gameList.add(gameData);
        if(getGame(gameData.gameID()) == null){
            throw new DataAccessException("Failed to create game.");
        }
    }

    @Override
    public GameData getGame(int gameID){
        for(GameData game : gameList){
            int currentGameID = game.gameID();
            if (gameID == currentGameID){
                return game;
            }
        }
        return null;
    }

    @Override
    public ArrayList<GameData> listGames(){
        return gameList;
    }

    @Override
    public int getGameListSize(){
        return gameList.size();
    }

    @Override
    public void updateGame(int gameID){

    }

    @Override
    public void deleteGame(int gameID){
        GameData game = getGame(gameID);
        gameList.remove(game);
    }

    @Override
    public void joinGame(int gameID, ChessGame.TeamColor color, String username) throws DataAccessException{
        GameData game = getGame(gameID);
        if(game == null){
            throw new DataAccessException("Game does not exist");
        }
        switch(color){
            case BLACK:
                GameData tempBlack = new GameData(game.gameID(), game.whiteUsername(),
                        username, game.gameName(), game.game());
                break;
            case WHITE:
                GameData tempWhite = new GameData(game.gameID(), username,
                        game.whiteUsername(), game.gameName(), game.game());
            case null, default:
                throw new DataAccessException("Invalid color choice");
        }

    }
}
