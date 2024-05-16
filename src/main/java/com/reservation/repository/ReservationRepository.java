package com.reservation.repository;

import com.reservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    @Override
    List<Reservation> findAll();

    @Override
    <S extends Reservation> S save(S entity);
//@Query("select r from Reservation r where r.hotel.id = :hotelId and r.room.id = :roomId and r.room.isAvailable = true")
//List<Reservation> findReservations(@Param("hotelId") Long hotelId, @Param("roomId") Long roomId);

    @Query("select r from Reservation r where r.hotel.id = :hotelId and r.room.id = :roomId")
    List<Reservation> findByHotelRoom(@Param("hotelId") Integer hotelId,
                                      @Param("roomId") Integer roomId);
}
