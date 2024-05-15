package com.reservation.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reservation.domain.Hotel;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Repository
public class HotelRepository {
    private static final String FILE_PATH = "data.json";
    private ObjectMapper objectMapper = new ObjectMapper();

    public List<Hotel> findAll() throws IOException {
        return objectMapper.readValue(new File(FILE_PATH), new TypeReference<>() {
        });
    }

    public Optional<Hotel> findById(Integer id) throws IOException {
        List<Hotel> allHotels = objectMapper.readValue(new File(FILE_PATH), new TypeReference<>() {
        });
        return allHotels.stream().filter(hotel -> hotel.getId().equals(id)).findFirst();
    }
}
