import chess.ChessBoard;
import chess.ChessGame;
import ui.Artist;
import client.ServerFacade;

import java.util.Vector;


public class Client {
    private Boolean authorized;
    private String authToken;
    private String username;
    private ChessBoard board;
    private ChessGame.TeamColor color;
    private Artist artist;
    private ServerFacade serverFacade;

    private void register(){

    }

    private void login(){

    }

    private void logout(){

    }

    private void createGame(){

    }

    private void getGameList(){

    }

    private void joinGame(){

    }

    public Client(){
        authorized = false;
        artist = new Artist();
        serverFacade = new ServerFacade();
    }

    public void preLogin(){

    }

    public void postLogin(){

    }

    public void inGame(){

    }
}
