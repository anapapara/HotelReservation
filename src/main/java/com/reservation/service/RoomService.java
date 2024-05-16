package com.reservation.service;

import com.reservation.domain.Room;
import com.reservation.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class RoomService {
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> findAvailableByHotelId(Integer hotelId) throws IOException {
        return roomRepository.findAllAvailableByHotelId(hotelId);
    }

}
