package io.github.ztrahmet.jbooker.gui;

import io.github.ztrahmet.jbooker.model.Booking;
import io.github.ztrahmet.jbooker.model.Room;
import io.github.ztrahmet.jbooker.service.BookingService;
import io.github.ztrahmet.jbooker.service.RoomService;
import io.github.ztrahmet.jbooker.service.ServiceResult;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class AdminPanel extends JPanel implements PanelListener {

    private final Notifier notifier;
    private DefaultTableModel bookingTableModel;
    private Runnable loadBookingData;
    private final RoomService roomService = new RoomService();
    private final BookingService bookingService = new BookingService();

    public AdminPanel(Notifier notifier) {
        this.notifier = notifier;
        setLayout(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Manage Rooms", createRoomManagementPanel());
        tabbedPane.addTab("Manage All Reservations", createBookingManagementPanel());
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createRoomManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        DefaultTableModel roomTableModel = new DefaultTableModel(new String[]{"Room Number", "Type", "Price/Night"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable roomTable = new JTable(roomTableModel);
        roomTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        Runnable loadRoomData = () -> {
            roomTableModel.setRowCount(0);
            List<Room> rooms = roomService.getAllRooms();
            for (Room room : rooms) {
                roomTableModel.addRow(new Object[]{room.getNumber(), room.getType(), String.format("%.2f", room.getPrice())});
            }
        };

        JButton addButton = new JButton("Add Room");
        addButton.addActionListener(e -> {
            RoomDialog dialog = new RoomDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add New Room", null);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                ServiceResult result = roomService.createRoom(dialog.getNumberText(), dialog.getTypeText(), dialog.getPriceText());
                JOptionPane.showMessageDialog(panel, result.getMessage(), "Status", result.isSuccess() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
                if (result.isSuccess()) {
                    notifier.notifyListeners();
                }
                loadRoomData.run();
            }
        });

        JButton updateButton = new JButton("Update Selected");
        updateButton.addActionListener(e -> {
            int selectedRow = roomTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(panel, "Please select a room to update.");
                return;
            }
            String roomNumber = (String) roomTable.getValueAt(selectedRow, 0);
            Optional<Room> roomOpt = roomService.findRoomByNumber(roomNumber);
            if (roomOpt.isPresent()) {
                Room roomToEdit = roomOpt.get();
                RoomDialog dialog = new RoomDialog((Frame) SwingUtilities.getWindowAncestor(this), "Edit Room " + roomNumber, roomToEdit);
                dialog.setVisible(true);
                if (dialog.isSaved()) {
                    ServiceResult result = roomService.updateRoom(roomNumber, dialog.getTypeText(), dialog.getPriceText());
                    JOptionPane.showMessageDialog(panel, result.getMessage(), "Status", result.isSuccess() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
                    if (result.isSuccess()) {
                        notifier.notifyListeners();
                    }
                    loadRoomData.run();
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Error: Could not find the selected room.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton deleteButton = new JButton("Delete Selected");
        deleteButton.addActionListener(e -> {
            int selectedRow = roomTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(panel, "Please select a room to delete.");
                return;
            }
            String roomNumber = (String) roomTable.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(panel, "Delete room " + roomNumber + "?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                ServiceResult result = roomService.deleteRoom(roomNumber);
                JOptionPane.showMessageDialog(panel, result.getMessage());
                if (result.isSuccess()) {
                    notifier.notifyListeners();
                }
                loadRoomData.run();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        panel.add(new JScrollPane(roomTable), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        loadRoomData.run();
        return panel;
    }

    private JPanel createBookingManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bookingTableModel = new DefaultTableModel(new String[]{"ID", "Room Number", "Guest", "Check-in", "Check-out"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable bookingTable = new JTable(bookingTableModel);
        bookingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        loadBookingData = () -> {
            bookingTableModel.setRowCount(0);
            List<Booking> bookings = bookingService.getAllBookings();
            for (Booking booking : bookings) {
                bookingTableModel.addRow(new Object[]{booking.getId(), booking.getRoomNumber(), booking.getGuestName(), booking.getCheckInDate(), booking.getCheckOutDate()});
            }
        };

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton deleteBookingButton = new JButton("Delete Selected Booking");
        JButton refreshButton = new JButton("Refresh");
        buttonPanel.add(deleteBookingButton);
        buttonPanel.add(refreshButton);
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
                ServiceResult result = bookingService.cancelBooking(bookingId);
                JOptionPane.showMessageDialog(panel, result.getMessage());
                if (result.isSuccess()) {
                    notifier.notifyListeners();
                }
                loadBookingData.run();
            }
        });
        panel.add(new JScrollPane(bookingTable), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        loadBookingData.run();
        return panel;
    }

    @Override
    public void refreshData() {
        if (loadBookingData != null) {
            loadBookingData.run();
        }
    }
}
