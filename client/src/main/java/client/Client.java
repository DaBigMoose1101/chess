package client;

import chess.ChessBoard;
import chess.ChessGame;
import records.CreateGameResponse;
import records.ErrorResponse;
import records.LoginResponse;
import records.RegisterResponse;
import ui.Artist;
import java.util.Scanner;
import java.util.List;
import java.util.Vector;


public class Client {
    private Boolean authorized;
    private String authToken;
    private ChessBoard board;
    private ChessGame.TeamColor color;
    private final Artist artist;
    final private ServerFacade serverFacade;

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
            if(response instanceof RegisterResponse){
                authorized = true;
                authToken = ((RegisterResponse) response).authToken();
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
            if(response instanceof LoginResponse){

            }
        }
    }

    private void logout(){
        authorized = false;
    }

    private void createGame(){
        System.out.println("Enter a game name: ");
        String gameName;
        Scanner s = new Scanner(System.in);
        gameName = s.nextLine();
        var response = serverFacade.createGame(authToken, gameName);
        if(response instanceof CreateGameResponse){
            System.out.println(((CreateGameResponse) response).gameID());
        }
        else{
            System.out.println("Error:\n");
        }
    }

    private void getGameList(){

    }

    private void joinGame(){

    }

    private void help(String type){

    }

    private void printMenu(Vector<String> menu){
        for(String item: menu){
            System.out.println(item);
        }
    }

    private void preLoginLoop(){
        Vector<String> menu = new Vector<String>(List.of("1: Register", "2: Login", "3: Help", "4: Quit"));
        String response;
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
                help("pre");
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
        String response;
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
                joinGame();
                inGameLoop(true);
            }
            else if(flag == 4){
                joinGame();
                inGameLoop(false);
            }
            else if(flag == 5){
                logout();
                return;
            }
            else if(flag == 6){
                help("post");
            }
            else{
                handleInvalid();
            }

        }
    }

    private void inGameLoop(Boolean isPlayer){
        String menu = "1: Make Move  2: ExitGame  3:Help";
        while(true){
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
                    help("game");
                }
                else{
                    handleInvalid();
                }
            }
            artist.clear();
            artist.drawBoard(board, color);
        }
    }

    private void handleInvalid(){
        System.out.println("Invalid choice try again");
    }

    private int getFlag(){
        String response = "";
        Scanner s = new Scanner(System.in);
        response = s.nextLine();
        return Integer.parseInt(response);
    }
    public Client(){
        authorized = false;
        artist = new Artist();
        serverFacade = new ServerFacade(8080, "http//:localHost:");
    }

    public void startLoop(){
        System.out.println("Welcome to Big Moose Chess");
        System.out.println("Please choose from the following menus");
        preLoginLoop();
        System.out.println("Goodbye!");
    }


}
