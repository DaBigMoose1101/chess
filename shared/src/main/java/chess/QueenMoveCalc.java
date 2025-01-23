package chess;
import java.util.Collection;
import java.util.ArrayList;

public class QueenMoveCalc implements PieceMoveCalc {
    private ChessBoard board;
    private ChessPosition position;
    private Collection<ChessMove> moves;
    public QueenMoveCalc(ChessBoard board, ChessPosition position){
        this.board = board;
        this.position = position;
        this.moves = new ArrayList<>();
    }
    @Override
    public Collection<ChessMove> getMoves() {
        ChessPiece.PieceType type = null;
        getVertical();
        getHorizontal();
        getDiagonal1();
        getDiagonal2();
        getDiagonal3();
        getDiagonal4();
        return moves;
    }
    private boolean checkPosition(ChessPosition pos){
        if(board.getPiece(pos) == null){
            return true;
        }
        return false;
    }
    private void getVertical(){
        for(int i = position.getRow()+1; i < 8; i++){
            ChessPosition pos = new ChessPosition(i, position.getColumn());
            if(checkPosition(pos)){
                ChessMove move = new ChessMove(position, pos, null);
                moves.add(move);
            }
            else if ( board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() != board.getPiece(position).getTeamColor()){
                ChessMove move = new ChessMove(position, pos, null);
                moves.add(move);
                break;
            }
            else{
                break;
            }
        }
        for(int i = position.getRow()-1; i >= 0; i--){
            ChessPosition pos = new ChessPosition(i, position.getColumn());
            if(checkPosition(pos)){
                ChessMove move = new ChessMove(position, pos, null);
                moves.add(move);
            }
            else if ( board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() != board.getPiece(position).getTeamColor()){
                ChessMove move = new ChessMove(position, pos, null);
                moves.add(move);
                break;
            }
            else{
                break;
            }
        }
    }
    private void getHorizontal(){
        for(int i = position.getColumn()+1; i < 8; i++){
            ChessPosition pos = new ChessPosition(position.getRow(), i);
            if(checkPosition(pos)){
                ChessMove move = new ChessMove(position, pos, null);
                moves.add(move);
            }
            else if ( board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() != board.getPiece(position).getTeamColor()){
                ChessMove move = new ChessMove(position, pos, null);
                moves.add(move);
                break;
            }
            else{
                break;
            }
        }
        for(int i = position.getColumn()-1; i >= 0; i--){
            ChessPosition pos = new ChessPosition(position.getRow(), i);
            if(checkPosition(pos)){
                ChessMove move = new ChessMove(position, pos, null);
                moves.add(move);
            }
            else if ( board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() != board.getPiece(position).getTeamColor()){
                ChessMove move = new ChessMove(position, pos, null);
                moves.add(move);
                break;
            }
            else{
                break;
            }
        }
    }
    private void getDiagonal1(){
        int row = position.getRow()+1;
        int col = position.getColumn()+1;
        while(row < 8 && col < 8){
            ChessPosition pos = new ChessPosition(row, col);
            if(checkPosition(pos)){
                ChessMove move = new ChessMove(position, pos, null);
                moves.add(move);
                row++;
                col++;
            }
            else if ( board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() != board.getPiece(position).getTeamColor()){
                ChessMove move = new ChessMove(position, pos, null);
                moves.add(move);
                break;
            }
            else {
                break;
            }

        }
    }
    private void getDiagonal2() {
        int row = position.getRow()+1;
        int col = position.getColumn()-1;
        while (row < 8 && col >= 0) {
            ChessPosition pos = new ChessPosition(row, col);
            if (checkPosition(pos)) {
                ChessMove move = new ChessMove(position, pos, null);
                moves.add(move);
                row++;
                col--;
            } else if (board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                ChessMove move = new ChessMove(position, pos, null);
                moves.add(move);
                break;
            } else {
                break;
            }
        }
    }
    private void getDiagonal3() {
        int row = position.getRow()-1;
        int col = position.getColumn()-1;
        while (row >= 0 && col >= 0) {
            ChessPosition pos = new ChessPosition(row, col);
            if (checkPosition(pos)) {
                ChessMove move = new ChessMove(position, pos, null);
                moves.add(move);
                row--;
                col--;
            } else if (board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                ChessMove move = new ChessMove(position, pos, null);
                moves.add(move);
                break;
            } else {
                break;
            }
        }
    }
    private void getDiagonal4() {
        int row = position.getRow()-1;
        int col = position.getColumn()+1;
        while (row < 8 && col < 8) {
            ChessPosition pos = new ChessPosition(row, col);
            if (checkPosition(pos)) {
                ChessMove move = new ChessMove(position, pos, null);
                moves.add(move);
                row--;
                col++;
            } else if (board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                ChessMove move = new ChessMove(position, pos, null);
                moves.add(move);
                break;
            } else {
                break;
            }
        }
    }

}

