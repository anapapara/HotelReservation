package com.reservation.controller;

import com.reservation.domain.Room;
import com.reservation.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Rest controller for managing room-related operations.
 */
@RestController
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * Retrieve all rooms for a given hotel
     *
     * @param hotelId The id of the hotel to search rooms for
     * @return ResponseEntity containing a list of Rooms
     */
    @GetMapping("/rooms/{hotelId}")
    public ResponseEntity<List<Room>> getAllByHotelId(@PathVariable Integer hotelId) {
        List<Room> rooms = roomService.findAvailableByHotelId(hotelId);
        return ResponseEntity.accepted().body(rooms);
    }
}
