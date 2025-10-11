package io.github.ztrahmet.jbooker.cli;

import io.github.ztrahmet.jbooker.model.Booking;
import io.github.ztrahmet.jbooker.model.Room;

/**
 * A utility class dedicated to printing formatted tables to the console.
 * This class cannot be instantiated.
 */
public final class ConsolePrinter {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private ConsolePrinter() {}

    public static void printRoomHeader() {
        System.out.println("-------------------------------------------------");
        System.out.printf("%-5s | %-12s | %-10s | %s%n", "ID", "Room Number", "Type", "Price/Night");
        System.out.println("-------------------------------------------------");
    }

    public static void printRoomRow(Room room) {
        System.out.printf("%-5d | %-12s | %-10s | %.2f%n",
                room.getId(), room.getRoomNumber(), room.getType(), room.getPrice());
    }

    public static void printBookingHeader() {
        System.out.println("-----------------------------------------------------------------");
        System.out.printf("%-10s | %-8s | %-20s | %-12s | %-12s%n", "Booking ID", "Room ID", "Guest Name", "Check-In", "Check-Out");
        System.out.println("-----------------------------------------------------------------");
    }

    public static void printBookingRow(Booking booking) {
        System.out.printf("%-10d | %-8d | %-20s | %-12s | %-12s%n",
                booking.getId(), booking.getRoomId(), booking.getGuestName(),
                booking.getCheckInDate(), booking.getCheckOutDate());
    }
}
