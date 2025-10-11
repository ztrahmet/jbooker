package io.github.ztrahmet.jbooker.service;

import io.github.ztrahmet.jbooker.data.BookingRepository;
import io.github.ztrahmet.jbooker.model.Booking;

import java.time.LocalDate;
import java.util.List;

public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService() {
        this.bookingRepository = new BookingRepository();
    }

    /**
     * Retrieves all bookings from the database.
     * @return A list of all bookings.
     */
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> findBookingsByGuestName(String guestName) {
        return bookingRepository.findByGuestName(guestName);
    }

    public String cancelBooking(int bookingId) {
        boolean success = bookingRepository.deleteBookingById(bookingId);
        if (success) {
            return "Booking successfully cancelled.";
        } else {
            return "Error: Could not cancel the booking. It may have already been cancelled or the ID is incorrect.";
        }
    }

    public String makeReservation(Booking booking) {
        if (!booking.getCheckOutDate().isAfter(booking.getCheckInDate())) {
            return "Error: Check-out date must be after the check-in date.";
        }
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
