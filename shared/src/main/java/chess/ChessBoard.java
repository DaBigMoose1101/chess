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
        int rowIndex = 8-position.getRow();
        int columnIndex = position.getColumn()-1;
        this.chessBoard[rowIndex][columnIndex] = piece;
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
                ChessPiece rb = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
                ChessPiece rw = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
                ChessPosition pos1 = new ChessPosition(8, i+1);
                ChessPosition pos2 = new ChessPosition(1, i+1);
                addPiece(pos1, rb);
                addPiece(pos2, rw);
            }
            if (i == 1 || i == 6){
                ChessPiece nb = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
                ChessPiece nw = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
                ChessPosition pos1 = new ChessPosition(8, i+1);
                ChessPosition pos2 = new ChessPosition(1, i+1);
                addPiece(pos1, nb);
                addPiece(pos2, nw);
            }
            if (i == 2 || i == 5){
                ChessPiece bb = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
                ChessPiece bw = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
                ChessPosition pos1 = new ChessPosition(8, i+1);
                ChessPosition pos2 = new ChessPosition(1, i+1);
                addPiece(pos1, bb);
                addPiece(pos2, bw);
            }
            if (i == 3){
                ChessPiece qb = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
                ChessPiece qw = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
                ChessPosition pos1 = new ChessPosition(8, i+1);
                ChessPosition pos2 = new ChessPosition(1, i+1);
                addPiece(pos1, qb);
                addPiece(pos2, qw);
            }
            if (i == 4){
                ChessPiece kb = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
                ChessPiece kw = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
                ChessPosition pos1 = new ChessPosition(8, i+1);
                ChessPosition pos2 = new ChessPosition(1, i+1);
                addPiece(pos1, kb);
                addPiece(pos2, kw);
            }
        }
        //pawns rest
        for(int i = 0; i < 8; i++){
            ChessPiece pb = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            ChessPiece pw = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            ChessPosition pos1 = new ChessPosition(7, i+1);
            ChessPosition pos2 = new ChessPosition(2, i+1);
            addPiece(pos1, pb);
            addPiece(pos2, pw);
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
