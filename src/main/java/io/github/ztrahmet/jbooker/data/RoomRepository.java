package io.github.ztrahmet.jbooker.data;

import io.github.ztrahmet.jbooker.model.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Handles all database operations for the Room entity.
 */
public class RoomRepository {

    public List<Room> findAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT id, room_number, type, price FROM rooms ORDER BY room_number";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                rooms.add(mapRowToRoom(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching rooms: " + e.getMessage());
        }
        return rooms;
    }

    public Optional<Room> findByRoomNumber(String roomNumber) {
        String sql = "SELECT id, room_number, type, price FROM rooms WHERE room_number = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, roomNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToRoom(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding room by number: " + e.getMessage());
        }
        return Optional.empty();
    }

    public boolean createRoom(Room room) {
        String sql = "INSERT INTO rooms(room_number, type, price) VALUES(?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, room.getRoomNumber());
            pstmt.setString(2, room.getType());
            pstmt.setDouble(3, room.getPrice());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error creating room: " + e.getMessage());
            return false;
        }
    }

    public boolean updateRoom(Room room) {
        String sql = "UPDATE rooms SET room_number = ?, type = ?, price = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, room.getRoomNumber());
            pstmt.setString(2, room.getType());
            pstmt.setDouble(3, room.getPrice());
            pstmt.setInt(4, room.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating room: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteRoom(int roomId) {
        String sql = "DELETE FROM rooms WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, roomId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting room: " + e.getMessage());
            return false;
        }
    }

    private Room mapRowToRoom(ResultSet rs) throws SQLException {
        return new Room(
                rs.getInt("id"),
                rs.getString("room_number"),
                rs.getString("type"),
                rs.getDouble("price")
        );
    }
}
