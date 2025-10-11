package io.github.ztrahmet.jbooker.gui;

import javax.swing.*;
import java.awt.*;

/**
 * The main window for the JBooker GUI application.
 * It uses a JTabbedPane to organize the different functionalities of the application.
 */
public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("JBooker - Hotel Reservation System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 600)); // Set a reasonable minimum size
        setLocationRelativeTo(null); // Center the window on the screen

        // Create the tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Add panels as tabs
        tabbedPane.addTab("View Rooms & Book", new BookingPanel());
        tabbedPane.addTab("Manage Reservations", new ManageBookingsPanel());
        tabbedPane.addTab("Admin: Manage Rooms & Reservations", new AdminPanel());

        // Add the tabbed pane to the frame
        add(tabbedPane, BorderLayout.CENTER);
    }

    /**
     * Sets a modern Look and Feel and makes the GUI visible.
     */
    public void initialize() {
        try {
            // Set a more modern Look and Feel
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            System.err.println("Nimbus Look and Feel not found. Using default.");
        }
        setVisible(true);
    }
}
