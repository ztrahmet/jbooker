package io.github.ztrahmet.jbooker.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Manages all interactions with the SQLite database.
 * This class handles the database connection and initial schema setup.
 */
public class DatabaseManager {

    // The name of the database file. It will be created in the project's root directory.
    private static final String DATABASE_URL = "jdbc:sqlite:jbooker.db";

    /**
     * Initializes the database.
     * If the database file or tables do not exist, they will be created.
     */
    public static void initializeDatabase() {
        // SQL statement for creating the rooms table
        String createRoomsTableSql = "CREATE TABLE IF NOT EXISTS rooms ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "room_number TEXT NOT NULL UNIQUE,"
                + "type TEXT NOT NULL," // e.g., 'Single', 'Double', 'Suite'
                + "price REAL NOT NULL"
                + ");";

        // SQL statement for creating the bookings table
        String createBookingsTableSql = "CREATE TABLE IF NOT EXISTS bookings ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "room_id INTEGER NOT NULL,"
                + "guest_name TEXT NOT NULL,"
                + "check_in_date TEXT NOT NULL,"
                + "check_out_date TEXT NOT NULL,"
                + "FOREIGN KEY (room_id) REFERENCES rooms (id)"
                + ");";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = conn.createStatement()) {
            // The 'try-with-resources' statement ensures that the connection and statement are closed automatically.
            System.out.println("Connecting to database and setting up tables...");
            stmt.execute(createRoomsTableSql);
            stmt.execute(createBookingsTableSql);
            System.out.println("Database has been initialized successfully.");
        } catch (SQLException e) {
            System.err.println("Database initialization error: " + e.getMessage());
            // Exit if we can't set up the database, as the application cannot function.
            System.exit(1);
        }
    }

    /**
     * Provides a new connection to the database.
     *
     * @return A Connection object to the database.
     * @throws SQLException if a database access error occurs.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }
}
