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
    public AuthData getAuthToken(String token){
        for(AuthData currentData: authDataVector){
            String currentToken = currentData.authToken();
            if(currentToken.equals(token)){
                return currentData;
            }
        }
        return null;
    }

    @Override
    public void deleteAuthToken(AuthData token){
        authDataVector.remove(token);
    }

    public void deleteDB(){
        authDataVector = new Vector<>();
    }

    public int getDataBaseSize(){
        return authDataVector.size();
    }
}
