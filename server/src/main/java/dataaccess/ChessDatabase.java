package dataaccess;

import dataaccess.DatabaseManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class ChessDatabase {
    public ChessDatabase() throws Exception {
        try (var conn = DatabaseManager.getConnection()) {
            DatabaseManager.createDatabase();
            conn.setCatalog("chess");
            var authTableStatement = """
                    CREATE TABLE IF NOT EXISTS auth(
                    auth_id INT NOT NULL AUTO_INCREMENT
                    token VARCHAR(500) NOT NULL UNIQUE,
                    user VARCHAR(255) NOT NULL UNIQUE,
                    PRIMARY KEY (auth_id)
                    )""";
            executeStatement(conn.prepareStatement(authTableStatement));

        }
    }

    private static void executeStatement(PreparedStatement statement) throws SQLException {
        statement.executeUpdate();
    }

}