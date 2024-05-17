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

/**
 * Rest controller for managing hotel-related operations.
 */
@RestController
public class HotelController {
    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    /**
     * GET API for all hotels
     *
     * @return ResponseEntity containing all hotels
     */
    @GetMapping("/hotels")
    public ResponseEntity<List<Hotel>> getHotels() {
        return ResponseEntity.accepted().body(hotelService.getAll());
    }

    /**
     * GET API for hotel by id field
     *
     * @param id The id of the hotel to retrieve
     * @return HotelResponse that contains the hotel resource
     * or an error message in case of there is no hotel with given id
     */
    @GetMapping("/hotel/{id}")
    public HotelResponse getHotelById(@PathVariable Integer id) {
        Optional<Hotel> hotelOptional = hotelService.getById(id);
        return hotelOptional.map(hotel -> new HotelResponse(hotel, null))
                .orElseGet(() -> new HotelResponse(null, "There does not exist a hotel with given id"));
    }

    /**
     * GET API for hotels placed in a specific geographical space
     *
     * @param latitude  Latitude coordinate of the center of geographical space to be searched
     * @param longitude Longitude coordinate of the center of geographical space to be searched
     * @param radius    The radius of the geographical space to be searched
     * @return ResponseEntity containing all hotels in range
     */
    @GetMapping("/hotels/{latitude}/{longitude}/{radius}")
    public ResponseEntity<List<Hotel>> getHotelsInRange(@PathVariable double latitude, @PathVariable double longitude, @PathVariable double radius) {
        return ResponseEntity.accepted().body(hotelService.getAllInRange(latitude, longitude, radius));
    }
}