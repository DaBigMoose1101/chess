package dataaccess;

import model.UserData;

public interface UserDAO {
    public void addUser(UserData user);
    public UserData getUser(String username);
    public boolean isAvailable(String attr);
    public void deleteDB();
    public int getDataBaseSize();
}
