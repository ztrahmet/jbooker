package io.github.ztrahmet.jbooker.cli;

import io.github.ztrahmet.jbooker.model.Booking;
import io.github.ztrahmet.jbooker.model.Room;
import io.github.ztrahmet.jbooker.service.BookingService;
import io.github.ztrahmet.jbooker.service.RoomService;

import java.util.List;
import java.util.Scanner;

/**
 * Handles all administrative tasks for the command-line interface.
 * This class is a self-contained component for managing rooms and all bookings.
 */
public class AdminConsole {

    private final Scanner scanner;
    private final RoomService roomService;
    private final BookingService bookingService;

    public AdminConsole(Scanner scanner, RoomService roomService, BookingService bookingService) {
        this.scanner = scanner;
        this.roomService = roomService;
        this.bookingService = bookingService;
    }

    public void start() {
        boolean administering = true;
        while (administering) {
            printAdminMenu();
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    handleRoomAdministration();
                    break;
                case "2":
                    handleBookingAdministration();
                    break;
                case "0":
                    administering = false; // Return to main menu
                    break;
                default:
                    System.out.println("\n*** Invalid option. Please select a number from the menu. ***");
            }
        }
    }

    private void printAdminMenu() {
        System.out.println("\n--- Administration Menu ---");
        System.out.println("1. Manage Rooms");
        System.out.println("2. Manage All Bookings");
        System.out.println("0. Back to Main Menu");
        System.out.print("Please enter your choice: ");
    }

    // --- Room Administration Methods ---

    private void handleRoomAdministration() {
        boolean managing = true;
        while (managing) {
            printRoomManagementMenu();
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    handleAddRoom();
                    break;
                case "2":
                    handleUpdateRoom();
                    break;
                case "3":
                    handleDeleteRoom();
                    break;
                case "0":
                    managing = false; // Return to admin menu
                    break;
                default:
                    System.out.println("\n*** Invalid option. Please select a valid number. ***");
            }
        }
    }

    private void printRoomManagementMenu() {
        System.out.println("\n--- Room Management ---");
        System.out.println("1. Add a New Room");
        System.out.println("2. Update an Existing Room");
        System.out.println("3. Delete a Room");
        System.out.println("0. Back to Admin Menu");
        System.out.print("Please enter your choice: ");
    }

    private void handleAddRoom() {
        System.out.println("\n--- Add a New Room ---");
        try {
            System.out.print("Enter new room number (e.g., 401): ");
            String roomNumber = scanner.nextLine();
            System.out.print("Enter room type (e.g., Single, Double, Suite): ");
            String type = scanner.nextLine();
            System.out.print("Enter price per night (e.g., 99.99): ");
            double price = Double.parseDouble(scanner.nextLine());
            Room newRoom = new Room(0, roomNumber, type, price);
            String result = roomService.createRoom(newRoom);
            System.out.println("\n" + result);
        } catch (NumberFormatException e) {
            System.err.println("\nError: Invalid price. Please enter a valid number.");
        }
    }

    private void handleUpdateRoom() {
        System.out.println("\n--- Update an Existing Room ---");
        viewAllRooms(); // Show list of rooms to help the user choose
        try {
            System.out.print("Enter the ID of the room to update (or 0 to cancel): ");
            int id = Integer.parseInt(scanner.nextLine());
            if (id == 0) return;
            System.out.print("Enter new room number: ");
            String roomNumber = scanner.nextLine();
            System.out.print("Enter new room type: ");
            String type = scanner.nextLine();
            System.out.print("Enter new price per night: ");
            double price = Double.parseDouble(scanner.nextLine());
            Room updatedRoom = new Room(id, roomNumber, type, price);
            String result = roomService.updateRoom(updatedRoom);
            System.out.println("\n" + result);
        } catch (NumberFormatException e) {
            System.err.println("\nError: Invalid ID or price. Please enter valid numbers.");
        }
    }

    private void handleDeleteRoom() {
        System.out.println("\n--- Delete a Room ---");
        viewAllRooms(); // Show list of rooms to help the user choose
        try {
            System.out.print("Enter the ID of the room to delete (or 0 to cancel): ");
            int id = Integer.parseInt(scanner.nextLine());
            if (id == 0) return;
            System.out.print("Are you sure you want to permanently delete room ID " + id + "? (y/n): ");
            String confirmation = scanner.nextLine();
            if (confirmation.equalsIgnoreCase("y")) {
                String result = roomService.deleteRoom(id);
                System.out.println("\n" + result);
            } else {
                System.out.println("\nDeletion cancelled.");
            }
        } catch (NumberFormatException e) {
            System.err.println("\nError: Invalid ID. Please enter a number.");
        }
    }

    // --- Booking Administration Methods ---

    private void handleBookingAdministration() {
        System.out.println("\n--- Manage All Bookings ---");
        List<Booking> allBookings = bookingService.getAllBookings();
        if (allBookings.isEmpty()) {
            System.out.println("There are currently no bookings in the system.");
            return;
        }
        ConsolePrinter.printBookingHeader();
        for (Booking booking : allBookings) {
            ConsolePrinter.printBookingRow(booking);
        }
        System.out.println("-----------------------------------------------------------------");
        try {
            System.out.print("\nEnter the ID of a booking to delete (or 0 to go back): ");
            int bookingIdToDelete = Integer.parseInt(scanner.nextLine());
            if (bookingIdToDelete == 0) {
                return;
            }
            System.out.print("Are you sure you want to permanently delete booking ID " + bookingIdToDelete + "? (y/n): ");
            String confirmation = scanner.nextLine();
            if (confirmation.equalsIgnoreCase("y")) {
                String result = bookingService.cancelBooking(bookingIdToDelete);
                System.out.println("\n" + result);
            } else {
                System.out.println("\nDeletion cancelled.");
            }
        } catch (NumberFormatException e) {
            System.err.println("\nError: Invalid ID. Please enter a number.");
        }
    }

    // --- Helper Method ---

    private void viewAllRooms() {
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
        System.out.println("-------------------------------------------------");
    }
}
