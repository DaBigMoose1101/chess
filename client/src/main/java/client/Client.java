package client;

import chess.ChessMove;
import records.*;
import chess.ChessGame;
import model.GameData;
import ui.Artist;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.*;

import static java.lang.System.exit;


public class Client implements WebSocketObserver {
    private Boolean authorized;
    private String authToken;
    private ChessGame game;
    private ChessGame.TeamColor color;
    private final Artist artist;
    final private ServerFacade serverFacade;
    private HashMap<Integer, GameData> gameList;
    private boolean isPlayer;
    private boolean inGame;

    private void register(){
        Scanner s = new Scanner(System.in);
        while(!authorized) {
            System.out.println("Return to menu by leaving all fields blank");

            String username;
            String password;
            String email;
            System.out.println("Enter Username: ");
            username = s.nextLine();
            System.out.println("Enter Password");
            password = s.nextLine();
            System.out.println("Enter email");
            email = s.nextLine();
            if(email.isEmpty() && password.isEmpty() && username.isEmpty()){
                return;
            }
            var response = serverFacade.register(username, password, email);
            if(response instanceof RegisterResponse(String user, String token)){
                authorizeUser(token);
            }
            else{
                System.out.println(((ErrorResponse) response).message());
            }
        }

    }

    private void login(){
        Scanner s = new Scanner(System.in);
        while(!authorized){
            System.out.println("Return to menu by leaving all fields blank");
            String username;
            String password;
            System.out.println("Enter Username: ");
            username = s.nextLine();
            System.out.println("Enter Password");
            password = s.nextLine();

            if(password.isEmpty() && username.isEmpty()){
                return;
            }
            var response = serverFacade.login(username, password);
            if(response instanceof LoginResponse(String user, String token)){
                authorizeUser(token);
            }
            else{
                System.out.println("Error: " + ((ErrorResponse)response).message());
            }
        }
    }

    private void logout(){
       Object response = serverFacade.logout(authToken);
       if(response instanceof LogoutResponse){
           authorized = false;
       }
       else{
           System.out.println("Error: " + ((ErrorResponse)response).message());
       }
    }

    private void createGame(){
        System.out.println("Enter a game name: ");
        String gameName;
        Scanner s = new Scanner(System.in);
        gameName = s.nextLine();
        var response = serverFacade.createGame(authToken, gameName);
        if(response instanceof CreateGameResponse){
            System.out.println("Game ID: " + ((CreateGameResponse) response).gameID());
        }
        else{
            System.out.println("Error: " + ((ErrorResponse)response).message());
        }
    }

    private void getGameList(){
        Object response = serverFacade.getGameList(authToken);
        if(response instanceof GamesListResponse(Vector<GameData> games)){
            int i = 1;
            for(GameData game : games){

                System.out.printf("%d. Game name: %s, White Player: %s,Black Player: %s\n",
                        i,
                        game.gameName(),
                        game.whiteUsername(),
                        game.blackUsername());
                gameList.put(i, game);
                i++;
            }
            System.out.println("No games exist. Please create one.");
        }
    }

    private int chooseGame(){
        Scanner s = new Scanner(System.in);
        System.out.println("Enter Game ID: ");
        int iD;
        try {
            iD = s.nextInt();
           return gameList.get(iD).gameID();
        } catch (Exception e) {
            handleInvalid();
        }
        return 0;
    }

    private void joinGame(){
        int gameID = chooseGame();
        System.out.println("Choose color: 1.White 2.Black\n");
        ChessGame.TeamColor chosenColor;
        while(true) {
            int flag = getFlag();
            if (flag == 1 ) {
                chosenColor = ChessGame.TeamColor.WHITE;
                break;
            } else if (flag == 2) {
                chosenColor = ChessGame.TeamColor.BLACK;
                break;
            }
            else {
              handleInvalid();
            }
        }

        Object response = serverFacade.joinGame(authToken, chosenColor, gameID);
        if(response instanceof JoinGameResponse){
            color = chosenColor;
            isPlayer = true;
            serverFacade.connect(authToken, gameID);
        }
        else{
            System.out.println(((ErrorResponse)response).message());
        }
    }

    private void observeGame(){
        int gameId = chooseGame();
        serverFacade.connect(authToken, gameId);
    }

