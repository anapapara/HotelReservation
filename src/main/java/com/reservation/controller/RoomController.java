package com.reservation.controller;

import com.reservation.domain.Room;
import com.reservation.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/rooms/{hotelId}")
    public ResponseEntity getAllByHotelId(@PathVariable Integer hotelId) {
        try {
            List<Room> rooms = roomService.findAvailableByHotelId(hotelId);
            return ResponseEntity.accepted().body(rooms);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
