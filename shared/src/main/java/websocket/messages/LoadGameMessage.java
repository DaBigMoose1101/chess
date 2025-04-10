package websocket.messages;

import chess.ChessGame;
import model.GameData;

public class LoadGameMessage extends ServerMessage{
    private final GameData game;
    private final int gameId;

    public LoadGameMessage(GameData game, int gameId){
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
        this.gameId = gameId;
    }

    public GameData getGame(){
        return game;
    }

    public int getGameId(){
        return gameId;
    }
}
