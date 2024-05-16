package com.reservation.service;

import com.reservation.domain.DTOs.ReservationDTO;
import com.reservation.domain.Hotel;
import com.reservation.domain.Reservation;
import com.reservation.domain.Room;
import com.reservation.domain.User;
import com.reservation.repository.HotelRepository;
import com.reservation.repository.ReservationRepository;
import com.reservation.repository.RoomRepository;
import com.reservation.repository.UserRepository;
import exception.ReservationException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public List<Reservation> getAll() throws IOException {
        return reservationRepository.findAll();
    }

    public List<ReservationDTO> getAllDTO() throws IOException {
        List<Reservation> allReservations = reservationRepository.findAll();
        List<ReservationDTO> reservationsDTO = new ArrayList<>();
        for (Reservation reservation : allReservations) {
            reservationsDTO.add(reservationToDTO(reservation));
        }
        return reservationsDTO;
    }


    public Reservation save(Reservation reservation) throws ReservationException {
        List<Reservation> reservationsRoom = reservationRepository.findByHotelRoom(reservation.getHotel().getId(), reservation.getRoom().getId());
        if (reservation.getRoom().getIsAvailable()) {
            for (Reservation r : reservationsRoom) {
                if (!(r.getEndDate().before(reservation.getStartDate()) || r.getStartDate().after(reservation.getEndDate()))) {
                    throw new ReservationException("Room is not available in selected interval");
                }
            }
            return reservationRepository.save(reservation);
        }
        throw new ReservationException("Room is not available");
    }

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

    private ReservationDTO reservationToDTO(Reservation reservation) {
        return new ReservationDTO(reservation.getUser().getId(), reservation.getHotel().getId(),
                reservation.getRoom().getId(), reservation.getStartDate(), reservation.getEndDate());
    }

}
