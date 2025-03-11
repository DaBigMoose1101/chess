package dataaccess;




public class ChessDatabase {
    public ChessDatabase() throws Exception {
        try (var conn = DatabaseManager.getConnection()) {
            DatabaseManager.createDatabase();
        }
    }

}