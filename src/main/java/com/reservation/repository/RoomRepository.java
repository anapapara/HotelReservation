package com.reservation.repository;

import com.reservation.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    Optional<Room> findById(Integer id);

    /**
     * Get all rooms that are available in a hotel
     *
     * @param hotelId id of hotel to search rooms for
     * @return List of rooms
     */
    @Query("select r from Room r where r.hotel.id = :hotelId and r.isAvailable = true")
    List<Room> findAllAvailableByHotelId(@Param("hotelId") Integer hotelId);
}
