package io.github.ztrahmet.jbooker.service;

import io.github.ztrahmet.jbooker.data.RoomRepository;
import io.github.ztrahmet.jbooker.model.Room;

import java.util.Optional;

/**
 * Contains the business logic for managing rooms.
 */
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService() {
        this.roomRepository = new RoomRepository();
    }

    /**
     * Creates a new room after validating the data.
     *
     * @param room The room object to create.
     * @return A message indicating the result of the operation.
     */
    public String createRoom(Room room) {
        // Business Rule: A room number must not already exist.
        Optional<Room> existingRoom = roomRepository.findByRoomNumber(room.getRoomNumber());
        if (existingRoom.isPresent()) {
            return "Error: A room with number '" + room.getRoomNumber() + "' already exists.";
        }

        // Business Rule: Price must be positive.
        if (room.getPrice() <= 0) {
            return "Error: Price must be a positive number.";
        }

        boolean success = roomRepository.createRoom(room);
        return success ? "Room successfully created." : "Error: Could not create the room in the database.";
    }

    /**
     * Updates an existing room's details.
     *
     * @param room The room object with updated information.
     * @return A message indicating the result.
     */
    public String updateRoom(Room room) {
        // Business Rule: Ensure a different room doesn't already have the new number.
        Optional<Room> existingRoom = roomRepository.findByRoomNumber(room.getRoomNumber());
        if (existingRoom.isPresent() && existingRoom.get().getId() != room.getId()) {
            return "Error: Another room with number '" + room.getRoomNumber() + "' already exists.";
        }

        boolean success = roomRepository.updateRoom(room);
        return success ? "Room successfully updated." : "Error: Could not update the room. Please check the ID.";
    }

    /**
     * Deletes a room from the system.
     *
     * @param roomId The ID of the room to delete.
     * @return A message indicating the result.
     */
    public String deleteRoom(int roomId) {
        // In a more complex app, we would check if the room has future bookings before deleting.
        boolean success = roomRepository.deleteRoom(roomId);
        return success ? "Room successfully deleted." : "Error: Could not delete the room. It may have existing bookings or the ID is incorrect.";
    }
}
