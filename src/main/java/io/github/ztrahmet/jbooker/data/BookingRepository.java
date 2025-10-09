package io.github.ztrahmet.jbooker.data;

import io.github.ztrahmet.jbooker.model.Booking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingRepository {

    public List<Booking> findByGuestName(String guestName) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT id, room_id, guest_name, check_in_date, check_out_date FROM bookings WHERE UPPER(guest_name) = UPPER(?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, guestName);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Booking booking = new Booking(
                        rs.getInt("id"),
                        rs.getInt("room_id"),
                        rs.getString("guest_name"),
                        LocalDate.parse(rs.getString("check_in_date")),
                        LocalDate.parse(rs.getString("check_out_date"))
                );
                bookings.add(booking);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching bookings by guest name: " + e.getMessage());
        }
        return bookings;
    }

    public boolean deleteBookingById(int bookingId) {
        String sql = "DELETE FROM bookings WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookingId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting booking: " + e.getMessage());
            return false;
        }
    }

    public boolean hasOverlappingBooking(int roomId, LocalDate checkIn, LocalDate checkOut) {
        String sql = "SELECT COUNT(*) FROM bookings WHERE room_id = ? AND check_out_date > ? AND check_in_date < ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, roomId);
            pstmt.setString(2, checkIn.toString());
            pstmt.setString(3, checkOut.toString());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking for overlapping bookings: " + e.getMessage());
        }
        return true;
    }

    public boolean createBooking(Booking booking) {
        String sql = "INSERT INTO bookings(room_id, guest_name, check_in_date, check_out_date) VALUES(?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, booking.getRoomId());
            pstmt.setString(2, booking.getGuestName());
            pstmt.setString(3, booking.getCheckInDate().toString());
            pstmt.setString(4, booking.getCheckOutDate().toString());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error creating booking: " + e.getMessage());
            return false;
        }
    }
}
