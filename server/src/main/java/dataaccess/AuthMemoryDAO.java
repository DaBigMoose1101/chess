package dataaccess;

import model.AuthData;
import java.util.Vector;

public class AuthMemoryDAO implements AuthDAO {
    private Vector<AuthData> authDataVector;

    public AuthMemoryDAO(){
        authDataVector = new Vector<>();
    }

    @Override
    public void addAuthToken(AuthData token){
        authDataVector.add(token);
    }

    @Override
    public AuthData getAuthToken(String token) throws DataAccessException {
        for(AuthData currentData: authDataVector){
            String currentToken = currentData.authToken();
            if(currentToken.equals(token)){
                return currentData;
            }
        }
        return null;
    }

    @Override
    public void updateAuthToken(AuthData token) throws DataAccessException {
        try {
            if(getAuthToken(token.authToken()) != null){
                deleteAuthToken(token);
                addAuthToken(token);
            }
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAuthToken(AuthData token) throws DataAccessException {
        authDataVector.remove(token);
    }

    public void deleteDB(){
        for(AuthData token : authDataVector){
            try {
                deleteAuthToken(token);
            } catch (DataAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
