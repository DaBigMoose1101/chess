package records;

import java.util.Vector;
import model.GameData;

public record GamesListResponse(Vector<GameData> games) {
}
