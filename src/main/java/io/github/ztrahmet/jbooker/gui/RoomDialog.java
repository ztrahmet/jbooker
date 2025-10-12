package io.github.ztrahmet.jbooker.gui;

import io.github.ztrahmet.jbooker.model.Room;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

@Getter
public class RoomDialog extends JDialog {

    private final JTextField numberField;
    private final JTextField typeField;
    private final JTextField priceField;
    private boolean saved = false;

    public RoomDialog(Frame owner, String title, Room roomToEdit) {
        super(owner, title, true);

        setLayout(new BorderLayout(10, 10));

        // Initialize fields
        numberField = new JTextField(15);
        typeField = new JTextField(15);
        priceField = new JTextField(15);

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
        panel.add(numberField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1;
        panel.add(typeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Price:"), gbc);
        gbc.gridx = 1;
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
        // Basic check for empty fields in the dialog itself.
        if (getNumberText().trim().isEmpty() || getTypeText().trim().isEmpty() || getPriceText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        saved = true;
        dispose();
    }

    private void onCancel() {
        saved = false;
        dispose();
    }

    // Getters for raw text values
    public String getNumberText() {
        return numberField.getText();
    }

    public String getTypeText() {
        return typeField.getText();
    }

    public String getPriceText() {
        return priceField.getText();
    }
}
