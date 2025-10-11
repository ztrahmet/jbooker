package io.github.ztrahmet.jbooker.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final String DATABASE_URL = "jdbc:sqlite:jbooker.db";

    public static void initializeDatabase() {
        String createRoomsTableSql = "CREATE TABLE IF NOT EXISTS rooms ("
                + "number TEXT PRIMARY KEY,"
                + "type TEXT NOT NULL,"
                + "price REAL NOT NULL"
                + ");";

        String createBookingsTableSql = "CREATE TABLE IF NOT EXISTS bookings ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "room_number TEXT NOT NULL,"
                + "guest_name TEXT NOT NULL,"
                + "check_in_date TEXT NOT NULL,"
                + "check_out_date TEXT NOT NULL,"
                + "FOREIGN KEY (room_number) REFERENCES rooms (number)"
                + ");";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = conn.createStatement()) {
            System.out.println("Connecting to database and setting up tables...");
            stmt.execute(createRoomsTableSql);
            stmt.execute(createBookingsTableSql);
            System.out.println("Database tables are ready.");
            seedInitialData(conn);
        } catch (SQLException e) {
            System.err.println("Database initialization error: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void seedInitialData(Connection conn) throws SQLException {
        String countSql = "SELECT COUNT(*) FROM rooms";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(countSql)) {
            if (rs.next() && rs.getInt(1) == 0) {
                System.out.println("No rooms found. Seeding initial room data...");
                String insertSql = "INSERT INTO rooms(number, type, price) VALUES(?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                    pstmt.setString(1, "101");
                    pstmt.setString(2, "Single");
                    pstmt.setDouble(3, 85.50);
                    pstmt.addBatch();

                    pstmt.setString(1, "102");
                    pstmt.setString(2, "Single");
                    pstmt.setDouble(3, 85.50);
                    pstmt.addBatch();

                    pstmt.setString(1, "201");
                    pstmt.setString(2, "Double");
                    pstmt.setDouble(3, 120.00);
                    pstmt.addBatch();

                    pstmt.setString(1, "202");
                    pstmt.setString(2, "Double");
                    pstmt.setDouble(3, 125.00);
                    pstmt.addBatch();

                    pstmt.setString(1, "301");
                    pstmt.setString(2, "Suite");
                    pstmt.setDouble(3, 210.75);
                    pstmt.addBatch();

                    pstmt.executeBatch();
                    System.out.println("Sample rooms have been added to the database.");
                }
            } else {
                System.out.println("Database already contains room data.");
            }
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }
}
