package ui;


import chess.ChessBoard;
import chess.ChessGame;

import java.util.List;
import java.util.Vector;

public class Artist {
    private ChessBoard board;
    private ChessGame.TeamColor color;

    private void drawHeader(){

    }

    private void drawFooter(){

    }

    private void drawMainBoard(){

    }

    public Artist(chess.ChessBoard board, ChessGame.TeamColor color){
        this.board = board;
        this.color = color;
    }

    public void drawBoard(){
        Vector<Integer> printOrderNums;
        Vector<String> printOrderChars;
        switch(color){
            case BLACK:
                printOrderNums = new Vector<Integer>(List.of(8,7,6,5,4,3,2,1));
                printOrderChars = new Vector<String>(List.of("h","g","f","e","d","c","b","a"));
            default:
                printOrderNums = new Vector<Integer>(List.of(1,2,3,4,5,6,7,8));
                printOrderChars = new Vector<String>(List.of("a","b","c","d","e","f","g","h"));
        }
    }

}
