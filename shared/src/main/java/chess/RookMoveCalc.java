package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMoveCalc extends FastMovers implements PieceMoveCalc {

    public RookMoveCalc(ChessBoard board, ChessPosition position) {
        super(board, position);
        this.moves = new ArrayList<>();
    }

    @Override
    public Collection<ChessMove> getMoves() {
        ChessPiece.PieceType type = null;
        getVertical();
        getHorizontal();
        return moves;
    }
}