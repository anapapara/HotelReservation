package com.reservation.controller;

import com.reservation.domain.Hotel;
import com.reservation.domain.response.HotelResponse;
import com.reservation.service.HotelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class HotelController {
    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }


    @GetMapping("/hotels")
    public ResponseEntity<List<Hotel>> getHotels() {
        return ResponseEntity.accepted().body(hotelService.getAll());
    }

    @GetMapping("/hotel/{id}")
    public HotelResponse getHotelById(@PathVariable Integer id) {
        Optional<Hotel> hotelOptional = hotelService.getById(id);
        return hotelOptional.map(hotel -> new HotelResponse(hotel, null))
                .orElseGet(() -> new HotelResponse(null, "There does not exist a hotel with given id"));
    }

    @GetMapping("/hotels/{latitude}/{longitude}/{radius}")
    public ResponseEntity<List<Hotel>> getHotelsInRange(@PathVariable double latitude, @PathVariable double longitude, @PathVariable double radius) {
        return ResponseEntity.accepted().body(hotelService.getAllInRange(latitude, longitude, radius));
    }
}