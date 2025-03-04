package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;
import model.AuthData;
import records.*;
import chess.ChessGame;
import chess.ChessGame.TeamColor;

public class GameService {
    final AuthDAO authDataAccess;
    final GameDAO gameDataAccess;

    public GameService(AuthDAO authDataAccess, GameDAO gameDataAccess){
        this.authDataAccess = authDataAccess;
        this.gameDataAccess = gameDataAccess;
    }

    public Object createGame(CreateGameRequest req, String authToken){
        try {
            validateUser(authToken);
            int gameID = generateGameID();
            String whiteUsername = "";
            String blackUsername = "";
            String gameName = req.gameName();
            ChessGame game = new ChessGame();
            GameData gameData = new GameData(gameID, whiteUsername, blackUsername, gameName, game);
            gameDataAccess.createGame(gameData);
            return new CreateGameResponse(gameID);
        }
        catch(DataAccessException e){
            return e;
        }

    }

    public Object joinGame(JoinGameRequest req, String authToken){
        try {
            AuthData authData = validateUser(authToken);
            String username = authData.username();
            TeamColor color = req.color();
            gameDataAccess.joinGame(req.gameID(), color, username);
            return new JoinGameResponse("");
        }
        catch(DataAccessException e){
            return e;
        }
    }

    public Object getGameList(GamesListRequest req, String authToken){

        return null;
    }

    private AuthData validateUser(String authToken) throws DataAccessException{
        AuthData data = authDataAccess.getAuthToken(authToken);
        if(data == null) {
            throw new DataAccessException("Unauthorized");
        }
        return data;
    }

    private int generateGameID(){
        int numOfGames = gameDataAccess.getGameListSize();
        return numOfGames + 1;
    }

}
