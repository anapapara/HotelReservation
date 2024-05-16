package com.reservation.controller;

import com.reservation.domain.Hotel;
import com.reservation.service.HotelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

@RestController
public class HotelController {
    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }


    @GetMapping("/hotels")
    public ResponseEntity getHotels() {
        return ResponseEntity.accepted().body(hotelService.getAll());
    }

    @GetMapping("/hotel/{id}")
    public ResponseEntity getHotelById(@PathVariable Integer id) {
        Optional<Hotel> hotelOptional = hotelService.getById(id);
        if (hotelOptional.isPresent())
            return ResponseEntity.accepted().body(hotelOptional.get());
        else {
            return ResponseEntity.internalServerError().body("There does not exist a hotel with given id");
        }
    }

    @GetMapping("/hotels/{latitude}/{longitude}/{radius}")
    public ResponseEntity getHotelsInRange(@PathVariable double latitude, @PathVariable double longitude, @PathVariable double radius) {
        return ResponseEntity.accepted().body(hotelService.getAllInRange(latitude, longitude, radius));
    }
}