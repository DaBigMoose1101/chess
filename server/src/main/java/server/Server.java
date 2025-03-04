package server;



import dataaccess.UserDAO;
import dataaccess.UserMemoryDAO;
import handler.UserHandler;
import spark.*;

public class Server {
    private UserDAO userDataAccess;

    public Server(){
        this.userDataAccess = new UserMemoryDAO();

    }
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::register);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }



    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
    private Object register(Request req, Response res) {
        UserHandler handler = new UserHandler(userDataAccess);
        return handler.register(req.body());
    }

    private Object login(Request req, Response res){

        return null;
    }

    private Object logout(Request req, Response res){

        return null;
    }

    private Object createNewGame(Request req, Response res){
        return null;
    }

    private Object joinGame(Request req, Response res){
        return null;
    }

    private Object getGameList(Request req, Response res){
        return null;
    }

    private Object clearServer(Request req, Response res){
        return null;
    }
}
