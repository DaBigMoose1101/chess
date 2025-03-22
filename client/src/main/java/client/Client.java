package client;

import chess.ChessBoard;
import chess.ChessGame;
import ui.Artist;

import java.util.Scanner;
import java.util.List;
import java.util.Vector;


public class Client {
    private Boolean authorized;
    private String authToken;
    private String username;
    private ChessBoard board;
    private ChessGame.TeamColor color;
    private Artist artist;
    final private ServerFacade serverFacade;

    private void register(){
        authorized = true;
    }

    private void login(){
        authorized = true;
    }

    private void logout(){
        authorized = false;
    }

    private void createGame(){

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
                authorized = true;
            }
            else if(flag == 2){
                login();
                authorized = true;
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
        serverFacade = new ServerFacade();
    }

    public void startLoop(){
        System.out.println("Welcome to Big Moose Chess");
        System.out.println("Please choose from the following menus");
        preLoginLoop();
        System.out.println("Goodbye!");
    }


}
