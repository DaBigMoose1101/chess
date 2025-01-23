package chess;
import java.util.Collection;
import java.util.ArrayList;


public class KnightMoveCalc implements PieceMoveCalc{
    private ChessBoard board;
    private ChessPosition position;
    private Collection<ChessMove> moves = new ArrayList<>();
    public KnightMoveCalc(ChessBoard Board, ChessPosition pos){
        this.board = Board;
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
        if(isValidMove(row1, col1)){
            ChessPosition pos = new ChessPosition(row1, col1);
            if(checkPosition(pos)){
                ChessMove move = new ChessMove(position, pos, null);
                moves.add(move);
            }
        }
        if(isValidMove(row1, col3)){
            ChessPosition pos = new ChessPosition(row1, col3);
            if(checkPosition(pos)){
                ChessMove move = new ChessMove(position, pos, null);
                moves.add(move);
            }
        }
        if(isValidMove(row2, col2)){
            ChessPosition pos = new ChessPosition(row2, col2);
            if(checkPosition(pos)){
                ChessMove move = new ChessMove(position, pos, null);
                moves.add(move);
            }
        }
        if(isValidMove(row2, col4)){
            ChessPosition pos = new ChessPosition(row2, col4);
            if(checkPosition(pos)){
                ChessMove move = new ChessMove(position, pos, null);
                moves.add(move);
            }
        }
        if(isValidMove(row3, col1)){
            ChessPosition pos = new ChessPosition(row3, col1);
            if(checkPosition(pos)){
                ChessMove move = new ChessMove(position, pos, null);
                moves.add(move);
            }
        }
        if(isValidMove(row3, col3)){
            ChessPosition pos = new ChessPosition(row3, col3);
            if(checkPosition(pos)){
                ChessMove move = new ChessMove(position, pos, null);
                moves.add(move);
            }
        }
        if(isValidMove(row4, col2)){
            ChessPosition pos = new ChessPosition(row4, col2);
            if(checkPosition(pos)){
                ChessMove move = new ChessMove(position, pos, null);
                moves.add(move);
            }
        }
        if(isValidMove(row4, col4)){
            ChessPosition pos = new ChessPosition(row4, col4);
            if(checkPosition(pos)){
                ChessMove move = new ChessMove(position, pos, null);
                moves.add(move);
            }
        }
        return moves;
    }

    private boolean checkPosition(ChessPosition pos){
        if(board.getPiece(pos) == null || board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() != board.getPiece(position).getTeamColor()){
            return true;
        }
        return false;
    }
    private boolean isValidMove(int row, int col){
        if (row >= 0 && row < 8 && col >= 0 && col < 8){
            return true;
        }
        return false;
    }
}
