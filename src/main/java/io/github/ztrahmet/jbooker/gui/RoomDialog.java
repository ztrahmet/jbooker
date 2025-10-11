package io.github.ztrahmet.jbooker.gui;

import io.github.ztrahmet.jbooker.model.Room;
import javax.swing.*;
import java.awt.*;

public class RoomDialog extends JDialog {

    private JTextField numberField;
    private JTextField typeField;
    private JTextField priceField;
    private Room room;
    private boolean saved = false;

    public RoomDialog(Frame owner, String title, Room roomToEdit) {
        super(owner, title, true);
        this.room = (roomToEdit == null) ? new Room() : roomToEdit;

        setLayout(new BorderLayout(10, 10));
        add(createFieldsPanel(), BorderLayout.CENTER);
        add(createButtonsPanel(), BorderLayout.SOUTH);

        if (roomToEdit != null) {
            numberField.setText(roomToEdit.getNumber());
            numberField.setEditable(false);
            typeField.setText(roomToEdit.getType());
            priceField.setText(String.valueOf(roomToEdit.getPrice()));
        }

        pack();
        setLocationRelativeTo(owner);
    }

    private JPanel createFieldsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Number:"), gbc);
        gbc.gridx = 1;
        numberField = new JTextField(15);
        panel.add(numberField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1;
        typeField = new JTextField(15);
        panel.add(typeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Price:"), gbc);
        gbc.gridx = 1;
        priceField = new JTextField(15);
        panel.add(priceField, gbc);

        return panel;
    }

    private JPanel createButtonsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> onSave());
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> onCancel());
        panel.add(saveButton);
        panel.add(cancelButton);
        return panel;
    }

    private void onSave() {
        try {
            room.setNumber(numberField.getText());
            room.setType(typeField.getText());
            room.setPrice(Double.parseDouble(priceField.getText()));
            if (room.getNumber().trim().isEmpty() || room.getType().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Room number and type cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            saved = true;
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid price format. Please enter a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCancel() {
        saved = false;
        dispose();
    }

    public Room getRoom() {
        return room;
    }

    public boolean isSaved() {
        return saved;
    }
}
