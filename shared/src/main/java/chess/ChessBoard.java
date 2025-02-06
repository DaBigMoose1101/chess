package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard implements Cloneable {
    private ChessPiece[][] chessBoard = new ChessPiece[8][8];
    public ChessBoard() {
        clearBoard();
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        int row_index = 8-position.getRow();
        int column_index = position.getColumn()-1;
        this.chessBoard[row_index][column_index] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return this.chessBoard[8-position.getRow()][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        clearBoard();
        //Back Rows Reset
        for(int i = 0; i < 8; i++){
            if (i == 0 || i == 7){
                ChessPiece r = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
                ChessPiece R = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
                ChessPosition pos1 = new ChessPosition(8, i+1);
                ChessPosition pos2 = new ChessPosition(1, i+1);
                addPiece(pos1, r);
                addPiece(pos2, R);
            }
            if (i == 1 || i == 6){
                ChessPiece n = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
                ChessPiece N = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
                ChessPosition pos1 = new ChessPosition(8, i+1);
                ChessPosition pos2 = new ChessPosition(1, i+1);
                addPiece(pos1, n);
                addPiece(pos2, N);
            }
            if (i == 2 || i == 5){
                ChessPiece b = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
                ChessPiece B = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
                ChessPosition pos1 = new ChessPosition(8, i+1);
                ChessPosition pos2 = new ChessPosition(1, i+1);
                addPiece(pos1, b);
                addPiece(pos2, B);
            }
            if (i == 3){
                ChessPiece q = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
                ChessPiece Q = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
                ChessPosition pos1 = new ChessPosition(8, i+1);
                ChessPosition pos2 = new ChessPosition(1, i+1);
                addPiece(pos1, q);
                addPiece(pos2, Q);
            }
            if (i == 4){
                ChessPiece k = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
                ChessPiece K = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
                ChessPosition pos1 = new ChessPosition(8, i+1);
                ChessPosition pos2 = new ChessPosition(1, i+1);
                addPiece(pos1, k);
                addPiece(pos2, K);
            }
        }
        //pawns rest
        for(int i = 0; i < 8; i++){
            ChessPiece p = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            ChessPiece P = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            ChessPosition pos1 = new ChessPosition(7, i+1);
            ChessPosition pos2 = new ChessPosition(2, i+1);
            addPiece(pos1, p);
            addPiece(pos2, P);
        }
    }
    private void clearBoard(){
        for (var i = 0; i < this.chessBoard.length; i++){
            for (var j = 0; j < this.chessBoard[0].length; j++){
                this.chessBoard[i][j] = null;
            }
        }

    }

    public void makeMove(ChessMove move){
        ChessPosition startPos = move.getStartPosition();
        ChessPosition endPos = move.getEndPosition();
        ChessPiece p = getPiece(startPos);
        ChessGame.TeamColor color = p.getTeamColor();
        if(move.getPromotionPiece() == null){
            ChessPiece clone = p.clone();
            addPiece(endPos, clone);
        }
        else{
            ChessPiece np = new ChessPiece(color, move.getPromotionPiece());
            addPiece(endPos, np);
        }
        addPiece(startPos, null);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(chessBoard, that.chessBoard);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(chessBoard);
    }
    @Override
    public ChessBoard clone(){
        try {
            ChessBoard clone = (ChessBoard) super.clone();
            ChessPiece[][] clonedPieces = new ChessPiece[8][8];
            for(int i = 0; i < 8; i++){
                for(int j = 0; j < 8; j++) {
                    if(chessBoard[i][j] == null){
                        clonedPieces[i][j] = null;
                    }
                    else{
                        ChessPiece piece = chessBoard[i][j];
                        clonedPieces[i][j] = piece.clone();
                    }
                }
            }
            clone.chessBoard = clonedPieces;
            return clone;
        }
        catch (CloneNotSupportedException e){
            throw new RuntimeException(e);
        }
    }
}
