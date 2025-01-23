package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMoveCalc implements PieceMoveCalc {
    private ChessBoard board;
    private ChessPosition position;
    private Collection<ChessMove> moves;

    public BishopMoveCalc(ChessBoard Board, ChessPosition pos){
        this.board = Board;
        this.position = pos;
        this.moves = new ArrayList<>();
    }

    @Override
    public Collection<ChessMove> getMoves() {
        ChessPiece.PieceType type = null;
        getDiagonal1();
        getDiagonal2();
        getDiagonal3();
        getDiagonal4();
        return moves;
    }
    private boolean checkPosition(ChessPosition pos){
        if(board.getPiece(pos) == null){
            return true;
        }
        return false;
    }

    private void getDiagonal1(){
        int row = position.getRow()+1;
        int col = position.getColumn()+1;
        while(row < 9 && col < 9){
            ChessPosition pos = new ChessPosition(row, col);
            if(checkPosition(pos)){
                ChessMove move = new ChessMove(position, pos, null);
                moves.add(move);
                row++;
                col++;
            }
            else if ( board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() != board.getPiece(position).getTeamColor()){
                ChessMove move = new ChessMove(position, pos, null);
                moves.add(move);
                break;
            }
            else {
                break;
            }

        }
    }
    private void getDiagonal2() {
        int row = position.getRow()+1;
        int col = position.getColumn()-1;
        while (row < 9 && col >= 1) {
            ChessPosition pos = new ChessPosition(row, col);
            if (checkPosition(pos)) {
                ChessMove move = new ChessMove(position, pos, null);
                moves.add(move);
                row++;
                col--;
            } else if (board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                ChessMove move = new ChessMove(position, pos, null);
                moves.add(move);
                break;
            } else {
                break;
            }
        }
    }
    private void getDiagonal3() {
        int row = position.getRow()-1;
        int col = position.getColumn()-1;
        while (row >= 1 && col >= 1) {
            ChessPosition pos = new ChessPosition(row, col);
            if (checkPosition(pos)) {
                ChessMove move = new ChessMove(position, pos, null);
                moves.add(move);
                row--;
                col--;
            } else if (board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                ChessMove move = new ChessMove(position, pos, null);
                moves.add(move);
                break;
            } else {
                break;
            }
        }
    }
    private void getDiagonal4() {
        int row = position.getRow()-1;
        int col = position.getColumn()+1;
        while (row >=1  && col < 9) {
            ChessPosition pos = new ChessPosition(row, col);
            if (checkPosition(pos)) {
                ChessMove move = new ChessMove(position, pos, null);
                moves.add(move);
                row--;
                col++;
            } else if (board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                ChessMove move = new ChessMove(position, pos, null);
                moves.add(move);
                break;
            } else {
                break;
            }
        }
    }
}
