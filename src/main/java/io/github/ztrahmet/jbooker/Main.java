package io.github.ztrahmet.jbooker;

import io.github.ztrahmet.jbooker.cli.CommandLineInterface;
import io.github.ztrahmet.jbooker.data.DatabaseManager;

/**
 * The main entry point for the JBooker CLI application.
 * Its primary responsibilities are to initialize the application
 * and start the user interaction loop.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Starting JBooker Hotel Reservation System...");

        // Initialize the database. This will create the DB file and tables if they don't exist.
        DatabaseManager.initializeDatabase();

        // Create and start the command-line interface to interact with the user.
        CommandLineInterface cli = new CommandLineInterface();
        cli.start();

        System.out.println("Thank you for using JBooker. Exiting.");
    }
}
