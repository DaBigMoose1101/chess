package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor Color;
    private ChessPiece.PieceType Type;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.Color = pieceColor;
        this.Type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {

        return Color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {

        return Type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves;
        switch(Type) {
            case KING:
                KingMoveCalc king = new KingMoveCalc(board, myPosition);
                moves = king.getMoves();
                break;
            case QUEEN:
                QueenMoveCalc queen = new QueenMoveCalc(board, myPosition);
                moves = queen.getMoves();
                break;
            case BISHOP:
                BishopMoveCalc bishop = new BishopMoveCalc(board, myPosition);
                moves = bishop.getMoves();
                break;
            case KNIGHT:
                KnightMoveCalc knight = new KnightMoveCalc(board, myPosition);
                moves = knight.getMoves();
                break;
            case ROOK:
                RookMoveCalc rook = new RookMoveCalc(board, myPosition);
                moves = rook.getMoves();
                break;
            default:
                throw new IllegalArgumentException("Invalid piece type: " + Type);

        }
        return moves;
    }

}
