package chess;
import java.util.Collection;
import java.util.ArrayList;


public class KnightMoveCalc implements PieceMoveCalc{
    private ChessBoard board;
    private ChessPosition position;
    private Collection<ChessMove> moves = new ArrayList<>();
    public KnightMoveCalc(ChessBoard board, ChessPosition pos){
        this.board = board;
        this.position = pos;
        this.moves = new ArrayList<>();
    }
    @Override
    public Collection<ChessMove> getMoves() {
        int row = position.getRow();
        int col = position.getColumn();
        int row1 = row + 2;
        int row2 = row + 1;
        int row3 = row - 2;
        int row4 = row - 1;
        int col1 = col + 1;
        int col2 = col + 2;
        int col3 = col - 1;
        int col4 = col - 2;
        addMove(row1, col1);
        addMove(row1, col3);

        addMove(row2, col2);
        addMove(row2, col4);

        addMove(row3, col1);
        addMove(row3, col3);

        addMove(row4, col2);
        addMove(row4, col4);
        return moves;
    }
    private void addMove(int row, int col){
        if(isValidMove(row, col)){
            ChessPosition pos = new ChessPosition(row, col);
            if(checkPosition(pos)){
                ChessMove move = new ChessMove(position, pos, null);
                moves.add(move);
            }
        }
    }

    private boolean checkPosition(ChessPosition pos){
        if(board.getPiece(pos) == null
                || board.getPiece(pos) != null && board.getPiece(pos).getTeamColor()
                != board.getPiece(position).getTeamColor()){
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

