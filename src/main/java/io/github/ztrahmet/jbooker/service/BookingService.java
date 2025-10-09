package io.github.ztrahmet.jbooker.service;

import io.github.ztrahmet.jbooker.data.BookingRepository;
import io.github.ztrahmet.jbooker.model.Booking;

import java.time.LocalDate;

/**
 * Contains the business logic for managing bookings.
 * It acts as a bridge between the user interface and the data repository.
 */
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService() {
        this.bookingRepository = new BookingRepository();
    }

    /**
     * Attempts to create a reservation after validating availability.
     *
     * @param booking The booking details to process.
     * @return A message indicating the result of the operation (success or failure).
     */
    public String makeReservation(Booking booking) {
        // --- Business Rule 1: Validate Dates ---
        if (!booking.getCheckOutDate().isAfter(booking.getCheckInDate())) {
            return "Error: Check-out date must be after the check-in date.";
        }

        // --- Business Rule 2: Check for Availability ---
        boolean isAvailable = !bookingRepository.hasOverlappingBooking(
                booking.getRoomId(),
                booking.getCheckInDate(),
                booking.getCheckOutDate()
        );

        if (isAvailable) {
            boolean success = bookingRepository.createBooking(booking);
            if (success) {
                return "Reservation successful! Your booking has been confirmed.";
            } else {
                return "Error: Could not save the reservation to the database.";
            }
        } else {
            return "Sorry, the selected room is not available for those dates.";
        }
    }
}
