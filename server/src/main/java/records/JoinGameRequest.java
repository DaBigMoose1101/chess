package records;
import chess.ChessGame.TeamColor;

public record JoinGameRequest(TeamColor playerColor, int gameID) {
}
