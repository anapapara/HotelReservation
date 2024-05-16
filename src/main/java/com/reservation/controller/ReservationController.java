package com.reservation.controller;

import com.reservation.domain.DTOs.ReservationDTO;
import com.reservation.domain.Reservation;
import com.reservation.repository.RoomRepository;
import com.reservation.service.ReservationService;
import exception.ReservationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class ReservationController {
    private final ReservationService reservationService;
    private final RoomRepository roomRepository;

    public ReservationController(ReservationService service,
                                 RoomRepository roomRepository) {
        this.reservationService = service;
        this.roomRepository = roomRepository;
    }

    @GetMapping("/reservations")
    public ResponseEntity getReservations() {
        try {
            return ResponseEntity.accepted().body(reservationService.getAllDTO());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/reservations")
    public ResponseEntity newReservation(@RequestBody ReservationDTO reservationDTO) {
        try {
            Reservation reservation = reservationService.reservationFromDTO(reservationDTO);
            return ResponseEntity.accepted().body(reservationService.save(reservation));
        } catch (ReservationException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity cancelReservation(@PathVariable Integer id) {
        try {
            return ResponseEntity.accepted().body(reservationService.deleteById(id));
        } catch (ReservationException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/reservations/{id}/{roomId}")
    public ResponseEntity<String> updateRoom(@PathVariable Integer id, @PathVariable Integer roomId) {
        try {
            int updatedRows = reservationService.updateRoom(id, roomId);
            if (updatedRows > 0) {
                return ResponseEntity.accepted().body("Successfully updated!");
            } else {
                return ResponseEntity.badRequest().body("No reservation was updated!");
            }
        } catch (ReservationException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/reservations/feedback/{id}/{feedback}")
    public ResponseEntity<String> updateFeedback(@PathVariable Integer id, @PathVariable String feedback) {
        try {
            int updatedRows = reservationService.addFeedback(id, feedback);
            if (updatedRows > 0) {
                return ResponseEntity.accepted().body("Successfully updated!");
            } else {
                return ResponseEntity.badRequest().body("No reservation was updated!");
            }
        } catch (ReservationException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
