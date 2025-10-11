package io.github.ztrahmet.jbooker.gui;

import io.github.ztrahmet.jbooker.model.Booking;
import io.github.ztrahmet.jbooker.model.Room;
import io.github.ztrahmet.jbooker.service.BookingService;
import io.github.ztrahmet.jbooker.service.RoomService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Vector;

/**
 * An administrative panel with tabs for managing both Rooms and Bookings.
 */
public class AdminPanel extends JPanel {

    public AdminPanel() {
        setLayout(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane();

        // Create the two sub-panels
        JPanel roomManagementPanel = createRoomManagementPanel();
        JPanel bookingManagementPanel = createBookingManagementPanel();

        // Add them as tabs
        tabbedPane.addTab("Manage Rooms", roomManagementPanel);
        tabbedPane.addTab("Manage All Reservations", bookingManagementPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    // --- Panel for Managing Rooms ---
    private JPanel createRoomManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        RoomService roomService = new RoomService();
        DefaultTableModel roomTableModel = new DefaultTableModel(new String[]{"ID", "Room Number", "Type", "Price/Night"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable roomTable = new JTable(roomTableModel);
        roomTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Function to load/reload room data
        Runnable loadRoomData = () -> {
            roomTableModel.setRowCount(0);
            List<Room> rooms = roomService.getAllRooms();
            for (Room room : rooms) {
                roomTableModel.addRow(new Object[]{room.getId(), room.getRoomNumber(), room.getType(), String.format("%.2f", room.getPrice())});
            }
        };

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton addButton = new JButton("Add Room");
        JButton updateButton = new JButton("Update Selected");
        JButton deleteButton = new JButton("Delete Selected");
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        // Button Actions
        addButton.addActionListener(e -> {
            RoomDialog dialog = new RoomDialog(null, "Add New Room", null);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                String result = roomService.createRoom(dialog.getRoom());
                JOptionPane.showMessageDialog(panel, result);
                loadRoomData.run();
            }
        });
        updateButton.addActionListener(e -> { /* Update logic from old AdminRoomPanel */ });
        deleteButton.addActionListener(e -> { /* Delete logic from old AdminRoomPanel */ });

        panel.add(new JScrollPane(roomTable), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        loadRoomData.run(); // Initial data load
        return panel;
    }

    // --- Panel for Managing Bookings ---
    private JPanel createBookingManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        BookingService bookingService = new BookingService();
        DefaultTableModel bookingTableModel = new DefaultTableModel(new String[]{"ID", "Room ID", "Guest", "Check-in", "Check-out"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable bookingTable = new JTable(bookingTableModel);
        bookingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Function to load/reload booking data
        Runnable loadBookingData = () -> {
            bookingTableModel.setRowCount(0);
            List<Booking> bookings = bookingService.getAllBookings();
            for (Booking booking : bookings) {
                bookingTableModel.addRow(new Object[]{booking.getId(), booking.getRoomId(), booking.getGuestName(), booking.getCheckInDate(), booking.getCheckOutDate()});
            }
        };

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton deleteBookingButton = new JButton("Delete Selected Booking");
        JButton refreshButton = new JButton("Refresh");
        buttonPanel.add(deleteBookingButton);
        buttonPanel.add(refreshButton);

        // Button Actions
        refreshButton.addActionListener(e -> loadBookingData.run());
        deleteBookingButton.addActionListener(e -> {
            int selectedRow = bookingTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(panel, "Please select a reservation to delete.");
                return;
            }
            int bookingId = (int) bookingTableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(panel, "Delete reservation " + bookingId + "?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String result = bookingService.cancelBooking(bookingId);
                JOptionPane.showMessageDialog(panel, result);
                loadBookingData.run();
            }
        });

        panel.add(new JScrollPane(bookingTable), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        loadBookingData.run(); // Initial data load
        return panel;
    }
}
