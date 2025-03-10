package dataaccess;

import dataaccess.DatabaseManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class ChessDatabase {
    public ChessDatabase() throws Exception {
        try (var conn = DatabaseManager.getConnection()) {
            DatabaseManager.createDatabase();
            conn.setCatalog("chess");
            var userTableStatement = """
                    CREATE TABLE IF NOT EXISTS user(
                    user_id INT NOT NULL AUTO_INCREMENT
                    username VARCHAR(255) NOT NULL UNIQUE,
                    password_hash VARCHAR(500) NOT NULL UNIQUE,
                    email VARCHAR(100) NOT NULL UNIQUE,
                    PRIMARY KEY (user_id)
                    )""";
            executeStatement(conn.prepareStatement(userTableStatement));
            var authTableStatement = """
                    CREATE TABLE IF NOT EXISTS auth(
                    auth_id INT NOT NULL AUTO_INCREMENT
                    token VARCHAR(500) NOT NULL UNIQUE,
                    user VARCHAR(255) NOT NULL UNIQUE,
                    PRIMARY KEY (auth_id)
                    )""";
            executeStatement(conn.prepareStatement(authTableStatement));
            var gameTableStatement = """
                    CREATE TABLE IF NOT EXISTS game(
                    game_id INT NOT NULL AUTO_INCREMENT
                    white_user VARCHAR(255) NOT NULL UNIQUE,
                    black_user VARCHAR(255) NOT NULL UNIQUE,
                    game_name VARCHAR(255) NOT NULL UNIQUE,
                    game TEXT NOT NULL,
                    PRIMARY KEY (game_id)
                    )""";
            executeStatement(conn.prepareStatement(gameTableStatement));
        }
    }

    private static void executeStatement(PreparedStatement statement) throws SQLException {
        statement.executeUpdate();
    }

}