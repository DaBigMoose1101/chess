package chess;
import java.util.Collection;
import java.util.ArrayList;
public class PawnMoveCalc implements PieceMoveCalc{
    private ChessBoard board;
    private ChessPosition position;
    public PawnMoveCalc(ChessBoard board, ChessPosition position){
        this.board = board;
        this.position = position;
    }
    @Override
    public Collection<ChessMove> getMoves(){
    ArrayList<ChessMove> moves = new ArrayList<>();

    return moves;
}
}
