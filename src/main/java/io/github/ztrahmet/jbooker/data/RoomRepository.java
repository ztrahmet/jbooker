package io.github.ztrahmet.jbooker.data;

import io.github.ztrahmet.jbooker.model.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomRepository {

    public Optional<Room> findByNumber(String number) {
        String sql = "SELECT number, type, price FROM rooms WHERE number = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, number);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Room room = new Room(
                        rs.getString("number"),
                        rs.getString("type"),
                        rs.getDouble("price")
                );
                return Optional.of(room);
            }
        } catch (SQLException e) {
            System.err.println("Error finding room by number: " + e.getMessage());
        }
        return Optional.empty();
    }

    public boolean create(Room room) {
        String sql = "INSERT INTO rooms(number, type, price) VALUES(?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, room.getNumber());
            pstmt.setString(2, room.getType());
            pstmt.setDouble(3, room.getPrice());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error creating room: " + e.getMessage());
            return false;
        }
    }

    public boolean update(Room room) {
        String sql = "UPDATE rooms SET type = ?, price = ? WHERE number = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, room.getType());
            pstmt.setDouble(2, room.getPrice());
            pstmt.setString(3, room.getNumber());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating room: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(String number) {
        String sql = "DELETE FROM rooms WHERE number = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, number);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting room: " + e.getMessage());
            return false;
        }
    }

    public List<Room> findAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT number, type, price FROM rooms ORDER BY number";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Room room = new Room(
                        rs.getString("number"),
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
