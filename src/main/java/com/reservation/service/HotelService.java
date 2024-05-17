package com.reservation.service;

import com.reservation.domain.Hotel;
import com.reservation.repository.HotelRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Class for managing hotel-related logic
 */
@Service
public class HotelService {
    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public List<Hotel> getAll() {
        return hotelRepository.findAll();
    }

    public Optional<Hotel> getById(Integer id) {
        return hotelRepository.findById(id);
    }

    /**
     * Find all hotels in a range
     *
     * @param userLatitude   User latitude coordinate
     * @param userLongitude  User longitude coordinate
     * @param radiusInMeters Radius measured in meters of the range to search hotels
     * @return List of hotel in the given range
     */
    public List<Hotel> getAllInRange(double userLatitude, double userLongitude, double radiusInMeters) {
        List<Hotel> allHotels = hotelRepository.findAll();
        List<Hotel> hotelsInRange = new ArrayList<>();
        for (Hotel hotel : allHotels) {
            double hotelLatitude = Double.parseDouble(hotel.getLatitude());
            double hotelLongitude = Double.parseDouble(hotel.getLongitude());

            double earthRadius = 6371000; //meters
            double dLat = Math.toRadians(userLatitude - hotelLatitude);
            double dLng = Math.toRadians(userLongitude - hotelLongitude);
            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                    Math.cos(Math.toRadians(userLatitude)) * Math.cos(Math.toRadians(hotelLatitude)) *
                            Math.sin(dLng / 2) * Math.sin(dLng / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            float distance = (float) (earthRadius * c);
            if (distance < radiusInMeters)
                hotelsInRange.add(hotel);
        }

        return hotelsInRange;
    }

}
