package chess;
import java.util.Collection;
import java.util.ArrayList;


public class PawnMoveCalc implements PieceMoveCalc{
    private ChessBoard board;
    private ChessPosition position;
    private Collection<ChessMove> moves;
    private ChessPiece.PieceType type;
    private ChessGame.TeamColor color;
    public PawnMoveCalc(ChessBoard board, ChessPosition position){
        this.board = board;
        this.position = position;
        this.moves = new ArrayList<>();
        this.type = null;
        this.color = board.getPiece(position).getTeamColor();
    }
    @Override
    public Collection<ChessMove> getMoves(){
        if(color == ChessGame.TeamColor.WHITE){
            getWhiteMoves();
        }
        else{
            getBlackMoves();
        }

        return moves;
    }
    private boolean checkPosition(ChessPosition pos){
        if(board.getPiece(pos) == null){
            return true;
        }
        return false;
    }
    private boolean isValidMove(int row, int col){
        if (row >= 1 && row < 9 && col >= 1 && col < 9){
            return true;
        }
        return false;
    }
    private boolean canAttack(ChessPosition pos){
        if(board.getPiece(pos) != null
                && board.getPiece(pos).getTeamColor() != board.getPiece(position).getTeamColor()){
           return true;
        }
        return false;
    }
    //talk to TA's about implementation
    private ChessPiece.PieceType getPromotionPiece(){

        return null;
    }

    private void getWhiteMoves() {
        int row = position.getRow();
        int col = position.getColumn();
        int row1 = row + 1;
        int row2 = row + 2;
        //check promotion
        if(row1 == 8){
            type = getPromotionPiece();
        }
        //starting move
        ChessPosition pos1 = new ChessPosition(row1, col);
        ChessPosition pos2 = new ChessPosition(row2, col);
        if (row == 2) {
            if (checkPosition(pos2) && checkPosition(pos1)) {
                ChessMove move = new ChessMove(position, pos2, type);
                moves.add(move);
            }
        }
        if (checkPosition(pos1)) {
            ChessMove move = new ChessMove(position, pos1, type);
            moves.add(move);
        }
        //attack moves
        if(isValidMove(row1, col +1 )) {
            ChessPosition attack_pos = new ChessPosition(row1, col + 1);

            if(canAttack(attack_pos)){
                ChessMove move = new ChessMove(position, attack_pos, type);
                moves.add(move);
            }
        }
        if(isValidMove(row1, col - 1)) {
            ChessPosition attack_pos = new ChessPosition(row1, col - 1);
            if(canAttack(attack_pos)){
                ChessMove move = new ChessMove(position, attack_pos, type);
                moves.add(move);
            }
        }


    }
    private void getBlackMoves(){
        int row = position.getRow();
        int col = position.getColumn();
        int row1 = row - 1;
        int row2 = row - 2;
        //check promotion
        if(row1 == 1){
            type = getPromotionPiece();
        }
        //starting move
        ChessPosition pos1 = new ChessPosition(row1, col);
        ChessPosition pos2 = new ChessPosition(row2, col);
        if (row == 7) {
            if (checkPosition(pos2) && checkPosition(pos1)) {
                ChessMove move = new ChessMove(position, pos2, type);
                moves.add(move);
            }
        }
        if (checkPosition(pos1)) {
            ChessMove move = new ChessMove(position, pos1, type);
            moves.add(move);
        }
        //attack moves
        if(isValidMove(row1, col +1 )) {
            ChessPosition attack_pos = new ChessPosition(row1, col + 1);
            if(canAttack(attack_pos)){
                ChessMove move = new ChessMove(position, attack_pos, type);
                moves.add(move);
            }
        }
        if(isValidMove(row1, col - 1)) {
            ChessPosition attack_pos = new ChessPosition(row1, col - 1);
            if(canAttack(attack_pos)){
                ChessMove move = new ChessMove(position, attack_pos, type);
                moves.add(move);
            }
        }
    }
}
