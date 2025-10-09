package io.github.ztrahmet.jbooker.data;

import io.github.ztrahmet.jbooker.model.Booking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Handles all database operations for the Booking entity.
 * This is our Data Access Object (DAO) for Bookings.
 */
public class BookingRepository {

    /**
     * Checks if a specific room has any overlapping bookings within a given date range.
     *
     * @param roomId    The ID of the room to check.
     * @param checkIn   The proposed check-in date.
     * @param checkOut  The proposed check-out date.
     * @return true if there is an overlapping booking, false otherwise.
     */
    public boolean hasOverlappingBooking(int roomId, LocalDate checkIn, LocalDate checkOut) {
        // SQL query to find bookings that overlap with the desired date range.
        // Overlap condition: (StartA <= EndB) and (EndA >= StartB)
        String sql = "SELECT COUNT(*) FROM bookings "
                + "WHERE room_id = ? AND check_out_date > ? AND check_in_date < ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, roomId);
            pstmt.setString(2, checkIn.toString());
            pstmt.setString(3, checkOut.toString());

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // If count > 0, an overlap exists.
            }

        } catch (SQLException e) {
            System.err.println("Error checking for overlapping bookings: " + e.getMessage());
        }
        return true; // Fail-safe: assume overlap if there's an error.
    }

    /**
     * Saves a new booking to the database.
     *
     * @param booking The Booking object to save.
     * @return true if the booking was created successfully, false otherwise.
     */
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
