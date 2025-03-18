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
        margin(" ");
        for(String ch: labels){
            drawSquare(ch);
        }
        margin(" ");
    }

    private void drawRow( Vector<Integer> printOrderNums, int index, String currentColor){
        margin(printOrderNums.get(index).toString());
        String ch;
        margin(printOrderNums.elementAt(index).toString());
        int i = 0;
        for(int num : printOrderNums){
            ChessPosition pos = new ChessPosition(index, num);
            ch = getIcon(board.getPiece(pos), " ");
            setIconColor(board.getPiece(pos));
            drawSquare(ch);
            if(i != 7){
                currentColor = changeSquareColor(currentColor);
            }
            i++;
        }
        margin(printOrderNums.get(index).toString());
    }

    private String getIcon(ChessPiece piece, String ch){
        String res = ch;
        switch (piece.getPieceType()){
            case ChessPiece.PieceType.KING:
                res = "K";
                break;
            case ChessPiece.PieceType.QUEEN:
                res = "Q";
                break;
            case ChessPiece.PieceType.BISHOP:
                res = "B";
                break;
            case ChessPiece.PieceType.KNIGHT:
                res = "N";
                break;
            case ChessPiece.PieceType.ROOK:
                res = "R";
                break;
            case ChessPiece.PieceType.PAWN:
                res = "P";
                break;
        }
        return res;
    }

    private void setIconColor(ChessPiece piece){
        out.print(SET_TEXT_COLOR_BLACK);
        if(piece != null && piece.getTeamColor() == ChessGame.TeamColor.WHITE){
            out.print(SET_TEXT_COLOR_WHITE);
        }
    }

    private void margin(String ch){
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_BLUE);
        out.print(ch);
    }

    private void drawSquare(String ch){
        out.print(" ");
        out.print(ch);
        out.print(" ");
    }

    private String changeSquareColor(String current){
        if(current.equals("light")){
            out.print(SET_BG_COLOR_DARK_GREY);
            return "dark";
        }
        else{
            out.print(SET_BG_COLOR_LIGHT_GREY);
            return "light";
        }
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
                break;
            default:
                printOrderNums = new Vector<Integer>(List.of(8,7,6,5,4,3,2,1));
                printOrderChars = new Vector<String>(List.of("a","b","c","d","e","f","g","h"));
                break;
        }
        drawHeaderFooter(printOrderChars);
        for(int i = 0; i < printOrderNums.size(); i++){
            String currentColor;
            if(i % 2 == 0){
                currentColor = "light";
            }
            else{
                currentColor = "dark";
            }
            drawRow(printOrderNums, printOrderNums.elementAt(i), currentColor);
        }
        drawHeaderFooter(printOrderChars);
    }

    public void clear(){
        out.print(ERASE_SCREEN);
    }

}
