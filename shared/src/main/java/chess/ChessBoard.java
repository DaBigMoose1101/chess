package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] chessBoard = new ChessPiece[8][8];
    public ChessBoard() {
        clearBoard();
        resetBoard();
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        this.chessBoard[position.getRow()][position.getColumn()] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return this.chessBoard[position.getRow()][position.getColumn()];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        clearBoard();
        //Initialize back row pieces
        ChessPiece b_k = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        ChessPiece w_k = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        ChessPiece b_q = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        ChessPiece w_q = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        ChessPiece b_b1 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        ChessPiece w_b1 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        ChessPiece b_n1 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        ChessPiece w_n1 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        ChessPiece b_r1 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        ChessPiece w_r1 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        ChessPiece b_b2 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        ChessPiece w_b2 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        ChessPiece b_n2 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        ChessPiece w_n2 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        ChessPiece b_r2 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        ChessPiece w_r2 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        //Initialize pawns
        ChessPiece b_p1 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPiece b_p2 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPiece b_p3 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPiece b_p4 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPiece b_p5 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPiece b_p6 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPiece b_p7 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPiece b_p8 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPiece w_p1 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece w_p2 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece w_p3 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece w_p4 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece w_p5 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece w_p6 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece w_p7 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece w_p8 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        //back rows set up
        this.chessBoard[0][0] = b_r1;
        this.chessBoard[7][0] = w_r1;
        this.chessBoard[0][1] = b_n1;
        this.chessBoard[7][1] = w_n1;
        this.chessBoard[0][2] = b_b1;
        this.chessBoard[7][2] = w_b1;
        this.chessBoard[0][3] = b_q;
        this.chessBoard[7][3] = w_q;
        this.chessBoard[0][4] = b_k;
        this.chessBoard[7][4] = w_k;
        this.chessBoard[0][5] = b_b2;
        this.chessBoard[7][5] = w_b2;
        this.chessBoard[0][6] = b_n2;
        this.chessBoard[7][6] = w_n2;
        this.chessBoard[0][7] = b_r2;
        this.chessBoard[7][7] = w_r2;
        //pawns rest
        this.chessBoard[1][0] = b_p1;
        this.chessBoard[6][0] = w_p1;
        this.chessBoard[1][1] = b_p2;
        this.chessBoard[6][1] = w_p2;
        this.chessBoard[1][2] = b_p3;
        this.chessBoard[6][2] = w_p3;
        this.chessBoard[1][3] = b_p4;
        this.chessBoard[6][3] = w_p4;
        this.chessBoard[1][4] = b_p5;
        this.chessBoard[6][4] = w_p5;
        this.chessBoard[1][5] = b_p6;
        this.chessBoard[6][5] = w_p6;
        this.chessBoard[1][6] = b_p7;
        this.chessBoard[6][6] = w_p7;
        this.chessBoard[1][7] = b_p8;
        this.chessBoard[6][7] = w_p8;
    }
    private void clearBoard(){
        for (var i = 0; i < this.chessBoard.length; i++){
            for (var j = 0; j < this.chessBoard[0].length; j++){
                this.chessBoard[i][j] = null;
            }
        }

    }
}
