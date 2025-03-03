package dataaccess;

import model.UserData;

public interface UserDAO {
    public void createUser();
    public void readUser();
    public void updateUser();
    public void deleteUser(String username);
}
