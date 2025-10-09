package io.github.ztrahmet.jbooker.cli;

import io.github.ztrahmet.jbooker.data.RoomRepository;
import io.github.ztrahmet.jbooker.model.Booking;
import io.github.ztrahmet.jbooker.model.Room;
import io.github.ztrahmet.jbooker.service.BookingService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class CommandLineInterface {

    private final Scanner scanner;
    private final RoomRepository roomRepository;
    private final BookingService bookingService;

    public CommandLineInterface() {
        this.scanner = new Scanner(System.in);
        this.roomRepository = new RoomRepository();
        this.bookingService = new BookingService();
    }

    public void start() {
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    handleViewRooms();
                    break;
                case "2":
                    handleMakeReservation();
                    break;
                case "3":
                    handleViewAndCancelReservations();
                    break;
                case "4":
                    running = false;
                    break;
                default:
                    System.out.println("\n*** Invalid option. Please select a number from 1 to 4. ***");
            }
        }
        System.out.println("\nThank you for using JBooker. Goodbye!");
    }

    private void printMenu() {
        System.out.println("\n===========================");
        System.out.println("    JBooker Main Menu");
        System.out.println("===========================");
        System.out.println("1. View All Rooms");
        System.out.println("2. Make a Reservation");
        System.out.println("3. View & Cancel My Reservations");
        System.out.println("4. Exit");
        System.out.print("Please enter your choice: ");
    }

    private void handleViewRooms() {
        List<Room> rooms = roomRepository.findAllRooms();
        System.out.println("\n--- All Hotel Rooms ---");
        if (rooms.isEmpty()) {
            System.out.println("No rooms found in the system.");
            return;
        }
        printRoomHeader();
        for (Room room : rooms) {
            printRoomRow(room);
        }
        System.out.println("-------------------------------------------------");
    }

    private void handleMakeReservation() {
        System.out.println("\n--- Make a New Reservation ---");
        handleViewRooms();
        try {
            System.out.print("Enter the ID of the room you want to book: ");
            int roomId = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter your full name: ");
            String guestName = scanner.nextLine();
            System.out.print("Enter check-in date (YYYY-MM-DD): ");
            LocalDate checkInDate = LocalDate.parse(scanner.nextLine());
            System.out.print("Enter check-out date (YYYY-MM-DD): ");
            LocalDate checkOutDate = LocalDate.parse(scanner.nextLine());
            Booking newBooking = new Booking(0, roomId, guestName, checkInDate, checkOutDate);
            String result = bookingService.makeReservation(newBooking);
            System.out.println("\n" + result);
        } catch (NumberFormatException e) {
            System.err.println("\nError: Invalid room ID. Please enter a number.");
        } catch (DateTimeParseException e) {
            System.err.println("\nError: Invalid date format. Please use YYYY-MM-DD.");
        }
    }

    private void handleViewAndCancelReservations() {
        System.out.println("\n--- View & Cancel Reservations ---");
        System.out.print("Please enter your full name to find your bookings: ");
        String guestName = scanner.nextLine();
        List<Booking> bookings = bookingService.findBookingsByGuestName(guestName);
        if (bookings.isEmpty()) {
            System.out.println("\nNo bookings found for '" + guestName + "'.");
            return;
        }
        System.out.println("\n--- Bookings for " + guestName + " ---");
        printBookingHeader();
        for (Booking booking : bookings) {
            printBookingRow(booking);
        }
        System.out.println("-----------------------------------------------------------------");
        System.out.print("\nEnter the ID of the booking to cancel (or enter 0 to go back): ");
        try {
            int bookingIdToCancel = Integer.parseInt(scanner.nextLine());
            if (bookingIdToCancel == 0) {
                return;
            }
            boolean isValidId = bookings.stream().anyMatch(b -> b.getId() == bookingIdToCancel);
            if (isValidId) {
                String result = bookingService.cancelBooking(bookingIdToCancel);
                System.out.println("\n" + result);
            } else {
                System.out.println("\nError: Invalid ID. The ID you entered does not match any of your bookings.");
            }
        } catch (NumberFormatException e) {
            System.err.println("\nError: Invalid input. Please enter a number.");
        }
    }

    private void printRoomHeader() {
        System.out.println("-------------------------------------------------");
        System.out.printf("%-5s | %-12s | %-10s | %s%n", "ID", "Room Number", "Type", "Price/Night");
        System.out.println("-------------------------------------------------");
    }

    private void printRoomRow(Room room) {
        System.out.printf("%-5d | %-12s | %-10s | $%.2f%n",
                room.getId(), room.getRoomNumber(), room.getType(), room.getPrice());
    }

    private void printBookingHeader() {
        System.out.println("-----------------------------------------------------------------");
        System.out.printf("%-10s | %-8s | %-20s | %-12s | %-12s%n", "Booking ID", "Room ID", "Guest Name", "Check-In", "Check-Out");
        System.out.println("-----------------------------------------------------------------");
    }

    private void printBookingRow(Booking booking) {
        System.out.printf("%-10d | %-8d | %-20s | %-12s | %-12s%n",
                booking.getId(), booking.getRoomId(), booking.getGuestName(),
                booking.getCheckInDate(), booking.getCheckOutDate());
    }
}
