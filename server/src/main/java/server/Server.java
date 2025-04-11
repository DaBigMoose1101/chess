package server;


import dataaccess.*;
import handler.DatabaseAdminHandler;
import handler.GameHandler;
import handler.UserHandler;
import handler.WebSocketHandler;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import spark.*;

public class Server {
    final private UserDAO userDataAccess;
    final private AuthDAO authDataAccess;
    final private GameDAO gameDataAccess;
    public Server(){
        try {
            DatabaseManager.createDatabase();
            this.userDataAccess = new UserDatabaseDAO();
            this.authDataAccess = new AuthDatabaseDAO();
            this.gameDataAccess = new GameDatabaseDAO();
            WebSocketHandler.initialize(authDataAccess, gameDataAccess);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.webSocket("/ws", WebSocketHandler.class);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.post("/game", this::createGame);
        Spark.get("/game", this::getGameList);
        Spark.put("/game", this::joinGame);
        Spark.delete("/db", this::clearServer);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object register(Request req, Response res) {
        UserHandler handler = new UserHandler(userDataAccess, authDataAccess, res);
        return handler.register(req.body());
    }

    private Object login(Request req, Response res){
        UserHandler handler = new UserHandler(userDataAccess, authDataAccess, res);
        return handler.login(req.body());
    }

    private Object logout(Request req, Response res){
        UserHandler handler = new UserHandler(userDataAccess, authDataAccess, res);
        return handler.logout(req.headers("authorization"));

    }

    private Object createGame(Request req, Response res){
        GameHandler handler = new GameHandler(authDataAccess, gameDataAccess, res);
        return handler.createGame(req.headers("authorization"), req.body());
    }

    private Object joinGame(Request req, Response res){
        GameHandler handler = new GameHandler(authDataAccess, gameDataAccess, res);
        return handler.joinGame(req.headers("authorization"), req.body());
    }

    private Object getGameList(Request req, Response res){
        GameHandler handler = new GameHandler(authDataAccess, gameDataAccess, res);
        return handler.getGameList(req.headers("authorization"));
    }

    private Object clearServer(Request req, Response res){
        DatabaseAdminHandler handler = new DatabaseAdminHandler(authDataAccess, userDataAccess, gameDataAccess, res);
        return handler.deleteDB();
    }

}
