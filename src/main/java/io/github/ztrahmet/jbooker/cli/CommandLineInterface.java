package io.github.ztrahmet.jbooker.cli;

import io.github.ztrahmet.jbooker.model.Booking;
import io.github.ztrahmet.jbooker.model.Room;
import io.github.ztrahmet.jbooker.service.BookingService;
import io.github.ztrahmet.jbooker.service.RoomService;
import io.github.ztrahmet.jbooker.service.ServiceResult;

import java.util.List;
import java.util.Scanner;

public class CommandLineInterface {

    private final Scanner scanner;
    private final BookingService bookingService;
    private final RoomService roomService;

    public CommandLineInterface() {
        this.scanner = new Scanner(System.in);
        this.bookingService = new BookingService();
        this.roomService = new RoomService();
    }

    public void start() {
        boolean running = true;
        while (running) {
            printMainMenu();
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
                    new AdminConsole(scanner, roomService, bookingService).start();
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("\n*** Invalid option. Please select a valid number. ***");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n===========================");
        System.out.println("    JBooker Main Menu");
        System.out.println("===========================");
        System.out.println("1. View All Rooms");
        System.out.println("2. Make a Reservation");
        System.out.println("3. View & Cancel My Reservations");
        System.out.println("4. Administration");
        System.out.println("0. Exit");
        System.out.print("Please enter your choice: ");
    }

    private void handleViewRooms() {
        List<Room> rooms = roomService.getAllRooms();
        System.out.println("\n--- All Hotel Rooms ---");
        if (rooms.isEmpty()) {
            System.out.println("No rooms found in the system.");
            return;
        }
        ConsolePrinter.printRoomHeader();
        for (Room room : rooms) {
            ConsolePrinter.printRoomRow(room);
        }
        System.out.println("-------------------------------------------");
    }

    private void handleMakeReservation() {
        System.out.println("\n--- Make a New Reservation ---");
        handleViewRooms();
        System.out.print("Enter the Room Number you want to book (or 0 to cancel): ");
        String roomNumber = scanner.nextLine();
        if (roomNumber.equals("0")) {
            System.out.println("Reservation cancelled.");
            return;
        }
        System.out.print("Enter your full name: ");
        String guestName = scanner.nextLine();
        System.out.print("Enter check-in date (YYYY-MM-DD): ");
        String checkInDate = scanner.nextLine();
        System.out.print("Enter check-out date (YYYY-MM-DD): ");
        String checkOutDate = scanner.nextLine();

        ServiceResult result = bookingService.makeReservation(roomNumber, guestName, checkInDate, checkOutDate);
        System.out.println("\n" + result.getMessage());
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
        ConsolePrinter.printBookingHeader();
        for (Booking booking : bookings) {
            ConsolePrinter.printBookingRow(booking);
        }
        System.out.println("-----------------------------------------------------------------");
        try {
            System.out.print("\nEnter the ID of the booking to cancel (or 0 to go back): ");
            int bookingIdToCancel = Integer.parseInt(scanner.nextLine());
            if (bookingIdToCancel == 0) {
                return;
            }
            boolean isValidId = bookings.stream().anyMatch(b -> b.getId() == bookingIdToCancel);
            if (isValidId) {
                ServiceResult result = bookingService.cancelBooking(bookingIdToCancel);
                System.out.println("\n" + result.getMessage());
            } else {
                System.out.println("\nError: Invalid ID. The ID you entered does not match any of your bookings.");
            }
        } catch (NumberFormatException e) {
            System.err.println("\nError: Invalid input. Please enter a number.");
        }
    }
}
