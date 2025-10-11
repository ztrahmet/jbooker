package io.github.ztrahmet.jbooker.service;

import io.github.ztrahmet.jbooker.data.RoomRepository;
import io.github.ztrahmet.jbooker.model.Room;

import java.util.Collections;
import java.util.List;

public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService() {
        this.roomRepository = new RoomRepository();
    }

    /**
     * Retrieves all rooms from the repository.
     * @return A list of all rooms.
     */
    public List<Room> getAllRooms() {
        return roomRepository.findAllRooms();
    }

    /**
     * Finds a single room by its ID.
     * @param roomId The ID of the room.
     * @return The Room object if found, otherwise null.
     */
    public Room findRoomById(int roomId) {
        // .orElse(null) is used to convert the Optional<Room> to a nullable Room object,
        // which can be simpler for some GUI components to handle.
        return roomRepository.findById(roomId).orElse(null);
    }

    public String createRoom(Room room) {
        if (room.getRoomNumber() == null || room.getRoomNumber().trim().isEmpty()) {
            return "Error: Room number cannot be empty.";
        }
        if (roomRepository.existsByRoomNumber(room.getRoomNumber())) {
            return "Error: A room with this number already exists.";
        }
        boolean success = roomRepository.create(room);
        return success ? "Room created successfully." : "Error: Failed to create room.";
    }

    public String updateRoom(Room room) {
        if (room.getRoomNumber() == null || room.getRoomNumber().trim().isEmpty()) {
            return "Error: Room number cannot be empty.";
        }
        boolean success = roomRepository.update(room);
        return success ? "Room updated successfully." : "Error: Failed to update room.";
    }

    public String deleteRoom(int id) {
        boolean success = roomRepository.delete(id);
        return success ? "Room deleted successfully." : "Error: Failed to delete room.";
    }
}
