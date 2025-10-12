package io.github.ztrahmet.jbooker.service;

import io.github.ztrahmet.jbooker.data.BookingRepository;
import io.github.ztrahmet.jbooker.model.Booking;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService() {
        this.bookingRepository = new BookingRepository();
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> findBookingsByGuestName(String guestName) {
        if (guestName == null || guestName.trim().isEmpty()) {
            return List.of(); // Return empty list for invalid input
        }
        return bookingRepository.findByGuestName(guestName);
    }

    public ServiceResult cancelBooking(String bookingIdStr) {
        try {
            int bookingId = Integer.parseInt(bookingIdStr);
            return cancelBooking(bookingId);
        } catch (NumberFormatException e) {
            return ServiceResult.failure("Error: Invalid ID. Please enter a number.");
        }
    }

    public ServiceResult cancelBooking(int bookingId) {
        boolean success = bookingRepository.deleteBookingById(bookingId);
        if (success) {
            return ServiceResult.success("Booking successfully cancelled.");
        } else {
            return ServiceResult.failure("Error: Could not cancel the booking. It may have already been cancelled or the ID is incorrect.");
        }
    }

    public ServiceResult makeReservation(String roomNumber, String guestName, String checkInDateStr, String checkOutDateStr) {
        if (guestName == null || guestName.trim().isEmpty()) {
            return ServiceResult.failure("Guest name cannot be empty.");
        }
        if (roomNumber == null || roomNumber.trim().isEmpty()) {
            return ServiceResult.failure("Room number cannot be empty.");
        }
        try {
            LocalDate checkInDate = LocalDate.parse(checkInDateStr);
            LocalDate checkOutDate = LocalDate.parse(checkOutDateStr);
            Booking booking = new Booking(0, roomNumber, guestName, checkInDate, checkOutDate);
            return makeReservation(booking);
        } catch (DateTimeParseException e) {
            return ServiceResult.failure("Invalid date format. Please use YYYY-MM-DD.");
        }
    }

    public ServiceResult makeReservation(Booking booking) {
        if (!booking.getCheckOutDate().isAfter(booking.getCheckInDate())) {
            return ServiceResult.failure("Error: Check-out date must be after the check-in date.");
        }
        boolean isAvailable = !bookingRepository.hasOverlappingBooking(
                booking.getRoomNumber(),
                booking.getCheckInDate(),
                booking.getCheckOutDate()
        );
        if (isAvailable) {
            boolean success = bookingRepository.createBooking(booking);
            if (success) {
                return ServiceResult.success("Reservation successful! Your booking has been confirmed.");
            } else {
                return ServiceResult.failure("Error: Could not save the reservation to the database.");
            }
        } else {
            return ServiceResult.failure("Sorry, the selected room is not available for those dates.");
        }
    }
}
