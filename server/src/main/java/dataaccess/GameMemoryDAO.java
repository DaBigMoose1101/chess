package dataaccess;

import chess.ChessGame;
import model.GameData;
import java.util.ArrayList;

public class GameMemoryDAO implements GameDAO{
    private ArrayList<GameData> gameList;

    public GameMemoryDAO(){
        gameList = new ArrayList<>();
    }

    @Override
    public void createGame(GameData gameData){
        gameList.add(gameData);
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
    public void updateGameColor(GameData game, String username,
                                ChessGame.TeamColor color) throws DataAccessException{
        switch(color){
            case BLACK:
                GameData tempBlack = new GameData(game.gameID(), game.whiteUsername(),
                        username, game.gameName(), game.game());
                deleteGame(game);
                createGame(tempBlack);
                break;
            case WHITE:
                GameData tempWhite = new GameData(game.gameID(), username,
                        game.whiteUsername(), game.gameName(), game.game());
                deleteGame(game);
                createGame(tempWhite);
                break;
            default:
                throw new DataAccessException("Error: Invalid Color");
        }
    }

    @Override
    public void deleteGame(GameData game){
        gameList.remove(game);
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

    public void deleteDB(){
        gameList = new ArrayList<>();
    }

    private boolean hasSpace(GameData game){
        return game.whiteUsername() == null || game.blackUsername() == null;
    }
}
