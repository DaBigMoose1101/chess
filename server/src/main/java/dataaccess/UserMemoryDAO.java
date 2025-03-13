package dataaccess;
import java.util.Vector;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;


public class UserMemoryDAO implements UserDAO {
    private Vector<UserData> userDataVector;
    public UserMemoryDAO(){
        this.userDataVector = new Vector<>();
    }

    @Override
    public void addUser(UserData user){
        userDataVector.add(user);
    }

    @Override
    public UserData getUser(String username){
        for(UserData currentUser: userDataVector){
            String currentUsername = currentUser.username();
            if(currentUsername.equals(username)){
                return currentUser;
            }
        }
       return null;
    }

    @Override
    public int getUserID(String userName) throws DataAccessException {
        return 0;
    }

    @Override
    public void updateUsername(String newUsername, int userID) throws DataAccessException {

    }

    @Override
    public void updatePassword(String newPassword, int userID) throws DataAccessException {

    }

    @Override
    public void updateEmail(String newEmail, int userID) throws DataAccessException {

    }

    public void deleteDB(){
            userDataVector = new Vector<>();
    }

    public boolean isAvailable(String attr){
        for(UserData user : userDataVector){
            if(BCrypt.checkpw(attr, user.password()) || user.email().equals(attr) || user.username().equals(attr)){
                return false;
            }
        }
        return true;
    }

    public int getDataBaseSize(){
        return userDataVector.size();
    }
}
