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
    PrintStream out;
    private ChessBoard board;
    private ChessGame.TeamColor playerColor;

    public static void main(String[] args){
        ChessBoard board = new ChessBoard();
        ChessGame.TeamColor color = ChessGame.TeamColor.BLACK;
        board.resetBoard();
        Artist a = new Artist(board, color);
        a.clear();
        a.drawBoard();

    }

    private void drawHeaderFooter(Vector<String> labels){
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_BOLD);
        out.print(SET_TEXT_COLOR_BLUE);
        margin("  ");
        for(String ch: labels){
            drawSquare(ch);
        }
        margin("  ");
        out.print(RESET_BG_COLOR);
        out.println();
    }

    private void drawRow( Vector<Integer> printOrderNums, int index, String currentColor){
        String label = printOrderNums.get(index-1).toString();
        margin(" "+label);
        String ch;
        setSquareLight();
        if(currentColor.equals("dark")) setSquareDark();
        for(int num : printOrderNums){
            ChessPosition pos = new ChessPosition(index, num);
            ch = getIcon(board.getPiece(pos), " ");
            setIconColor(board.getPiece(pos));
            drawSquare(ch);
            currentColor = changeSquareColor(currentColor);
        }
        margin(label + " ");
        out.print(RESET_BG_COLOR);
        out.println();
    }

    private String getIcon(ChessPiece piece, String ch){
        if(piece == null) return ch;
        return switch (piece.getPieceType()){
            case ChessPiece.PieceType.KING-> "K";
            case ChessPiece.PieceType.QUEEN-> "Q";
            case ChessPiece.PieceType.BISHOP->"B";
            case ChessPiece.PieceType.KNIGHT->"N";
            case ChessPiece.PieceType.ROOK->"R";
            case ChessPiece.PieceType.PAWN->"P";
        };

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

    private void setSquareLight(){
        out.print(SET_BG_COLOR_LIGHT_GREY);
    }

    private void setSquareDark(){
        out.print(SET_BG_COLOR_DARK_GREY);
    }

    private String changeSquareColor(String current){
        if(current.equals("light")){
            setSquareDark();
            return "dark";
        }
        else{
            setSquareLight();
            return "light";
        }
    }

    public Artist(chess.ChessBoard board, ChessGame.TeamColor playerColor){
        this.board = board;
        this.playerColor = playerColor;
        out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
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
        out.println();
    }

}
