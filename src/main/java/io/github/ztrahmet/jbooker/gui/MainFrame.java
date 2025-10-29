package io.github.ztrahmet.jbooker.gui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("JBooker - Hotel Reservation System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        Notifier notifier = new Notifier();

        BookingPanel bookingPanel = new BookingPanel(notifier);
        ManageBookingsPanel manageBookingsPanel = new ManageBookingsPanel(notifier);
        AdminPanel adminPanel = new AdminPanel(notifier);

        notifier.addListener(bookingPanel);
        notifier.addListener(manageBookingsPanel);
        notifier.addListener(adminPanel);

        tabbedPane.addTab("View Rooms & Book", bookingPanel);
        tabbedPane.addTab("Manage Reservations", manageBookingsPanel);
        tabbedPane.addTab("Administration", adminPanel);

        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedComponent() != manageBookingsPanel) {
                manageBookingsPanel.clearBookings();
            }
        });

        add(tabbedPane, BorderLayout.CENTER);
    }

    public void initialize() {
        setVisible(true);
    }
}
