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

    public ServiceResult createRoom(String number, String type, String priceStr) {
        if (number == null || number.trim().isEmpty() || type == null || type.trim().isEmpty()) {
            return ServiceResult.failure("Room number and type cannot be empty.");
        }
        try {
            double price = Double.parseDouble(priceStr);
            Room room = new Room(number, type, price);
            return createRoom(room);
        } catch (NumberFormatException e) {
            return ServiceResult.failure("Invalid price format. Please enter a valid number.");
        }
    }

    public ServiceResult createRoom(Room room) {
        if (roomRepository.findByNumber(room.getNumber()).isPresent()) {
            return ServiceResult.failure("Error: Room with number " + room.getNumber() + " already exists.");
        }
        boolean success = roomRepository.create(room);
        return success ? ServiceResult.success("Room created successfully.") : ServiceResult.failure("Error: Failed to create room.");
    }

    public ServiceResult updateRoom(String number, String type, String priceStr) {
        if (type == null || type.trim().isEmpty()) {
            return ServiceResult.failure("Room type cannot be empty.");
        }
        try {
            double price = Double.parseDouble(priceStr);
            Room room = new Room(number, type, price);
            return updateRoom(room);
        } catch (NumberFormatException e) {
            return ServiceResult.failure("Invalid price format. Please enter a valid number.");
        }
    }

    public ServiceResult updateRoom(Room room) {
        boolean success = roomRepository.update(room);
        return success ? ServiceResult.success("Room updated successfully.") : ServiceResult.failure("Error: Failed to update room.");
    }

    public ServiceResult deleteRoom(String number) {
        if (number == null || number.trim().isEmpty()) {
            return ServiceResult.failure("Room number cannot be empty.");
        }
        boolean success = roomRepository.delete(number);
        return success ? ServiceResult.success("Room deleted successfully.") : ServiceResult.failure("Error: Failed to delete room.");
    }
}
