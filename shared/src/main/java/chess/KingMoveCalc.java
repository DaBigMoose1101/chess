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
        if(isValidMove(move1, move4)){
            ChessPosition pos = new ChessPosition(move1, move4);
            if (checkPosition(pos)){
                ChessMove move = new ChessMove(position, pos, type);
                moves.add(move);
            }
        }
        if(isValidMove(move1, col)){
            ChessPosition pos = new ChessPosition(move1, col);
            if (checkPosition(pos)){
                ChessMove move = new ChessMove(position, pos, type);
                moves.add(move);
            }
        }
        if(isValidMove(move1, move3)){
            ChessPosition pos = new ChessPosition(move1, move3);
            if (checkPosition(pos)){
                ChessMove move = new ChessMove(position, pos, type);
                moves.add(move);
            }
        }
        if(isValidMove(row, move4)){
            ChessPosition pos = new ChessPosition(row, move4);
            if (checkPosition(pos)){
                ChessMove move = new ChessMove(position, pos, type);
                moves.add(move);
            }
        }
        if(isValidMove(row, move3)){
            ChessPosition pos = new ChessPosition(row, move3);
            if (checkPosition(pos)){
                ChessMove move = new ChessMove(position, pos, type);
                moves.add(move);
            }
        }
        if(isValidMove(move2, move4)){
            ChessPosition pos = new ChessPosition(move2, move4);
            if (checkPosition(pos)){
                ChessMove move = new ChessMove(position, pos, type);
                moves.add(move);
            }
        }
        if(isValidMove(move2, move3)){
            ChessPosition pos = new ChessPosition(move2, move3);
            if (checkPosition(pos)){
                ChessMove move = new ChessMove(position, pos, type);
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
