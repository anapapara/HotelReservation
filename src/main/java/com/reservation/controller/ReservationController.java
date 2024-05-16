package com.reservation.controller;

import com.reservation.domain.DTOs.ReservationDTO;
import com.reservation.domain.Hotel;
import com.reservation.domain.Reservation;
import com.reservation.domain.Room;
import com.reservation.repository.RoomRepository;
import com.reservation.service.ReservationService;
import exception.ReservationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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

    @PutMapping("/reservations/{id}")
    public ResponseEntity updateReservation(@RequestBody ReservationDTO reservationDTO, @PathVariable Integer id) {
        try {
            Reservation reservation = reservationService.updateReservation(reservationDTO, id);
            return ResponseEntity.accepted().body(reservation);
        } catch (ReservationException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
