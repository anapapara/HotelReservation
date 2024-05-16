package com.reservation.controller;

import com.reservation.domain.Hotel;
import com.reservation.service.HotelService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
public class HotelController {
    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }


    @GetMapping("/hotels")
    public List<Hotel> getHotels() {
        try {
            return hotelService.getAll();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/hotel/{id}")
    public Hotel getHotelById(@PathVariable Integer id) {
        try {
            Optional<Hotel> hotelOptional = hotelService.getById(id);
            if (hotelOptional.isPresent())
                return hotelOptional.get();
            else return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //http://localhost:8080/hotels/46.750222/23.606609/2000
    @GetMapping("/hotels/{latitude}/{longitude}/{radius}")
    public List<Hotel> getHotelsInRange(@PathVariable double latitude, @PathVariable double longitude, @PathVariable double radius) {
        try {
            return hotelService.getAllInRange(latitude, longitude, radius);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}