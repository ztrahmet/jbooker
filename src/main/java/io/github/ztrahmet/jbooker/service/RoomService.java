package io.github.ztrahmet.jbooker.service;

import io.github.ztrahmet.jbooker.data.RoomRepository;
import io.github.ztrahmet.jbooker.model.Room;

import java.util.List;
import java.util.Optional;

public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService() {
        this.roomRepository = new RoomRepository();
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAllRooms();
    }

    public Optional<Room> findRoomByNumber(String roomNumber) {
        return roomRepository.findByNumber(roomNumber);
    }

    public String createRoom(Room room) {
        boolean success = roomRepository.create(room);
        return success ? "Room created successfully." : "Error: Failed to create room.";
    }

    public String updateRoom(Room room) {
        boolean success = roomRepository.update(room);
        return success ? "Room updated successfully." : "Error: Failed to update room.";
    }

    public String deleteRoom(String number) {
        boolean success = roomRepository.delete(number);
        return success ? "Room deleted successfully." : "Error: Failed to delete room.";
    }
}
