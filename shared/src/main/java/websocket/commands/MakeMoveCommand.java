package websocket.commands;

import chess.ChessGame;
import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand{
    private final ChessMove move;

    public MakeMoveCommand(String authToken, int gameID, ChessMove move){
        super(CommandType.MAKE_MOVE, authToken, gameID);
        this.move = move;
    }

    public ChessMove getMove(){
        return move;
    }
}
