package com.reservation.controller;

import com.reservation.domain.Reservation;
import com.reservation.domain.dto.ReservationDTO;
import com.reservation.domain.response.ReservationDTOListResponse;
import com.reservation.domain.response.ReservationResponse;
import com.reservation.domain.response.StringResponse;
import com.reservation.service.ReservationService;
import exception.ReservationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService service) {
        this.reservationService = service;
    }

    @GetMapping("/reservations")
    public ReservationDTOListResponse getReservationsDTO() {
        return new ReservationDTOListResponse(reservationService.getAllDTO(), null);
    }

    @GetMapping("/reservations/user/{id}")
    public ResponseEntity<List<Reservation>> getReservationsByUser(@PathVariable Integer id) {
        return ResponseEntity.accepted().body(reservationService.getAllByUser(id));
    }

    @PostMapping("/reservations")
    public ReservationResponse newReservation(@RequestBody ReservationDTO reservationDTO) {
        try {
            Reservation reservation = reservationService.reservationFromDTO(reservationDTO);
            return new ReservationResponse(reservationService.save(reservation), null);
        } catch (ReservationException e) {
            return new ReservationResponse(null, e.getMessage());
        }
    }


    @DeleteMapping("/reservations/{id}")
    public ReservationResponse cancelReservation(@PathVariable Integer id) {
        try {
            return new ReservationResponse(reservationService.deleteById(id), null);
        } catch (ReservationException e) {
            return new ReservationResponse(null, e.getMessage());
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
    public StringResponse updateFeedback(@PathVariable Integer id, @PathVariable String feedback) {
        try {
            int updatedRows = reservationService.updateFeedback(id, feedback);
            if (updatedRows > 0) {
                return new StringResponse("Successfully updated!", null);
            } else {
                return new StringResponse(null, "Failed to update reservation!");
            }
        } catch (ReservationException e) {
            return new StringResponse(null, e.getMessage());
        }
    }
}
