package chess;

import java.util.Collection;
import java.util.ArrayList;


public class KingMoveCalc implements PieceMoveCalc{
    private ChessBoard board;
    private ChessPosition position;
    private Collection<ChessMove> moves = new ArrayList<>();

    public KingMoveCalc(ChessBoard board, ChessPosition position){
        this.board = board;
        this.position = position;
    }
    @Override
    public Collection<ChessMove> getMoves() {
        ChessPiece.PieceType type = null;
        int row = position.getRow();
        int col = position.getColumn();
        int move1 = row+1;
        int move2 = row-1;
        int move3 = col+1;
        int move4 = col-1;
        if (move1 < 8 && move2 >= 0 && move3 < 8 && move4 >= 0){
            ChessPosition pos1 = new ChessPosition(move1, move4);
            ChessPosition pos2 = new ChessPosition(move1, col);
            ChessPosition pos3 = new ChessPosition(move1, move3);
            ChessPosition pos4 = new ChessPosition(row, move4);
            ChessPosition pos5 = new ChessPosition(row, move3);
            ChessPosition pos6 = new ChessPosition(move2, move4);
            ChessPosition pos7 = new ChessPosition(move2, col);
            ChessPosition pos8 = new ChessPosition(move2, move3);
            if(checkPosition(pos1)){
                ChessMove move = new ChessMove(position, pos1, type);
                moves.add(move);
            }
        }
        return moves;
    }
    private boolean checkPosition(ChessPosition pos){
        if(board.getPiece(position) == null){
            return true;
        }
        if(board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() != board.getPiece(position).getTeamColor()){
            return true;
        }
        return false;
    }
}
