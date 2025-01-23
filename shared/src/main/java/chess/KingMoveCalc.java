package chess;

import java.util.Collection;
import java.util.ArrayList;


public class KingMoveCalc implements PieceMoveCalc{
    private ChessBoard board;
    private ChessPosition position;
    private Collection<ChessMove> moves;

    public KingMoveCalc(ChessBoard board, ChessPosition position){
        this.board = board;
        this.position = position;
        this.moves = new ArrayList<>();
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
        for(int i = move1; i >= move2; i--){
            for(int j = move3; j >= move4; j--){
                if(isValidMove(i, j)){
                    ChessPosition pos = new ChessPosition(i, j);
                    if(checkPosition(pos)){
                        ChessMove move = new ChessMove(position, pos, null);
                        moves.add(move);
                    }
                }
            }
        }
        return moves;
    }
    private boolean checkPosition(ChessPosition pos){
        if(board.getPiece(pos) == null
                || (board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() != board.getPiece(position).getTeamColor())){
            return true;
        }
        return false;
    }
    private boolean isValidMove(int row, int col){
        if (row >= 1 && row < 9 && col >= 1 && col < 9){
            return true;
        }
        return false;
    }
}
