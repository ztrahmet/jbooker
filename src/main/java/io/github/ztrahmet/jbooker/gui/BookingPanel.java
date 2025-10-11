package io.github.ztrahmet.jbooker.gui;

import io.github.ztrahmet.jbooker.model.Booking;
import io.github.ztrahmet.jbooker.model.Room;
import io.github.ztrahmet.jbooker.service.BookingService;
import io.github.ztrahmet.jbooker.service.RoomService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Vector;

/**
 * A panel for viewing available rooms and making a new reservation.
 */
public class BookingPanel extends JPanel {

    private final RoomService roomService;
    private final BookingService bookingService;
    private JTable roomTable;
    private DefaultTableModel tableModel;
    private JTextField guestNameField;
    private JTextField checkInDateField;
    private JTextField checkOutDateField;
    private JSpinner roomIdSpinner;

    public BookingPanel() {
        this.roomService = new RoomService();
        this.bookingService = new BookingService();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Setup components
        setupTable();
        setupBookingForm();

        // Load initial data
        loadRoomData();
    }

    private void setupTable() {
        // The table model is now created with isCellEditable overridden to return false.
        tableModel = new DefaultTableModel(new String[]{"ID", "Room Number", "Type", "Price/Night"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // This makes all cells non-editable.
                return false;
            }
        };
        roomTable = new JTable(tableModel);
        roomTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // The listener now correctly gets the ID from column 0.
        roomTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && roomTable.getSelectedRow() != -1) {
                // Get the ID from the first column (index 0).
                Object roomId = roomTable.getValueAt(roomTable.getSelectedRow(), 0);
                roomIdSpinner.setValue(roomId);
            }
        });
        add(new JScrollPane(roomTable), BorderLayout.CENTER);
    }

    private void setupBookingForm() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Make a Reservation"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Room ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Room ID:"), gbc);
        gbc.gridx = 1;
        roomIdSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
        formPanel.add(roomIdSpinner, gbc);

        // Guest Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        guestNameField = new JTextField(20);
        formPanel.add(guestNameField, gbc);

        // Check-in Date
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Check-in (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        checkInDateField = new JTextField(20);
        formPanel.add(checkInDateField, gbc);

        // Check-out Date
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Check-out (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        checkOutDateField = new JTextField(20);
        formPanel.add(checkOutDateField, gbc);

        // Submit Button
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton submitButton = new JButton("Submit Reservation");
        submitButton.addActionListener(e -> makeReservation());
        formPanel.add(submitButton, gbc);

        add(formPanel, BorderLayout.SOUTH);
    }

    public void loadRoomData() {
        // Clear existing data
        tableModel.setRowCount(0);
        // Fetch and add new data
        List<Room> rooms = roomService.getAllRooms();
        for (Room room : rooms) {
            Vector<Object> row = new Vector<>();
            row.add(room.getId());
            row.add(room.getRoomNumber());
            row.add(room.getType());
            row.add(String.format("%.2f", room.getPrice()));
            tableModel.addRow(row);
        }
    }

    private void makeReservation() {
        try {
            int roomId = (Integer) roomIdSpinner.getValue();
            String guestName = guestNameField.getText();
            LocalDate checkIn = LocalDate.parse(checkInDateField.getText());
            LocalDate checkOut = LocalDate.parse(checkOutDateField.getText());

            if (guestName.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Guest name cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Booking booking = new Booking(0, roomId, guestName, checkIn, checkOut);
            String result = bookingService.makeReservation(booking);
            JOptionPane.showMessageDialog(this, result, "Booking Status", JOptionPane.INFORMATION_MESSAGE);

            // Clear fields on success
            if (result.startsWith("Reservation successful")) {
                guestNameField.setText("");
                checkInDateField.setText("");
                checkOutDateField.setText("");
            }
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use YYYY-MM-DD.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
