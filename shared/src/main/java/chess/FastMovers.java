package chess;

import java.util.ArrayList;
import java.util.Collection;

public class FastMovers {
    protected ChessBoard board;
    protected ChessPosition position;
    protected Collection<ChessMove> moves;

    public FastMovers(ChessBoard board, ChessPosition position){
        this.board = board;
        this.position = position;
        this.moves = new ArrayList<>();
    }

    protected boolean checkPosition(ChessPosition pos){
        if(board.getPiece(pos) == null){
            return true;
        }
        return false;
    }
    protected void getVertical(){
        for(int i = position.getRow()+1; i < 9; i++){
            ChessPosition pos = new ChessPosition(i, position.getColumn());
            if(!validateMove(pos)){
                break;
            }
        }
        for(int i = position.getRow()-1; i >= 1; i--){
            ChessPosition pos = new ChessPosition(i, position.getColumn());
            if(!validateMove(pos)){
                break;
            }
        }
    }
    protected void getHorizontal(){
        for(int i = position.getColumn()+1; i < 9; i++){
            ChessPosition pos = new ChessPosition(position.getRow(), i);
            if(!validateMove(pos)){
                break;
            }
        }
        for(int i = position.getColumn()-1; i >= 1; i--){
            ChessPosition pos = new ChessPosition(position.getRow(), i);
            if(!validateMove(pos)){
                break;
            }
        }
    }
    protected void getDiagonal1(){
        int row = position.getRow()+1;
        int col = position.getColumn()+1;
        while(row < 9 && col < 9){
            ChessPosition pos = new ChessPosition(row, col);
            if(!validateMove(pos)){
                break;
            }
            else {
                row++;
                col++;
            }
        }
    }
    protected void getDiagonal2() {
        int row = position.getRow()+1;
        int col = position.getColumn()-1;
        while (row < 9 && col >= 1) {
            ChessPosition pos = new ChessPosition(row, col);
            if(!validateMove(pos)){
                break;
            }
            else {
                row++;
                col--;
            }
        }
    }
    protected void getDiagonal3() {
        int row = position.getRow()-1;
        int col = position.getColumn()-1;
        while (row >= 1 && col >= 1) {
            ChessPosition pos = new ChessPosition(row, col);
            if(!validateMove(pos)){
                break;
            }
            else {
                row--;
                col--;
            }
        }
    }
    protected void getDiagonal4() {
        int row = position.getRow()-1;
        int col = position.getColumn()+1;
        while (row >=1  && col < 9) {
            ChessPosition pos = new ChessPosition(row, col);
            if(!validateMove(pos)){
                break;
            }
            else {
                row--;
                col++;
            }
        }
    }

    protected Boolean validateMove(ChessPosition pos){
        if(checkPosition(pos)){
            ChessMove move = new ChessMove(position, pos, null);
            moves.add(move);
            return true;
        }
        else if ( board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() != board.getPiece(position).getTeamColor()){
            ChessMove move = new ChessMove(position, pos, null);
            moves.add(move);
            return false;
        }
        else{
            return false;
        }
    }
}
