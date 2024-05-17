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

/**
 * Rest controller for managing reservation-related operations.
 */
@RestController
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService service) {
        this.reservationService = service;
    }

    /**
     * Retrieve all reservations
     *
     * @return ReservationDTOListResponse that contains a list of ReservationDTOs
     */
    @GetMapping("/reservations")
    public ReservationDTOListResponse getReservationsDTO() {
        return new ReservationDTOListResponse(reservationService.getAllDTO(), null);
    }

    /**
     * Retrieve reservation by user id
     *
     * @param id The id of the user to search reservations for
     * @return ResponseEntity containing a list of Reservations
     */
    @GetMapping("/reservations/user/{id}")
    public ResponseEntity<List<Reservation>> getReservationsByUser(@PathVariable Integer id) {
        return ResponseEntity.accepted().body(reservationService.getAllByUser(id));
    }

    /**
     * Create new reservation
     *
     * @param reservationDTO ReservationDTO containing reservation basic information
     * @return ReservationResponse containing even the new created reservation
     * or exception message in case of error on creating or adding
     */
    @PostMapping("/reservations")
    public ReservationResponse newReservation(@RequestBody ReservationDTO reservationDTO) {
        try {
            Reservation reservation = reservationService.reservationFromDTO(reservationDTO);
            return new ReservationResponse(reservationService.save(reservation), null);
        } catch (ReservationException e) {
            return new ReservationResponse(null, e.getMessage());
        }
    }

    /**
     * Delete reservation
     *
     * @param id id of reservation to be deleted
     * @return ReservationResponse containing even the just deleted reservation
     * *          or exception message in case of error on deleting
     */
    @DeleteMapping("/reservations/{id}")
    public ReservationResponse cancelReservation(@PathVariable Integer id) {
        try {
            return new ReservationResponse(reservationService.deleteById(id), null);
        } catch (ReservationException e) {
            return new ReservationResponse(null, e.getMessage());
        }
    }

    /**
     * Update room for a reservation
     *
     * @param id     id of reservation to be updated
     * @param roomId id of the new room
     * @return ResponseEntity containing a string about the success of updating
     */
    @PutMapping("/reservations/{id}/{roomId}")
    public ResponseEntity<String> updateRoom(@PathVariable Integer id, @PathVariable Integer roomId) {
        try {
            int updatedRows = reservationService.updateRoom(id, roomId);
            if (updatedRows > 0) {
                return ResponseEntity.ok().body("Successfully updated!");
            } else {
                return ResponseEntity.accepted().body("No reservation was updated!");
            }
        } catch (ReservationException e) {
            return ResponseEntity.accepted().body(e.getMessage());
        }
    }

    /**
     * Add feedback for a reservation
     *
     * @param id       id of reservation to be updated
     * @param feedback the string to be added as feedback
     * @return StringResponse containing information about the success of updating
     * or an error message in case of error
     */
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
