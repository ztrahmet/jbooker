package io.github.ztrahmet.jbooker.gui;

import io.github.ztrahmet.jbooker.model.Room;
import io.github.ztrahmet.jbooker.service.BookingService;
import io.github.ztrahmet.jbooker.service.RoomService;
import io.github.ztrahmet.jbooker.service.ServiceResult;
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Vector;

public class BookingPanel extends JPanel implements PanelListener {

    private final RoomService roomService;
    private final BookingService bookingService;
    private final Notifier notifier;
    private JTable roomTable;
    private DefaultTableModel tableModel;
    private JTextField guestNameField;
    private JTextField checkInDateField;
    private JTextField checkOutDateField;
    private JSpinner roomNumberSpinner;

    public BookingPanel(Notifier notifier) {
        this.roomService = new RoomService();
        this.bookingService = new BookingService();
        this.notifier = notifier;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setupTable();
        setupBookingForm();
        loadRoomData();
    }

    private void setupTable() {
        tableModel = new DefaultTableModel(new String[]{"Room Number", "Type", "Price/Night"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        roomTable = new JTable(tableModel);
        roomTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        roomTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && roomTable.getSelectedRow() != -1) {
                Object roomNumber = roomTable.getValueAt(roomTable.getSelectedRow(), 0);
                roomNumberSpinner.setValue(roomNumber);
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

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Room Number:"), gbc);
        gbc.gridx = 1;
        roomNumberSpinner = new JSpinner();
        formPanel.add(roomNumberSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        guestNameField = new JTextField(20);
        guestNameField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter full name");
        formPanel.add(guestNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Check-in:"), gbc);
        gbc.gridx = 1;
        checkInDateField = new JTextField(20);
        checkInDateField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "YYYY-MM-DD");
        formPanel.add(checkInDateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Check-out:"), gbc);
        gbc.gridx = 1;
        checkOutDateField = new JTextField(20);
        checkOutDateField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "YYYY-MM-DD");
        formPanel.add(checkOutDateField, gbc);

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
        tableModel.setRowCount(0);
        List<Room> rooms = roomService.getAllRooms();
        List<String> roomNumbers = new java.util.ArrayList<>();
        for (Room room : rooms) {
            Vector<Object> row = new Vector<>();
            row.add(room.getNumber());
            row.add(room.getType());
            row.add(String.format("%.2f", room.getPrice()));
            tableModel.addRow(row);
            roomNumbers.add(room.getNumber());
        }
        if (!roomNumbers.isEmpty()) {
            roomNumberSpinner.setModel(new SpinnerListModel(roomNumbers));
        } else {
            roomNumberSpinner.setModel(new SpinnerListModel(new String[]{}));
        }
    }

    private void makeReservation() {
        String roomNumber = (String) roomNumberSpinner.getValue();
        String guestName = guestNameField.getText();
        String checkIn = checkInDateField.getText();
        String checkOut = checkOutDateField.getText();

        ServiceResult result = bookingService.makeReservation(roomNumber, guestName, checkIn, checkOut);
        JOptionPane.showMessageDialog(this, result.getMessage(), "Booking Status",
                result.isSuccess() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);

        if (result.isSuccess()) {
            guestNameField.setText("");
            checkInDateField.setText("");
            checkOutDateField.setText("");
            notifier.notifyListeners();
        }
    }

    @Override
    public void refreshData() {
        loadRoomData();
    }
}
