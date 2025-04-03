package client;

import records.*;
import chess.ChessBoard;
import chess.ChessGame;
import model.GameData;
import ui.Artist;
import websocket.messages.ServerMessage;
import java.util.HashMap;
import java.util.Scanner;
import java.util.List;
import java.util.Vector;



public class Client implements WebSocketObserver {
    private Boolean authorized;
    private String authToken;
    private ChessBoard board;
    private ChessGame.TeamColor color;
    private final Artist artist;
    final private ServerFacade serverFacade;
    private HashMap<Integer, GameData> gameList;

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

    private boolean joinGame(){
        Scanner s = new Scanner(System.in);
        System.out.println("Enter Game ID: ");
        int iD;
        int gameID = 0;
        try {
            iD = s.nextInt();
            gameID = gameList.get(iD).gameID();
        } catch (Exception e) {
            handleInvalid();
            return false;
        }
        System.out.println("Choose color: 1.White 2.Black");
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
              return false;
            }
        }
        Object response = serverFacade.joinGame(authToken, chosenColor, gameID);
        if(response instanceof JoinGameResponse){
            color = chosenColor;
            return true;
        }
        else{
            System.out.println(((ErrorResponse)response).message());
            return false;
        }
    }

    private void observeGame(){
        Scanner s = new Scanner(System.in);
        int gameID = 0;
        int iD = getFlag();
        try{
            gameID = gameList.get(iD).gameID();
        }
        catch (Exception e){
            handleInvalid();
            return;
        }
        artist.drawBoard(board, ChessGame.TeamColor.WHITE);
        System.out.println("1. exit game");
        int flag = getFlag();
        if(flag == 1){
            return;
        }
        else{
            handleInvalid();
            observeGame();
        }


    }

    private void makeMove(){

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
                return;
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
                "2: List Games", "3: Play game", "4: ObserveGame", "5: Logout", "6: Help"));
        while(true){
            printMenu(menu);
            int flag = getFlag();
            if(flag == 1){
                createGame();
            }
            else if(flag == 2){
                getGameList();
            }
            else if(flag == 3){
                if(joinGame()) {
                    inGameLoop(true);
                }
            }
            else if(flag == 4){
                observeGame();
            }
            else if(flag == 5){
                logout();
                return;
            }
            else if(flag == 6){
                help();
            }
            else{
                handleInvalid();
            }

        }
    }

    private void inGameLoop(Boolean isPlayer){
        String menu = "1: Make Move  2: ExitGame  3:Help";
        while(true){
            System.out.println("\033[H\033[2J");
            System.out.flush();
            artist.drawBoard(board, color);
            if(isPlayer) {
                System.out.println(menu);
                int flag = getFlag();
                if(flag == 1){
                    //make move
                }
                else if(flag == 2){
                    return;
                }
                else if(flag == 3){
                    help();
                }
                else{
                    handleInvalid();
                }
            }

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
    public Client(){
        authorized = false;
        artist = new Artist();
        serverFacade = new ServerFacade("http://localhost:8080");
        board = new ChessBoard();
        board.resetBoard();
        gameList = new HashMap<>();
    }

    public Client(int port){
        String p = Integer.toString(port);
        authorized = false;
        artist = new Artist();
        serverFacade = new ServerFacade("http://localhost:" + p);
        board = new ChessBoard();
        board.resetBoard();
    }

    public void startLoop(){
        System.out.println("Welcome to Big Moose Chess");
        System.out.println("Please choose from the following menus");
        preLoginLoop();
        System.out.println("Goodbye!");
    }

    public void notify(ServerMessage message){

    }

}
