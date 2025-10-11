package io.github.ztrahmet.jbooker;

import io.github.ztrahmet.jbooker.cli.CommandLineInterface;
import io.github.ztrahmet.jbooker.gui.MainFrame;
import io.github.ztrahmet.jbooker.data.DatabaseManager;

/**
 * The main entry point for the JBooker application.
 * This class checks command-line arguments to decide whether to start
 * the Command Line Interface (CLI) or the Graphical User Interface (GUI).
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Starting JBooker Hotel Reservation System...");

        // Initialize the database. This is needed for both GUI and CLI.
        DatabaseManager.initializeDatabase();

        // Check if the "cli" argument was provided.
        if (args.length > 0 && args[0].equalsIgnoreCase("cli")) {
            // If "cli" argument is present, launch the command-line interface.
            System.out.println("Mode: Command Line Interface");
            CommandLineInterface cli = new CommandLineInterface();
            cli.start();
        } else {
            // By default (no arguments), launch the graphical user interface.
            System.out.println("Mode: Graphical User Interface");
            MainFrame frame = new MainFrame();
            frame.initialize();
        }

        System.out.println("\nJBooker has finished execution. Exiting.");
    }
}
