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

    @Override
    public void updateUser(UserData user) throws DataAccessException{
        if(getUser(user.username()) == null){
            throw new DataAccessException("User Doesn't Exist");
        }
        deleteUser(user);
        addUser(user);
    }

    @Override
    public void deleteUser(UserData user){
        userDataVector.remove(user);
    }

    public void deleteDB(){
        for(UserData user : userDataVector){
            deleteUser(user);
        }
    }

    public boolean isAvailable(String attr){
        for(UserData user : userDataVector){
            if(user.password().equals(attr) || user.email().equals(attr) || user.username().equals(attr)){
                return false;
            }
        }
        return true;
    }
}
