package client;

import chess.*;
import com.google.gson.Gson;
import records.*;
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
    private int gameID;

    private void register(){
        Scanner s = new Scanner(System.in);
        while(!authorized) {
            System.out.println("Return to menu by leaving all fields blank\n");

            String username;
            String password;
            String email;
            System.out.println("Enter Username: ");
            username = s.nextLine();
            System.out.println("Enter Password: ");
            password = s.nextLine();
            System.out.println("Enter email: ");
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
            System.out.println("Return to menu by leaving all fields blank\n");
            String username;
            String password;
            System.out.println("Enter Username: ");
            username = s.nextLine();
            System.out.println("Enter Password: ");
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
            if(games.isEmpty()){
                System.out.println("No Games exist please Create one.");
            }
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
        gameID = chooseGame();
        System.out.println("Choose color: 1.White 2.Black");
        ChessGame.TeamColor chosenColor;
        int flag = getFlag();
        if (flag == 1 ) {
            chosenColor = ChessGame.TeamColor.WHITE;
        } else if (flag == 2) {
            chosenColor = ChessGame.TeamColor.BLACK;
        }
        else {
            handleInvalid();
            return;
        }

        Object response = serverFacade.joinGame(authToken, chosenColor, gameID);
        if(response instanceof JoinGameResponse){
            color = chosenColor;
            isPlayer = true;
            serverFacade.connect(authToken, gameID);
            inGame = true;
        }
        else{
            System.out.println(((ErrorResponse)response).message());
        }
    }

    private void observeGame(){
        gameID = chooseGame();
        color = ChessGame.TeamColor.WHITE;
        serverFacade.connect(authToken, gameID);
        isPlayer = false;
        inGame = true;
    }

    private int toInt(char c){
        return switch(c){
            case 'a' -> 1;
            case 'b' -> 2;
            case 'c' -> 3;
            case 'd' -> 4;
            case 'e' -> 5;
            case 'f' -> 6;
            case 'g' -> 7;
            case 'h' -> 8;
            default -> 0;
        };
    }


    private boolean isValidInput(String input){
        return input.matches("[1-8][a-h][1-8][a-h]");
    }

    private boolean canPromote(ChessPosition start, ChessPosition end){
        ChessPiece p = game.getBoard().getPiece(start);
        if(p != null && p.getPieceType() == ChessPiece.PieceType.PAWN){
            return end.getRow() == 1 || end.getRow() == 8;
        }
        return false;
    }

    private ChessPiece.PieceType getPromotion(){
        while(true) {
            System.out.println("1: Queen 2: Bishop 3: Knight 4: Rook\n");
            int flag = getFlag();
            if(flag == 1){
                return ChessPiece.PieceType.QUEEN;
            }
            else if(flag == 2){
                return ChessPiece.PieceType.BISHOP;
            }
            else if(flag == 3){
                return ChessPiece.PieceType.KNIGHT;
            }
            else if(flag == 4){
                return ChessPiece.PieceType.ROOK;
            }
        }

    }

    private void makeMove(){
        Scanner s = new Scanner(System.in);
        System.out.println("Enter a move by typing the starting position " +
                "and ending position without spaces. (i.e. 2a3a\n");
        System.out.println("Enter move: ");
        String input = s.nextLine();
        ChessPosition start;
        ChessPosition end;
        ChessPiece.PieceType promotion = null;
        if(isValidInput(input)){
            start = new ChessPosition(Integer.parseInt(String.valueOf(input.charAt(0))),
                    toInt(input.charAt(1)));
            end = new ChessPosition(Integer.parseInt(String.valueOf(input.charAt(2))),
                    toInt(input.charAt(3)));
            if(canPromote(start, end)){
                promotion = getPromotion();
            }
            Collection<ChessMove> valid = game.validMoves(start);
            ChessMove move = new ChessMove(start, end, promotion);
            if(valid.contains(move)){
                serverFacade.makeMove(authToken, gameID, move);
            }
        }
        else{
            System.out.println("Invalid move\n");
            printGamePlayMenu();
        }

    }

    private void highlight(){
        Scanner s = new Scanner(System.in);
        System.out.println("Enter a Starting Position (i.e. 1a): ");
        String input = s.nextLine();
        if(input.matches("[1-8][a-h]")){
            ChessPosition pos =
                    new ChessPosition(Integer.parseInt(String.valueOf(input.charAt(0))), toInt(input.charAt(1)));
            Collection<ChessMove> highlighted = game.validMoves(pos);
            artist.setMoves(highlighted);
            drawBoard("");
            artist.setMoves(new ArrayList<>());
        }
        else{
            System.out.println("Invalid position\n");
            drawBoard("");
        }
    }

    private void leaveGame(){
        serverFacade.leave(authToken, gameID);
    }

    private void resign(){
        serverFacade.resign(authToken, gameID);
    }

    private void help(){
        System.out.println("Select from the given menu by typing a number corresponding with the given menu item\n");
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
            System.out.println("\n Main Menu");
            printMenu(menu);
            int flag = getFlag();
            if (flag == 1) {
                createGame();
            } else if (flag == 2) {
                getGameList();
            } else if (flag == 3) {
                joinGame();
                inGameLoop();
            } else if (flag == 4) {
                observeGame();
                inGameLoop();
            } else if (flag == 5) {
                logout();
            } else if (flag == 6) {
                help();
            } else {
                handleInvalid();
            }
        }
    }

    private void inGameLoop(){
        while (inGame) {
            int flag = getFlag();
            switch (flag) {
                case 1:
                    if (!game.isGameOver() && isPlayer) {
                        makeMove();
                    } else {
                        drawBoard("");
                    }
                    break;
                case 2:
                    if (!isPlayer) {
                        leaveGame();
                        return;
                    }
                    drawBoard("");
                    break;
                case 3:
                    if (!isPlayer) {
                        help();
                    } else {
                        if (!game.isGameOver()) {
                            highlight();
                        }
                    }
                    break;
                case 4:
                    if (isPlayer) {
                        leaveGame();
                        return;
                    }
                case 5:
                    if (!game.isGameOver() && isPlayer) {
                        resign();
                    }
                    break;
                case 6:
                    if (isPlayer) {
                        help();
                    }
                    break;
            }
        }
    }

    private void handleInvalid(){
        System.out.println("Invalid choice try again\n");
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

    private void drawBoard(String note){
        artist.drawBoard(game, color);
        System.out.println(note);
        printGamePlayMenu();
    }

    private void printGamePlayMenu(){
        if(isPlayer){
            System.out.println("1: Make Move  2: Redraw Board 3: Highlight Moves " +
                    "4: Exit Game 5: resign  6:Help ");
        }
        else{
            System.out.println("1: Redraw Board 2: Exit Game 3: Help");
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

    public void notify(String serverMessage) {
        synchronized (this) {
            ServerMessage message = new Gson().fromJson(serverMessage, ServerMessage.class);
            switch (message.getServerMessageType()) {
                case LOAD_GAME:
                    LoadGameMessage g = new Gson().fromJson(serverMessage, LoadGameMessage.class);
                    game = g.getGame().game();
                    drawBoard("");
                    break;
                case NOTIFICATION:
                    NotificationMessage n = new Gson().fromJson(serverMessage, NotificationMessage.class);
                    drawBoard(n.getMessage());
                    break;
                case ERROR:
                    ErrorMessage e = new Gson().fromJson(serverMessage, ErrorMessage.class);
                    drawBoard(e.getErrorMessage());
                    break;
            }

        }
    }

}
