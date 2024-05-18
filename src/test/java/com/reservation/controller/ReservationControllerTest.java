package com.reservation.controller;

import com.reservation.domain.Hotel;
import com.reservation.domain.Reservation;
import com.reservation.domain.Room;
import com.reservation.domain.User;
import com.reservation.domain.dto.ReservationDTO;
import com.reservation.service.ReservationService;
import com.reservation.exception.ReservationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ReservationControllerTest {
    @Mock
    private ReservationService reservationService;
    @InjectMocks
    private ReservationController reservationController;

    @BeforeAll
    public static void beforeAll() {
        MockitoAnnotations.openMocks(ReservationControllerTest.class);
    }


    @Test
    void getReservationsDTOTest() {
        ReservationDTO reservation = new ReservationDTO(1, 2, 3, new Date(), new Date());
        Mockito.when(reservationService.getAllDTO())
                .thenReturn(List.of(reservation));

        Mockito.verifyNoInteractions(this.reservationService);

        List<ReservationDTO> response = reservationController.getReservationsDTO().getBody();

        Mockito.verify(this.reservationService, Mockito.times(1)).getAllDTO();
        assert (response.get(0).equals(reservation));

    }

    @Test
    void getReservationsByUserTest() {
        Reservation reservation = new Reservation(new User(), new Hotel(), new Room(), new Date(), new Date());
        Mockito.when(reservationService.getAllByUser(1))
                .thenReturn(List.of(reservation));

        Mockito.verifyNoInteractions(this.reservationService);

        List<Reservation> response = reservationController.getReservationsByUser(1).getBody();

        Mockito.verify(this.reservationService, Mockito.times(1)).getAllByUser(1);
        Mockito.verify(this.reservationService, Mockito.times(0)).getAllByUser(7);
        assert (Objects.requireNonNull(response).get(0).equals(reservation));

    }

    @Test
    void newReservationsTest() {
        Reservation reservation = new Reservation(new User(), new Hotel(), new Room(), new Date(), new Date());
        ReservationDTO reservationDTO = new ReservationDTO(1, 2, 3, new Date(), new Date());

        Reservation emptyReservation = new Reservation();
        try {
            Mockito.when(reservationService.save(reservation))
                    .thenReturn(reservation);
            Mockito.when(reservationService.reservationFromDTO(reservationDTO))
                    .thenReturn(reservation);
            Mockito.when(reservationService.save(emptyReservation))
                    .thenThrow(new ReservationException("Null reservation error"));

            Reservation response = reservationController.newReservation(reservationDTO).getBody();

            Mockito.verify(this.reservationService, Mockito.times(1)).save(reservation);
            Mockito.verify(this.reservationService, Mockito.times(1)).reservationFromDTO(reservationDTO);

            assert (response.equals(reservation));
            assertThrows(ReservationException.class, () -> reservationService.save(emptyReservation));

        } catch (ReservationException e) {
            assert false;
        }
    }

    @Test
    void cancelReservationsTest() {
        Reservation reservation = new Reservation(new User(), new Hotel(), new Room(), new Date(), new Date());
        try {
            Mockito.when(reservationService.deleteById(1))
                    .thenReturn(reservation);
            Mockito.when(reservationService.deleteById(null))
                    .thenThrow(new ReservationException("Null id error"));

            Reservation response = reservationController.cancelReservation(1).getBody();

            Mockito.verify(this.reservationService, Mockito.times(1)).deleteById(1);

            assert (response.equals(reservation));
            assertThrows(ReservationException.class, () -> reservationService.deleteById(null));

        } catch (ReservationException e) {
            assert false;
        }

    }

    @Test
    void updateRoomTest() {
        try {
            Mockito.when(reservationService.updateRoom(111, 22))
                    .thenReturn(1);
            Mockito.when(reservationService.updateRoom(0, 0))
                    .thenReturn(0);
            Mockito.when(reservationService.updateRoom(null, null))
                    .thenThrow(new ReservationException("Null data error"));

            String response1 = reservationController.updateRoom(111, 22).getBody();
            String response2 = reservationController.updateRoom(0, 0).getBody();

            Mockito.verify(this.reservationService, Mockito.times(1)).updateRoom(111, 22);
            Mockito.verify(this.reservationService, Mockito.times(1)).updateRoom(0, 0);

            assert (Objects.equals(response1, "Successfully updated!"));
            assert (Objects.equals(response2, "No reservation was updated!"));
            assertThrows(ReservationException.class, () -> reservationService.updateRoom(null, null));

        } catch (ReservationException e) {
            assert false;
        }

    }

    @Test
    void updateFeedbackTest() {
        try {
            Mockito.when(reservationService.updateFeedback(111, "update"))
                    .thenReturn(1);
            Mockito.when(reservationService.updateFeedback(0, "no update"))
                    .thenReturn(0);
            Mockito.when(reservationService.updateFeedback(null, null))
                    .thenThrow(new ReservationException("Null data error"));

            String response1 = reservationController.updateFeedback(111, "update").getBody();
            String response2 = reservationController.updateFeedback(0, "no update").getError();

            Mockito.verify(this.reservationService, Mockito.times(1)).updateFeedback(111, "update");
            Mockito.verify(this.reservationService, Mockito.times(1)).updateFeedback(0, "no update");

            assert (Objects.equals(response1, "Successfully updated!"));
            assert (Objects.equals(response2, "Failed to update reservation!"));

            assertThrows(ReservationException.class, () -> reservationService.updateFeedback(null, null));

        } catch (ReservationException e) {
            assert false;
        }
    }
}
