package server;



import dataaccess.*;
import handler.DatabaseAdminHandler;
import handler.GameHandler;
import handler.UserHandler;
import spark.*;

public class Server {
    final private UserDAO userDataAccess;
    final private AuthDAO authDataAccess;
    final private GameDAO gameDataAccess;

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
        Spark.get("/game", this::getGameList);
        Spark.put("/game", this::joinGame);
        Spark.delete("/db", this::clearServer);
        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Response register(Request req, Response res) {
        UserHandler handler = new UserHandler(userDataAccess, authDataAccess, res);
        return handler.register(req.body());
    }

    private Response login(Request req, Response res){
        UserHandler handler = new UserHandler(userDataAccess, authDataAccess, res);
        return handler.login(req.body());
    }

    private Response logout(Request req, Response res){
        UserHandler handler = new UserHandler(userDataAccess, authDataAccess, res);
        return handler.logout(req.headers("authorization"));

    }

    private Response createGame(Request req, Response res){
        GameHandler handler = new GameHandler(authDataAccess, gameDataAccess, res);
        return handler.createGame(req.headers("authorization"), req.body());
    }

    private Response joinGame(Request req, Response res){
        GameHandler handler = new GameHandler(authDataAccess, gameDataAccess, res);
        return handler.joinGame(req.headers("authorization"), req.body());
    }

    private Response getGameList(Request req, Response res){
        GameHandler handler = new GameHandler(authDataAccess, gameDataAccess, res);
        return handler.getGameList(req.headers("authorization"));
    }

    private Response clearServer(Request req, Response res){
        DatabaseAdminHandler handler = new DatabaseAdminHandler(authDataAccess, userDataAccess, gameDataAccess, res);
        return handler.deleteDB();
    }
}
