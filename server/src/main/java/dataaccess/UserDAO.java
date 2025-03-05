package dataaccess;

import model.UserData;

public interface UserDAO {
    public void addUser(UserData user) throws DataAccessException;
    public UserData getUser(String username);
    public void updateUser(UserData user) throws DataAccessException;
    public void deleteUser(UserData user) throws DataAccessException;
    public boolean isAvailable(String attr);
    public void deleteDB() throws DataAccessException;
}
