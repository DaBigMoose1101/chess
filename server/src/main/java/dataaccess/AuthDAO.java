package dataaccess;

import model.AuthData;

public interface AuthDAO {
    public void addAuthToken(AuthData token);
    public AuthData getAuthToken(String token);
    public void updateAuthToken(AuthData token);
    public void deleteAuthToken(AuthData token);
    public void deleteDB();
    public int getDataBaseSize();
}
