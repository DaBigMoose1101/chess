package client;

import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void register() {
        Assertions.assertTrue(true);
    }
    @Test
    public void registerUsernameTaken(){

    }
    @Test
    public void login(){

    }
    @Test
    public void loginInvalidPassword(){

    }
    @Test
    public void logout(){

    }
    @Test
    public void logoutNoAuthToken(){

    }
    @Test
    public void createGame(){

    }
    @Test
    public void createGameNoName(){

    }
    @Test
    public void getGameList(){

    }
    @Test
    public void joinGame(){

    }
    @Test
    public void joinGameInvalidColorChoice(){

    }

}
