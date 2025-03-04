package dataaccess;

import model.UserData;

public interface UserDAO {
    public void addUser(UserData user);
    public UserData getUser(UserData user);
    public void updateUser(UserData user) throws DataAccessException;
    public void deleteUser(UserData user);
}
