import chess.*;
import client.Client;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess client.Client: " + piece);
        Client c = new Client();
        c.startLoop();
    }
}