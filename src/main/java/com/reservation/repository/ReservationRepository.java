package com.reservation.repository;

import com.reservation.domain.Reservation;
import com.reservation.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    @Override
    List<Reservation> findAll();

    @Override
    <S extends Reservation> S save(S entity);

    @Query("select r from Reservation r where r.hotel.id = :hotelId and r.room.id = :roomId")
    List<Reservation> findByHotelRoom(@Param("hotelId") Integer hotelId,
                                      @Param("roomId") Integer roomId);

    @Override
    void deleteById(Integer integer);

    @Override
    Optional<Reservation> findById(Integer integer);

}
