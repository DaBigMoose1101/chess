package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMoveCalc implements PieceMoveCalc {
    private ChessBoard board;
    private ChessPosition position;
    private Collection<ChessMove> moves;

    public RookMoveCalc(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
        this.moves = new ArrayList<>();
    }

    @Override
    public Collection<ChessMove> getMoves() {
        ChessPiece.PieceType type = null;
        getVertical();
        getHorizontal();
        return moves;
    }

    private boolean checkPosition(ChessPosition pos) {
        if (board.getPiece(pos) == null) {
            return true;
        }
        return false;
    }

    private void getVertical() {
        for (int i = position.getRow() + 1; i < 8; i++) {
            ChessPosition pos = new ChessPosition(i, position.getColumn());
            if (checkPosition(pos)) {
                ChessMove move = new ChessMove(position, pos, null);
                moves.add(move);
            } else if (board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                ChessMove move = new ChessMove(position, pos, null);
                moves.add(move);
                break;
            } else {
                break;
            }
        }
        for (int i = position.getRow() - 1; i >= 0; i--) {
            ChessPosition pos = new ChessPosition(i, position.getColumn());
            if (checkPosition(pos)) {
                ChessMove move = new ChessMove(position, pos, null);
                moves.add(move);
            } else if (board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                ChessMove move = new ChessMove(position, pos, null);
                moves.add(move);
                break;
            } else {
                break;
            }
        }
    }

    private void getHorizontal() {
        for (int i = position.getColumn() + 1; i < 8; i++) {
            ChessPosition pos = new ChessPosition(position.getRow(), i);
            if (checkPosition(pos)) {
                ChessMove move = new ChessMove(position, pos, null);
                moves.add(move);
            } else if (board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                ChessMove move = new ChessMove(position, pos, null);
                moves.add(move);
                break;
            } else {
                break;
            }
        }
        for (int i = position.getColumn() - 1; i >= 0; i--) {
            ChessPosition pos = new ChessPosition(position.getRow(), i);
            if (checkPosition(pos)) {
                ChessMove move = new ChessMove(position, pos, null);
                moves.add(move);
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