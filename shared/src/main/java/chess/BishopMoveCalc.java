package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMoveCalc extends FastMovers implements PieceMoveCalc {


    public BishopMoveCalc(ChessBoard board, ChessPosition pos){
        super(board, pos);
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
}
