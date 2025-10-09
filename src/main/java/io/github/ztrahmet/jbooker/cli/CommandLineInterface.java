package io.github.ztrahmet.jbooker.cli;

import java.util.Scanner;

/**
 * Handles all command-line input and output for the application.
 * This is the "view" part of your application.
 */
public class CommandLineInterface {

    private final Scanner scanner;

    public CommandLineInterface() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts the main interactive loop of the application.
     */
    public void start() {
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.println("\n---> Feature [View Rooms] is not implemented yet. <---");
                    break;
                case "2":
                    System.out.println("\n---> Feature [Make Reservation] is not implemented yet. <---");
                    break;
                case "3":
                    System.out.println("\n---> Feature [Cancel Reservation] is not implemented yet. <---");
                    break;
                case "4":
                    running = false;
                    break;
                default:
                    System.out.println("\n*** Invalid option. Please select a number from 1 to 4. ***");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n===========================");
        System.out.println("    JBooker Main Menu");
        System.out.println("===========================");
        System.out.println("1. View Available Rooms");
        System.out.println("2. Make a Reservation");
        System.out.println("3. Cancel a Reservation");
        System.out.println("4. Exit");
        System.out.print("Please enter your choice: ");
    }
}
