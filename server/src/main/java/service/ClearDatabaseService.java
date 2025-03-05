package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import records.ClearResponse;
import records.ErrorResponse;

public class ClearDatabaseService {
    final private AuthDAO authDataAccess;
    final private UserDAO userDataAccess;
    final private GameDAO gameDataAccess;

    public ClearDatabaseService(AuthDAO authDataAccess, UserDAO userDataAccess, GameDAO gameDataAccess){
        this.authDataAccess = authDataAccess;
        this.userDataAccess = userDataAccess;
        this.gameDataAccess = gameDataAccess;
    }

    public Object deleteDB(){
        try {
            authDataAccess.deleteDB();
            userDataAccess.deleteDB();
            gameDataAccess.deleteDB();
            return new ClearResponse(null);
        }
        catch (DataAccessException e){
            return handleError(e);
        }
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
