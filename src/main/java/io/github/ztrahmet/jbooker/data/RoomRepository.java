package io.github.ztrahmet.jbooker.data;

import io.github.ztrahmet.jbooker.model.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all database operations related to the Room entity.
 * This is our Data Access Object (DAO) for Rooms.
 */
public class RoomRepository {

    /**
     * Retrieves all rooms from the database.
     *
     * @return A list of Room objects.
     */
    public List<Room> findAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT id, room_number, type, price FROM rooms ORDER BY room_number";

        // Use try-with-resources to ensure the connection and statement are automatically closed.
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            // Loop through the result set and create Room objects
            while (rs.next()) {
                Room room = new Room(
                        rs.getInt("id"),
                        rs.getString("room_number"),
                        rs.getString("type"),
                        rs.getDouble("price")
                );
                rooms.add(room);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching rooms: " + e.getMessage());
        }
        return rooms;
    }
}
