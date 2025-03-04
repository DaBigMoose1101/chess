package server;



import dataaccess.*;
import handler.UserHandler;
import spark.*;

public class Server {
    private UserDAO userDataAccess;
    private AuthDAO authDataAccess;
    private GameDAO gameDataAccess;

    public Server(){
        this.userDataAccess = new UserMemoryDAO();
        this.authDataAccess = new AuthMemoryDAO();
        this.gameDataAccess = new GameMemoryDAO();

    }
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
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
        UserHandler handler = new UserHandler(userDataAccess, authDataAccess);
        return handler.register(req.body());
    }

    private Object login(Request req, Response res){
        UserHandler handler = new UserHandler(userDataAccess, authDataAccess);
        return handler.login(req.body());
    }

    private Object logout(Request req, Response res){
        UserHandler handler = new UserHandler(userDataAccess, authDataAccess);
        return handler.logout(req.headers("Authorization"));

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
