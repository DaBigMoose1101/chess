package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    TeamColor turn;
    ChessBoard board;
    Boolean gameOver;
    public ChessGame() {
        this.board = new ChessBoard();
        this.turn = TeamColor.WHITE;
        this.gameOver = false;
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.turn = team;
    }

    private void changeTurn(){
        if(turn == TeamColor.WHITE){
            setTeamTurn(TeamColor.BLACK);
        }
        else{
            setTeamTurn(TeamColor.WHITE);
        }
    }
    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> moves;
        Collection<ChessMove>result = new ArrayList<>();
        ChessBoard original = board;
        if(board.getPiece(startPosition) == null){
            return null;
        }
        ChessPiece piece = board.getPiece(startPosition);
        TeamColor color = piece.getTeamColor();
        moves = piece.pieceMoves(board, startPosition);
        for(ChessMove move : moves){
            ChessBoard clone = board.clone();
            clone.makeMove(move);
            setBoard(clone);
            if(!isInCheck(color)){
                result.add(move);
            }
            setBoard(original);
        }

        return result;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        Collection<ChessMove> valid = validMoves(move.getStartPosition());
        if(board.getPiece(move.getStartPosition()) != null && valid.contains(move)
                && turn == board.getPiece(move.getStartPosition()).getTeamColor() && !gameOver) {
                board.makeMove(move);
                changeTurn();
        }
        else{
            throw new InvalidMoveException();
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        boolean check = false;
        ChessPiece piece;
        ChessPosition pos;
        for(int i = 1; i < 9; i++){
            for(int j = 1; j < 9; j++){
                pos = new ChessPosition(i, j);
                piece = board.getPiece(pos);
                if(piece != null && piece.getTeamColor() != teamColor){
                    Collection<ChessMove> moves = piece.pieceMoves(board, pos);
                    check = isKing(moves, teamColor);
                    if(check){
                        return check;
                    }
                }

            }
        }
        return check;
    }

    private boolean isKing(Collection<ChessMove> moves, TeamColor color){
        boolean foundKing = false;
        for(ChessMove move: moves){
            ChessPiece piece = board.getPiece(move.getEndPosition());
            if(piece!=null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == color){
                foundKing = true;
                break;
            }
        }
        return foundKing;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {

        ChessPosition pos;
        if(isInCheck(teamColor)){
            for(int i = 1; i < 9; i++){
                for(int j = 1; j < 9 ; j++){
                    pos = new ChessPosition(i, j);
                    if(board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() == teamColor
                            && !validMoves(pos).isEmpty()){
                        return false;
                    }
                }
            }
            gameOver = true;
            return true;
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {

        ChessPosition pos;
        if(!isInCheck(teamColor)){
            for(int i = 1; i < 9; i++){
                for(int j = 1; j < 9 ; j++){
                    pos = new ChessPosition(i, j);
                    if(board.getPiece(pos) != null && !validMoves(pos).isEmpty()
                            && board.getPiece(pos).getTeamColor() == teamColor){
                        return false;
                    }
                }
            }
            gameOver = true;
            return true;
        }
        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    public boolean isGameOver(){
        return gameOver;
    }
    public void resign(){
        gameOver = true;
    }

}
