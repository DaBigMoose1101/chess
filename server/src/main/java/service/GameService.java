package service;

import records.*;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;
import model.AuthData;
import chess.ChessGame;
import chess.ChessGame.TeamColor;

import java.util.ArrayList;
import java.util.Vector;

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
            String gameName = req.gameName();
            ChessGame game = new ChessGame();
            GameData gameData = new GameData(getGameId(), null, null, gameName, game);
            int gameID = gameDataAccess.createGame(gameData);
            return new CreateGameResponse(gameID);
        }
        catch(DataAccessException e){
           return handleError(e);
        }

    }

    public Object joinGame(JoinGameRequest req, String authToken){
        try {
            AuthData authData = validateUser(authToken);
            String username = authData.username();
            TeamColor color = req.playerColor();
            gameDataAccess.joinGame(req.gameID(), color, username);
            return new JoinGameResponse();
        }
        catch(DataAccessException e){
            return handleError(e);

        }
    }

    public Object getGameList(GamesListRequest req){
        String authToken = req.authToken();
        Vector<GameData> returnData = new Vector<>();
        try {
            validateUser(authToken);
            ArrayList<GameData> gameList = gameDataAccess.listGames();
            for(GameData game: gameList){
                int id = game.gameID();
                String white = game.whiteUsername();
                String black = game.blackUsername();
                String name = game.gameName();
                GameData gameData = new GameData(id, white, black, name, null);
                returnData.add(gameData);
            }
            return new GamesListResponse(returnData);
        }
        catch(DataAccessException e){
            return handleError(e);
        }

    }

    private AuthData validateUser(String authToken) throws DataAccessException{
        AuthData data = authDataAccess.getAuthToken(authToken);
        if(data == null) {
            throw new DataAccessException("Error: Unauthorized");
        }
        return data;
    }

    private int getGameId() throws DataAccessException {
        int iD = gameDataAccess.getGameListSize();
        return iD + 1;
    }

    private ErrorResponse handleError(DataAccessException e){
        String message = e.getMessage();

        return switch (message) {
            case "Error: Bad request" -> new ErrorResponse(400, message);
            case "Error: Unauthorized" -> new ErrorResponse(401, message);
            case "Error: Already Taken" -> new ErrorResponse(403, message);
            default -> new ErrorResponse(500, message);
        };
    }

}
