package dataaccess;
import java.util.Vector;
import model.UserData;



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

    public void deleteDB(){
            userDataVector = new Vector<>();
    }

    public boolean isAvailable(String attr){
        for(UserData user : userDataVector){
            if(user.password().equals(attr) || user.email().equals(attr) || user.username().equals(attr)){
                return false;
            }
        }
        return true;
    }

    public int getDataBaseSize(){
        return userDataVector.size();
    }
}
