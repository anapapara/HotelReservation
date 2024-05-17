package com.reservation.repository;

import com.reservation.domain.Reservation;
import com.reservation.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Class for managing database operations related to reservation entities
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    @Override
    <S extends Reservation> S save(S entity);

    @Override
    List<Reservation> findAll();

    @Override
    Optional<Reservation> findById(Integer integer);

    /**
     * Finds all reservations for a specific room
     *
     * @param hotelId The hotelId of hotel to search reservations for
     * @param roomId  The roomId of room to search reservations for
     * @return List of Reservations
     */
    @Query("select r from Reservation r where r.hotel.id = :hotelId and r.room.id = :roomId")
    List<Reservation> findByHotelRoom(@Param("hotelId") Integer hotelId,
                                      @Param("roomId") Integer roomId);

    @Override
    void deleteById(Integer integer);

    /**
     * Update feedback for a reservation
     *
     * @param reservationId id of reservation to be updated
     * @param feedback      The user feedback
     * @return Integer representing number of rows that have been updated
     */
    @Modifying
    @Transactional
    @Query("update Reservation r set r.feedback = :feedback where r.id = :reservationId")
    int updateFeedback(@Param("reservationId") Integer reservationId,
                       @Param("feedback") String feedback);

    /**
     * Update room for a reservation
     *
     * @param rId  id of reservation to be updated
     * @param room The new room for the reservation
     * @return Integer representing number of rows that have been updated
     */
    @Modifying
    @Transactional
    @Query("update Reservation r set r.room = :room where r.id = :rId")
    int updateRoom(@Param("rId") Integer rId,
                   @Param("room") Room room);

    /**
     * Get all reservations for a user
     *
     * @param userId id of user to search reservation for
     * @return List of reservations
     */
    @Query("select r from Reservation r where r.user.id = :userId")
    List<Reservation> findAllByUser(@Param("userId") Integer userId);
}
