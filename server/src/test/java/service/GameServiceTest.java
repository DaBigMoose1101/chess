package service;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import records.*;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {
    private static AuthDAO authDataAccess;
    private static GameDAO gameDataAccess;
    private GameService service;
    private AuthData token1;
    private AuthData token2;

    @AfterAll
    static void clearDB() throws DataAccessException {
        authDataAccess.deleteDB();
        gameDataAccess.deleteDB();

    }

    @BeforeEach
    void setUp() throws DataAccessException {
        authDataAccess = new AuthDatabaseDAO();
        gameDataAccess = new GameDatabaseDAO();
        authDataAccess.deleteDB();
        gameDataAccess .deleteDB();
        this.service = new GameService(authDataAccess, gameDataAccess);
        this.token1 = new AuthData("abc", "bob");
        this.token2 = new AuthData("def", "bill");
        authDataAccess.addAuthToken(token1);
        authDataAccess.addAuthToken(token2);

    }

    @Test
    void createGame() throws DataAccessException {
        CreateGameRequest req = new CreateGameRequest("game");
        CreateGameResponse res = (CreateGameResponse) service.createGame(req, token1.authToken());
        int gameID = res.gameID();
        assertEquals(req.gameName(), gameDataAccess.getGame(gameID).gameName());
    }

    @Test
    void createGameInvalidAuthToken() {
        CreateGameRequest req = new CreateGameRequest("game");
        assertInstanceOf(ErrorResponse.class, service.createGame(req,"ab"));
    }

    @Test
    void createMultipleGames() throws DataAccessException {
        CreateGameRequest req1 = new CreateGameRequest("game1");
        CreateGameResponse res1 = (CreateGameResponse) service.createGame(req1, token1.authToken());
        int gameID1 = res1.gameID();
        assertEquals(req1.gameName(), gameDataAccess.getGame(gameID1).gameName(),
                "Game should be in database");

        CreateGameRequest req2 = new CreateGameRequest("game2");
        CreateGameResponse res2 = (CreateGameResponse) service.createGame(req2, token1.authToken());
        int gameID2 = res2.gameID();

        assertEquals(req2.gameName(), gameDataAccess.getGame(gameID2).gameName(),
                "Game should be in database");

        CreateGameRequest req3 = new CreateGameRequest("game3");
        CreateGameResponse res3 = (CreateGameResponse) service.createGame(req3, token1.authToken());
        int gameID3 = res3.gameID();

        assertEquals(req3.gameName(), gameDataAccess.getGame(gameID3).gameName(),
                "Game should be in database");
    }

    @Test
    void joinGame() throws DataAccessException {
        CreateGameRequest createReq = new CreateGameRequest("game");
        CreateGameResponse createRes = (CreateGameResponse) service.createGame(createReq, token1.authToken());
        int gameID = createRes.gameID();

        JoinGameRequest joinBlackReq = new JoinGameRequest(ChessGame.TeamColor.BLACK, gameID);
        assertInstanceOf(JoinGameResponse.class, service.joinGame(joinBlackReq, token1.authToken()),
                "Invalid Join Response");

        GameData game = gameDataAccess.getGame(gameID);
        assertEquals(token1.username(), game.blackUsername(), "Usernames should match");

        JoinGameRequest joinWhiteReq = new JoinGameRequest(ChessGame.TeamColor.WHITE, gameID);
        assertInstanceOf(JoinGameResponse.class, service.joinGame(joinWhiteReq, token2.authToken()),
                "Invalid Join Response");

        game = gameDataAccess.getGame(gameID);
        assertEquals(token2.username(), game.whiteUsername(), "Usernames should match");

    }

    @Test
    void joinGameInvalidAuthToken() {
        CreateGameRequest createReq = new CreateGameRequest("game");
        CreateGameResponse createRes = (CreateGameResponse) service.createGame(createReq, token1.authToken());
        int gameID = createRes.gameID();

        JoinGameRequest joinReq = new JoinGameRequest(ChessGame.TeamColor.BLACK, gameID);
        assertInstanceOf(ErrorResponse.class, service.joinGame(joinReq, token1.authToken()+"a"),
                "Should Return ErrorResponse");
    }

    @Test
    void joinGameInvalidGameId() {
        CreateGameRequest createReq = new CreateGameRequest("game");
        CreateGameResponse createRes = (CreateGameResponse) service.createGame(createReq, token1.authToken());
        int gameID = createRes.gameID();

        JoinGameRequest joinReq = new JoinGameRequest(ChessGame.TeamColor.BLACK, gameID + 1);
        assertInstanceOf(ErrorResponse.class, service.joinGame(joinReq, token1.authToken()),
                "Should Return ErrorResponse");
    }

    @Test
    void getGameListEmpty() {
        GamesListRequest gameListReq = new GamesListRequest(token1.authToken());
        GamesListResponse gameListRes = (GamesListResponse) service.getGameList(gameListReq);

        assert(gameListRes.games().isEmpty());
    }

    @Test
    void getGameList1Game() {
        CreateGameRequest createReq = new CreateGameRequest("game");
        assertInstanceOf(CreateGameResponse.class, service.createGame(createReq, token1.authToken()));

        GamesListRequest gameListReq = new GamesListRequest(token1.authToken());
        GamesListResponse gameListRes = (GamesListResponse) service.getGameList(gameListReq);

        assertEquals(1, gameListRes.games().size(), "Should only be one game");
    }

    @Test
    void getGameListMultipleGames() {
        for(int i = 0; i < 3; i++){
            CreateGameRequest createReq = new CreateGameRequest("game");
            assertInstanceOf(CreateGameResponse.class, service.createGame(createReq, token1.authToken()));
        }
        GamesListRequest gameListReq = new GamesListRequest(token1.authToken());
        GamesListResponse gameListRes = (GamesListResponse) service.getGameList(gameListReq);
        assertEquals(3, gameListRes.games().size(), "Should be 3 games");
        for(GameData game: gameListRes.games()){
            assertNull(game.game());
        }
    }

    @Test
    void addAndRemoveGameList() throws DataAccessException {
        Vector<Integer> gameIDs = new Vector<>();
        for(int i = 0; i < 6; i++){
            CreateGameRequest createReq = new CreateGameRequest("game");
            CreateGameResponse createRes = (CreateGameResponse) service.createGame(createReq, token1.authToken());
            gameIDs.add(createRes.gameID());
        }
        for(int i = 5; i > 0; i--){
            GamesListRequest gameListReq = new GamesListRequest(token1.authToken());
            GamesListResponse gameListRes = (GamesListResponse) service.getGameList(gameListReq);

            assertEquals(i + 1, gameListRes.games().size(), "Wrong number of games");

            GameData game = gameDataAccess.getGame(i+1);
            gameDataAccess.deleteGame(game.gameID());
        }
    }

    @Test
    void fullGameGameList() throws DataAccessException {
        var serializer = new Gson();
        Vector<GameData> games = new Vector<>();
        GameData game = new GameData(1,null, null, "game", null);
        games.add(game);
        GamesListResponse expectedResponse = new GamesListResponse(games);
        String expectedString = serializer.toJson(expectedResponse);

        gameDataAccess.createGame(game);
        GamesListRequest listRequest = new GamesListRequest(token1.authToken());
        String actualString = serializer.toJson(service.getGameList(listRequest));

        assertEquals(expectedString, actualString);


    }

    @Test
    void emptyGameGameList(){
        var serializer = new Gson();
        Vector<GameData> games = new Vector<>();
        GameData game = new GameData(1, null,
                null, "game", null);
        games.add(game);
        GamesListResponse expectedResponse = new GamesListResponse(games);
        String expectedString = serializer.toJson(expectedResponse);

        CreateGameRequest createReq = new CreateGameRequest("game");
        assertInstanceOf(CreateGameResponse.class, service.createGame(createReq, token1.authToken()));
        GamesListRequest listRequest = new GamesListRequest(token1.authToken());
        String actualString = serializer.toJson(service.getGameList(listRequest));

        assertEquals(expectedString, actualString);
    }
}