package client;

import records.*;
import chess.ChessGame;
import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;
    private static String url;
    private static ServerFacade facade;
    private static String authToken;
    private static String username;
    private static String password;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        url = "http://localhost:" + port;
    }

    @AfterAll
    static void stopServer() {
        facade.deleteDB();
        server.stop();
    }

    @BeforeEach
    void initializeSeverFacade(){
        facade = new ServerFacade(url);
        facade.deleteDB();
    }


    @Test
    public void register() {
        Assertions.assertInstanceOf(RegisterResponse.class,
                facade.register("Bob", "Bob", "Bob"));

    }
    @Test
    public void registerUsernameTaken(){
        postRegisterSetUp();
        Assertions.assertInstanceOf(ErrorResponse.class, facade.register("Bob","bill","bill"));
    }
    @Test
    public void login(){
        postRegisterSetUp();
        facade.login(username, password);

    }
    @Test
    public void loginInvalidPassword(){
        postRegisterSetUp();
        facade.login(username, "bill");
    }
    @Test
    public void logout(){
        postRegisterSetUp();
        Assertions.assertInstanceOf(LogoutResponse.class, facade.logout(authToken));
    }
    @Test
    public void logoutNoAuthToken(){
        postRegisterSetUp();
        Assertions.assertInstanceOf(ErrorResponse.class, facade.logout(""));
    }
    @Test
    public void createGame(){
        postRegisterSetUp();
        Assertions.assertInstanceOf(CreateGameResponse.class, facade.createGame(authToken, "game"));
    }
    @Test
    public void createGameNoName(){
        postRegisterSetUp();
        Assertions.assertInstanceOf(ErrorResponse.class, facade.createGame(authToken, null));
    }
    @Test
    public void getGameList(){
        postRegisterSetUp();
        addGames(2);
        GamesListResponse res1 = (GamesListResponse) facade.getGameList(authToken);
        Assertions.assertEquals(2, res1.games().size());
        addGames(2);
        GamesListResponse res2 = (GamesListResponse) facade.getGameList(authToken);
        Assertions.assertEquals(4, res2.games().size());
    }
    @Test
    public void getGameListEmpty(){
        postRegisterSetUp();
        GamesListResponse res1 = (GamesListResponse) facade.getGameList(authToken);
        Assertions.assertEquals(0, res1.games().size());
    }
    @Test
    public void joinGame(){
        postRegisterSetUp();
        addGames(1);
        Assertions.assertInstanceOf(JoinGameResponse.class,
                facade.joinGame(authToken, ChessGame.TeamColor.WHITE, 1));
    }
    @Test
    public void joinGameInvalidColorChoice(){
        joinGame();
        Assertions.assertInstanceOf(ErrorResponse.class,
                facade.joinGame(authToken, ChessGame.TeamColor.WHITE, 1));
    }

    @Test
    public void deleteDB(){
        postRegisterSetUp();
        addGames(3);
        Assertions.assertInstanceOf(ClearResponse.class, facade.deleteDB());
    }
    @Test
    public void deleteDBMultiple(){
        postRegisterSetUp();
        addGames(3);
        Assertions.assertInstanceOf(ClearResponse.class, facade.deleteDB());
        postRegisterSetUp();
        addGames(5);
        Assertions.assertInstanceOf(ClearResponse.class, facade.deleteDB());
    }
    private void postRegisterSetUp(){
        RegisterResponse res = (RegisterResponse) facade.register("Bob", "bob", "bob");
        username = "Bob";
        password = "bob";
        authToken = res.authToken();
    }
    private void addGames(int n){
        for(int i = 0; i < n; i++){
            facade.createGame(authToken,"game"+i);
        }
    }

}
