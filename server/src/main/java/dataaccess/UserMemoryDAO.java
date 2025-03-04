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
    public UserData getUser(UserData user){
        for(UserData currentUser: userDataVector){
            if(currentUser.equals(user)){
                return user;
            }
        }
       return null;
    }

    @Override
    public void updateUser(UserData user) throws DataAccessException{
        if(getUser(user) == null){
            throw new DataAccessException("User Doesn't Exist");
        }
        deleteUser(user);
        addUser(user);
    }

    @Override
    public void deleteUser(UserData user){
        userDataVector.remove(user);
    }

}
