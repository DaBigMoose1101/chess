package chess;
import java.util.Collection;
import java.util.ArrayList;

public class QueenMoveCalc extends FastMovers implements PieceMoveCalc {

    public QueenMoveCalc(ChessBoard board, ChessPosition position){
        super(board,position);
    }
    @Override
    public Collection<ChessMove> getMoves() {
        this.moves = new ArrayList<>();
        ChessPiece.PieceType type = null;
        getVertical();
        getHorizontal();
        getDiagonal1();
        getDiagonal2();
        getDiagonal3();
        getDiagonal4();
        return moves;
    }

}

