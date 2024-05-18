package com.reservation.service;

import com.reservation.domain.Hotel;
import com.reservation.domain.Reservation;
import com.reservation.domain.Room;
import com.reservation.domain.User;
import com.reservation.domain.dto.ReservationDTO;
import com.reservation.repository.HotelRepository;
import com.reservation.repository.ReservationRepository;
import com.reservation.repository.RoomRepository;
import com.reservation.repository.UserRepository;
import com.reservation.exception.ReservationException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Class for managing reservation-related logic
 */
@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;

    public ReservationService(ReservationRepository reservationRepository, UserRepository userRepository, HotelRepository hotelRepository, RoomRepository roomRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
    }

    /**
     * Saves a new reservation
     *
     * @param reservation Reservation to be saved
     * @return The reservation that was saved
     * @throws ReservationException Exception with specific error message for the cases when
     *                              invalid dates
     *                              room is not available in given interval
     *                              or room is not available in general
     */
    public Reservation save(Reservation reservation) throws ReservationException {
        if(reservation.getStartDate().after(reservation.getEndDate())){
            throw new ReservationException("Invalid dates! Start date must be before end date!");
        }
        if(reservation.getStartDate().before(new Date())){
            throw new ReservationException("Invalid dates! Reservation dates must be in future!");
        }
        List<Reservation> reservationsRoom = reservationRepository.findByHotelRoom(reservation.getHotel().getId(), reservation.getRoom().getId());
        if (reservation.getRoom().isAvailable()) {
            for (Reservation r : reservationsRoom) {
                if (!(r.getEndDate().before(reservation.getStartDate()) || r.getStartDate().after(reservation.getEndDate()))) {
                    throw new ReservationException("Room is not available in selected interval");
                }
            }
            return reservationRepository.save(reservation);
        }
        throw new ReservationException("Room is not available");
    }

    /**
     * Finds all reservations
     *
     * @return List of DTOs for reservations
     */
    public List<ReservationDTO> getAllDTO() {
        List<Reservation> allReservations = reservationRepository.findAll();
        List<ReservationDTO> reservationsDTO = new ArrayList<>();
        for (Reservation reservation : allReservations) {
            reservationsDTO.add(reservationToDTO(reservation));
        }
        return reservationsDTO;
    }

    public Optional<Reservation> findById(Integer id) {
        return reservationRepository.findById(id);
    }

    /**
     * Update room in a reservation
     *
     * @param id        id of reservation to be updated
     * @param newRoomId The id of new room
     * @return Integer referring number of entities that have been updated
     * @throws ReservationException Exception with specific error message for the cases when
     *                              the new room is not a valid one
     *                              the reservation can no longer be modified
     *                              the id reservation does not exist
     */
    public int updateRoom(Integer id, Integer newRoomId) throws ReservationException {
        Optional<Reservation> reservation = findById(id);
        if (reservation.isPresent()) {
            Duration duration = Duration.between(new Date().toInstant(), reservation.get().getStartDate().toInstant());
            long differenceInMinutes = Math.abs(duration.toMinutes());
            if (differenceInMinutes > 120) {
                Optional<Room> newRoom = roomRepository.findById(newRoomId);
                if (newRoom.isPresent()) {
                    return reservationRepository.updateRoom(id, newRoom.get());
                } else {
                    throw new ReservationException("The new room does not exist!");
                }
            } else {
                throw new ReservationException("Reservation can no longer be modified!");
            }
        } else {
            throw new ReservationException("Invalid reservation!");
        }

    }

    /**
     * Update feedback for a reservation
     *
     * @param reservationId id of reservation to be updated
     * @param feedback      The new feedback to be added
     * @return Integer referring number of entities that have been updated
     * @throws ReservationException Exception with specific error message for the cases when
     *                              the end date of reservation has not passed yet
     *                              the id reservation does not exist
     */
    public int updateFeedback(Integer reservationId, String feedback) throws ReservationException {
        Optional<Reservation> reservation = reservationRepository.findById(reservationId);
        if (reservation.isPresent()) {
            if (new Date().compareTo(reservation.get().getEndDate()) >= 0) {
                return reservationRepository.updateFeedback(reservationId, feedback);
            }
            throw new ReservationException("Feedback cannot be added before reservation ending!");
        } else {
            throw new ReservationException("Invalid reservation");
        }
    }

    /**
     * Delete reservation
     *
     * @param id id of reservation to be deleted
     * @return Reservation just deleted
     * @throws ReservationException Exception with specific error message for the cases when
     *                              the reservation can no longer be canceled
     *                              the id reservation does not exist
     */
    public Reservation deleteById(Integer id) throws ReservationException {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        if (reservation.isPresent()) {
            Duration duration = Duration.between(new Date().toInstant(), reservation.get().getStartDate().toInstant());
            long differenceInMinutes = Math.abs(duration.toMinutes());
            if (differenceInMinutes > 120) {
                reservationRepository.deleteById(id);
                return reservation.get();
            } else {
                throw new ReservationException("The reservation can no longer be canceled!");
            }
        } else {
            throw new ReservationException("The reservation does not exist!");
        }
    }

    /**
     * Transform ReservationDTO into Reservation
     *
     * @param reservationDTO reservation DTO to be transformed
     * @return Reservation
     * @throws ReservationException Exception with specific error message for the cases when
     *                              the user id contained bt DTO does not exist
     *                              the hotel id contained bt DTO does not exist
     *                              the room id contained bt DTO does not exist
     */
    public Reservation reservationFromDTO(ReservationDTO reservationDTO) throws ReservationException {
        Optional<User> user = userRepository.findById(reservationDTO.getUserId());
        Optional<Hotel> hotel = hotelRepository.findById(reservationDTO.getHotelId());
        Optional<Room> room = roomRepository.findById(reservationDTO.getRoomId());
        if (user.isEmpty()) {
            throw new ReservationException("Invalid user");
        }
        if (hotel.isEmpty()) {
            throw new ReservationException("Invalid hotel");
        }
        if (room.isEmpty()) {
            throw new ReservationException("Invalid room");
        }
        return new Reservation(user.get(), hotel.get(), room.get(), reservationDTO.getStartDate(), reservationDTO.getEndDate());
    }

    /**
     * Transform Reservation to ReservationDTO
     *
     * @param reservation reservation to be transformed into DTO
     * @return The DTO
     */
    private ReservationDTO reservationToDTO(Reservation reservation) {
        ReservationDTO reservationDTO = new ReservationDTO(reservation.getUser().getId(), reservation.getHotel().getId(), reservation.getRoom().getId(), reservation.getStartDate(), reservation.getEndDate());
        reservationDTO.setId(reservation.getId());
        return reservationDTO;
    }


    public List<Reservation> getAllByUser(Integer userId) {
        return reservationRepository.findAllByUser(userId);
    }
}
