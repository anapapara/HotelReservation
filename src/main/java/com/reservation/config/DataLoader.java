package com.reservation.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reservation.domain.Hotel;
import com.reservation.repository.HotelRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

/**
 * Class for loading initial data from JSON and populate database.
 */
@Component
public class DataLoader implements ApplicationRunner {
    private final HotelRepository hotelRepository;
    private static final String FILE_NAME = "data.json";

    public DataLoader(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (hotelRepository.count() == 0) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                List<Hotel> hotels = mapper.readValue(new File(FILE_NAME), new TypeReference<>() {
                });
                hotelRepository.saveAll(hotels);
                System.out.println("Database has been populated.");
            } catch (Exception e) {
                System.err.println("Unable to populate database: " + e.getMessage());
            }
        }
    }
}