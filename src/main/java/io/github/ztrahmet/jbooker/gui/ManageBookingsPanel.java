package io.github.ztrahmet.jbooker.gui;

import io.github.ztrahmet.jbooker.model.Booking;
import io.github.ztrahmet.jbooker.service.BookingService;
import io.github.ztrahmet.jbooker.service.ServiceResult;
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Vector;

public class ManageBookingsPanel extends JPanel implements PanelListener {

    private final BookingService bookingService;
    private final Notifier notifier;
    private JTextField searchField;
    private JTable bookingTable;
    private DefaultTableModel tableModel;

    public ManageBookingsPanel(Notifier notifier) {
        this.bookingService = new BookingService();
        this.notifier = notifier;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setupSearchPanel();
        setupTable();
    }

    private void setupSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(30);
        searchField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter guest's full name to search");
        JButton searchButton = new JButton("Find Reservations");
        searchButton.addActionListener(e -> findBookings());
        searchPanel.add(new JLabel("Enter Full Name:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);
    }

    private void setupTable() {
        tableModel = new DefaultTableModel(new String[]{"Booking ID", "Room Number", "Guest Name", "Check-in", "Check-out"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookingTable = new JTable(tableModel);
        bookingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(new JScrollPane(bookingTable), BorderLayout.CENTER);
        JButton cancelButton = new JButton("Cancel Selected Booking");
        cancelButton.addActionListener(e -> cancelSelectedBooking());
        tablePanel.add(cancelButton, BorderLayout.SOUTH);
        add(tablePanel, BorderLayout.CENTER);
    }

    private void findBookings() {
        String guestName = searchField.getText();
        if (guestName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a name to search.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        List<Booking> bookings = bookingService.findBookingsByGuestName(guestName);
        tableModel.setRowCount(0);
        if (bookings.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No bookings found for '" + guestName + "'.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
        } else {
            for (Booking booking : bookings) {
                Vector<Object> row = new Vector<>();
                row.add(booking.getId());
                row.add(booking.getRoomNumber());
                row.add(booking.getGuestName());
                row.add(booking.getCheckInDate().toString());
                row.add(booking.getCheckOutDate().toString());
                tableModel.addRow(row);
            }
        }
    }

    private void cancelSelectedBooking() {
        int selectedRow = bookingTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a booking from the table to cancel.", "Action Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int bookingId = (Integer) tableModel.getValueAt(selectedRow, 0);
        int confirmation = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to cancel booking ID " + bookingId + "?",
                "Confirm Cancellation",
                JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            ServiceResult result = bookingService.cancelBooking(bookingId);
            JOptionPane.showMessageDialog(this, result.getMessage(), "Cancellation Status", JOptionPane.INFORMATION_MESSAGE);
            if (result.isSuccess()) {
                notifier.notifyListeners();
            }
            findBookings();
        }
    }

    public void clearBookings() {
        tableModel.setRowCount(0);
        searchField.setText("");
    }

    @Override
    public void refreshData() {
        // This panel doesn't need to refresh automatically,
        // as it requires user input to display data.
    }
}
