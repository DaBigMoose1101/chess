package chess;
import java.util.Collection;
import java.util.ArrayList;


public class PawnMoveCalc implements PieceMoveCalc{
    private final ChessBoard board;
    private final ChessPosition position;
    private Collection<ChessMove> moves;
    private final ArrayList<ChessPiece.PieceType> types = new ArrayList<>();
    private final ChessGame.TeamColor color;
    public PawnMoveCalc(ChessBoard board, ChessPosition position){
        this.board = board;
        this.position = position;
        this.moves = new ArrayList<>();
        types.add(ChessPiece.PieceType.BISHOP);
        types.add(ChessPiece.PieceType.KNIGHT);
        types.add(ChessPiece.PieceType.QUEEN);
        types.add(ChessPiece.PieceType.ROOK);
        this.color = board.getPiece(position).getTeamColor();
    }
    @Override
    public Collection<ChessMove> getMoves(){
        int row1;
        int row2;
        if(color == ChessGame.TeamColor.WHITE){
            row1 = position.getRow() + 1;
            row2 = position.getRow() + 2;
            calcMovesWhite(row1, row2);
        }
        else{
            row1 = position.getRow() - 1;
            row2 = position.getRow() - 2;
            calcMovesBlack(row1, row2);
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

    private void moveForward(ChessPosition pos, int row){
        if(row == 8 || row == 1){
            promotionMoves(pos);

        }
        else{
            ChessMove move = new ChessMove(position, pos, null);
            moves.add(move);
        }
    }

    private void promotionMoves(ChessPosition pos){
        for(ChessPiece.PieceType p : types){
            ChessMove move = new ChessMove(position, pos, p);
            moves.add(move);
        }
    }

    private void Attack(ChessPosition pos, int row){
        if(row == 8 || row == 1){
            promotionMoves(pos);
        }
        else{
            ChessMove move = new ChessMove(position, pos, null);
            moves.add(move);
        }
    }
    private void calcMovesWhite(int row1, int row2) {
        //first move option
        if (position.getRow() == 2) {
            ChessPosition pos1 = new ChessPosition(row1, position.getColumn());
            ChessPosition pos2 = new ChessPosition(row2, position.getColumn());
            if (checkPosition(pos1) && checkPosition(pos2)) {
                ChessMove move = new ChessMove(position, pos2, null);
                moves.add(move);
            }
        }
        //regular moves
        for(int i = position.getColumn()-1; i <= position.getColumn() + 1; i++){
            if(isValidMove(row1, i)){
                ChessPosition pos = new ChessPosition(row1, i);
                //attack moves
                if((i == position.getColumn()-1 || i == position.getColumn()+1) && canAttack(pos)){
                    Attack(pos, row1);
                }
                //forward move
                else if(checkPosition(pos) && i == position.getColumn()){
                    moveForward(pos, row1);
                }
            }
        }

    }
    private void calcMovesBlack(int row1, int row2) {
        //first move option
        if (position.getRow() == 7) {
            ChessPosition pos1 = new ChessPosition(row1, position.getColumn());
            ChessPosition pos2 = new ChessPosition(row2, position.getColumn());
            if (checkPosition(pos1) && checkPosition(pos2)) {
                ChessMove move = new ChessMove(position, pos2, null);
                moves.add(move);
            }

        }
        //regular moves
        for(int i = position.getColumn()-1; i <= position.getColumn() + 1; i++){
            if(isValidMove(row1, i)){
                ChessPosition pos = new ChessPosition(row1, i);
                //attack moves
                if((i == position.getColumn()-1 || i == position.getColumn()+1) && canAttack(pos)){
                    Attack(pos, row1);
                }
                //forward move
                else if(checkPosition(pos) && i == position.getColumn()){
                    moveForward(pos, row1);
                }
            }
        }

    }
}
