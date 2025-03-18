package ui;


import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Vector;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import static ui.EscapeSequences.*;

public class Artist {
    PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    private ChessBoard board;
    private ChessGame.TeamColor playerColor;
    private int boardWidth = 26;
    private int boardHeight = 10;
    private int squareWidth = 3;
    private int squareHeight = 1;

    private void drawHeaderFooter(Vector<String> labels){
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_BOLD);
        out.print(SET_TEXT_COLOR_BLUE);
    }

    private void drawRow(Vector<String> printOrderChars, Vector<Integer> printOrderNums, int index){
        margin(printOrderChars.get(index));
        for(int i = 1; i < 9; i++){
            ChessPosition pos = new ChessPosition(index, printOrderNums.get(i));
            ChessPiece piece = board.getPiece(pos);
            drawSquare(piece);
        }
        margin(printOrderChars.get(index));
    }

    private void margin(String ch){

    }

    private void drawSquare(ChessPiece piece){

    }

    public Artist(chess.ChessBoard board, ChessGame.TeamColor playerColor){
        this.board = board;
        this.playerColor = playerColor;
    }

    public void drawBoard(){
        Vector<Integer> printOrderNums;
        Vector<String> printOrderChars;
        switch(playerColor){
            case BLACK:
                printOrderNums = new Vector<Integer>(List.of(1,2,3,4,5,6,7,8));
                printOrderChars = new Vector<String>(List.of("h","g","f","e","d","c","b","a"));
            default:
                printOrderNums = new Vector<Integer>(List.of(8,7,6,5,4,3,2,1));
                printOrderChars = new Vector<String>(List.of("a","b","c","d","e","f","g","h"));
        }
    }

    public void clear(){
        out.print(ERASE_SCREEN);
    }

}
