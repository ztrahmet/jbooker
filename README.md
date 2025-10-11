# JBooker: Hotel Reservation System (CLI & GUI)

JBooker is a complete, command-line and graphical-based hotel reservation system written in Java. This project is
designed to showcase a strong understanding of Object-Oriented Programming (OOP) principles, multi-layered application
architecture, and modern Java development practices.

---

## ✨ Features

The application supports all the core functionalities required for a hotel management system, accessible via both a
Command-Line Interface (CLI) and a Graphical User Interface (GUI).

### User Features

* **View Available Rooms:** Display a clean, formatted list of all rooms, including their type and price per night.
* **Make a Reservation:** Book a room for a specific date range, with validation to prevent double-booking.
* **View & Cancel Bookings:** Search for existing bookings by guest name and cancel them.

### Administrative Features

* **Room Management:** A dedicated admin menu to perform CRUD (Create, Read, Update, Delete) operations on rooms.
* **Dynamic Data:** Add, update, or remove rooms from the hotel's inventory in real-time.

---

## 🛠️ Tech Stack & Architecture

This project was built using industry-standard tools and a professional architectural pattern to ensure the code is
maintainable, scalable, and testable.

* **Language:** `Java`
* **Build Tool:** `Maven`
* **Database:** `SQLite`
* **Testing:** `JUnit`
* **Utilities:** `Lombok`

### Application Architecture

The system is built on a classic 3-tier architecture, which separates the application's concerns into distinct layers.
This is a core concept in modern software engineering.

```

┌───────────────────┐
│ Presentation Layer│ (cli / gui)
└─────────┬─────────┘
          │
┌─────────▼─────────┐
│   Service Layer   │ (Business Logic)
└─────────┬─────────┘
          │
┌─────────▼─────────┐
│ Data Access Layer │ (Database Queries)
└─────────┬─────────┘
          │
┌─────────▼─────────┐
│     Database      │ (SQLite)
└───────────────────┘

```

---

## 🚀 Getting Started

### Prerequisites

* Java JDK 17 or higher
* Apache Maven

### How to Build

1. Clone the repository:
   ```bash
   git clone https://github.com/ztrahmet/jbooker.git
   ```
2. Navigate to the project directory:
   ```bash
   cd jbooker
   ```
3. Build the project and create the executable JAR file:
   ```bash
   mvn clean package
   ```
   This will create a `jbooker-$VERSION.jar` file in the `target` directory.

### How to Run

The application can be launched in two different modes from the command line.

* **To run the GUI (default mode):**
    ```bash
    java -jar target/jbooker-$VERSION.jar
    ```
* **To run the CLI:**
    ```bash
    java -jar target/jbooker-$VERSION.jar cli
    ```

The `jbooker.db` database file will be created automatically in the root directory on the first run.
