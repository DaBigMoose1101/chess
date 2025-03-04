package server;



import dataaccess.*;
import handler.GameHandler;
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
        Spark.post("/game", this::createGame);
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
        return handler.logout(req.headers("authorization"));

    }

    private Object createGame(Request req, Response res){
        GameHandler handler = new GameHandler(authDataAccess, gameDataAccess);
        return handler.createGame(req.headers("authorization"), req.body());
    }

    private Object joinGame(Request req, Response res){
        GameHandler handler = new GameHandler(authDataAccess, gameDataAccess);
        return handler.joinGame(req.headers("authorization"), req.body());
    }

    private Object getGameList(Request req, Response res){
        GameHandler handler = new GameHandler(authDataAccess, gameDataAccess);
        return handler.getGameList(req.headers("authorization"));
    }

    private Object clearServer(Request req, Response res){
        return null;
    }
}
