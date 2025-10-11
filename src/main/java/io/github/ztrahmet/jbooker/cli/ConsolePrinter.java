package io.github.ztrahmet.jbooker.cli;

import io.github.ztrahmet.jbooker.model.Booking;
import io.github.ztrahmet.jbooker.model.Room;

public final class ConsolePrinter {

    private ConsolePrinter() {}

    public static void printRoomHeader() {
        System.out.println("-------------------------------------------");
        System.out.printf("%-12s | %-10s | %s%n", "Room Number", "Type", "Price/Night");
        System.out.println("-------------------------------------------");
    }

    public static void printRoomRow(Room room) {
        System.out.printf("%-12s | %-10s | %.2f%n",
                room.getNumber(), room.getType(), room.getPrice());
    }

    public static void printBookingHeader() {
        System.out.println("-----------------------------------------------------------------");
        System.out.printf("%-10s | %-12s | %-20s | %-12s | %-12s%n", "Booking ID", "Room Number", "Guest Name", "Check-In", "Check-Out");
        System.out.println("-----------------------------------------------------------------");
    }

    public static void printBookingRow(Booking booking) {
        System.out.printf("%-10d | %-12s | %-20s | %-12s | %-12s%n",
                booking.getId(), booking.getRoomNumber(), booking.getGuestName(),
                booking.getCheckInDate(), booking.getCheckOutDate());
    }
}
