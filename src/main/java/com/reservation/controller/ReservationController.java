package com.reservation.controller;

import com.reservation.domain.DTOs.ReservationDTO;
import com.reservation.domain.Hotel;
import com.reservation.domain.Reservation;
import com.reservation.service.ReservationService;
import exception.ReservationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService service) {
        this.reservationService = service;
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
}
