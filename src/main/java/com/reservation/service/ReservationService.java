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
import exception.ReservationException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
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
                    throw new ReservationException("New room you selected does not exist!");
                }
            } else {
                throw new ReservationException("You can no longer modify the reservation!");
            }
        } else {
            throw new ReservationException("Invalid reservation!");
        }

    }

    public int updateFeedback(Integer reservationId, String feedback) throws ReservationException {
        Optional<Reservation> reservation = reservationRepository.findById(reservationId);
        if (reservation.isPresent()) {
            if (new Date().compareTo(reservation.get().getEndDate()) >= 0) {
                return reservationRepository.updateFeedback(reservationId, feedback);
            }
            throw new ReservationException("You cannot add feedback before reservation ending!");
        } else {
            throw new ReservationException("Invalid reservation");
        }
    }

    public Reservation deleteById(Integer id) throws ReservationException {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        if (reservation.isPresent()) {
            Duration duration = Duration.between(new Date().toInstant(), reservation.get().getStartDate().toInstant());
            long differenceInMinutes = Math.abs(duration.toMinutes());
            if (differenceInMinutes > 120) {
                reservationRepository.deleteById(id);
                return reservation.get();
            } else {
                throw new ReservationException("You can no longer cancel the reservation!");
            }
        } else {
            throw new ReservationException("The reservation you want to cancel does not exist!");
        }
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
        ReservationDTO reservationDTO = new ReservationDTO(reservation.getUser().getId(), reservation.getHotel().getId(), reservation.getRoom().getId(), reservation.getStartDate(), reservation.getEndDate());
        reservationDTO.setId(reservation.getId());
        return reservationDTO;
    }


    public List<Reservation> getAllByUser(Integer userId) {
        return reservationRepository.findAllByUser(userId);
    }
}