    private void makeMove(){
        ArrayList<ChessMove> valid = game.validMoves()
    }

    private void highlight(){

    }

    private void leaveGame(){

    }

    private void resign(){

    }

    private void help(){
        System.out.println("Select from the given menu by typing a number corresponding with the given menu item");
    }

    private void authorizeUser(String authT){
        authorized = true;
        authToken = authT;
    }

    private void printMenu(Vector<String> menu){
        for(String item: menu){
            System.out.println(item);
        }
    }

    private void preLoginLoop(){
        Vector<String> menu = new Vector<String>(List.of("1: Register", "2: Login", "3: Help", "4: Quit"));
        while(true){
            printMenu(menu);
            int flag = getFlag();
            if(flag == 1){
                register();
            }
            else if(flag == 2){
                login();
            }
            else if(flag == 3){
                help();
            }
            else if(flag == 4){
                exit(0);
            }
            else{
                handleInvalid();
            }
            if(authorized){
                postLoginLoop();
            }
        }
    }

    private void postLoginLoop(){
        Vector<String> menu = new Vector<String>(List.of("1: Create Game",
                "2: List Games", "3: Join game", "4: ObserveGame", "5: Logout", "6: Help"));
        while(authorized) {
            printMenu(menu);
            int flag = getFlag();
            if (flag == 1) {
                createGame();
            } else if (flag == 2) {
                getGameList();
            } else if (flag == 3) {
                joinGame();
                inGame = true;
                inGameLoop();
            } else if (flag == 4) {
                observeGame();
                inGame = true;
                inGameLoop();
            } else if (flag == 5) {
                logout();
            } else if (flag == 6) {
                help();
            } else {
                handleInvalid();
                postLoginLoop();
            }
        }
    }

    private void inGameLoop(){
        while(inGame){

        }
    }

    private void handleInvalid(){
        System.out.println("Invalid choice try again");
    }

    private int getFlag(){
        String response = "";
        Scanner s = new Scanner(System.in);
        response = s.nextLine();
        int returnVal;
        try {
            returnVal = Integer.parseInt(response);
            return returnVal;
        } catch (Exception e){
            return 0;
        }
    }

    private void drawBoard(){
        artist.drawBoard(game, color);
    }

    private void printMenu(){
        if(isPlayer){
            System.out.println("1: Make Move  2: Redraw Board 3: Highlight Moves " +
                    "4: Exit Game 5: resign  6:Help \n");
            handlePlayerInput();
        }
        else{
            System.out.println("1: Redraw Board 2: Exit Game 3: Help\n");
            handleObserverInput();
        }
    }

    private void handlePlayerInput(){
        int flag = getFlag();
        switch (flag){
            case 1:
                makeMove();
                break;
            case 2:
                artist.drawBoard(game, color);
                break;
            case 3:
                highlight();
                break;
            case 4:
                leaveGame();
                inGame = false;
                break;
            case 5:
                resign();
                break;
            case 6:
                help();
                break;
        }

    }

    private void handleObserverInput(){
        int flag = getFlag();
        switch (flag){
            case 1:
                artist.drawBoard(game, color);
                break;
            case 2:
                leaveGame();
                inGame = false;
                break;
            case 3:
                help();
                break;
        }

    }

    public Client(){
        authorized = false;
        artist = new Artist();
        serverFacade = new ServerFacade("http://localhost:8080", this);
        gameList = new HashMap<>();
    }

    public Client(int port){
        String p = Integer.toString(port);
        authorized = false;
        artist = new Artist();
        serverFacade = new ServerFacade("http://localhost:" + p, this);
    }

    public void startLoop(){
        System.out.println("Welcome to Big Moose Chess");
        System.out.println("Please choose from the following menus");
        preLoginLoop();
        System.out.println("Goodbye!");
    }

    public void notify(ServerMessage message){

        switch(message.getServerMessageType()){
            case LOAD_GAME:
                game = ((LoadGameMessage) message).getGame().game();
                drawBoard();
                printMenu();
                break;
            case NOTIFICATION:
                System.out.println(((NotificationMessage) message).getMessage());
                printMenu();
                break;
            case ERROR:
                System.out.println(((ErrorMessage)message).getErrorMessage());
                printMenu();
                break;
        }

    }

}
