package com.reservation.controller;

import com.reservation.domain.Hotel;
import com.reservation.service.HotelService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class HotelControllerTest {
    @Mock
    private HotelService hotelService;
    @InjectMocks
    private HotelController hotelController;

    @BeforeAll
    public static void beforeAll() {
        MockitoAnnotations.openMocks(HotelControllerTest.class);
    }

    @Test
    public void getHotelsTest() {
        Hotel hotel1 = new Hotel("hotel1", "latitude1", "longitude1", new ArrayList<>());
        Hotel hotel2 = new Hotel("hotel2", "latitude2", "longitude2", new ArrayList<>());
        Hotel hotel3 = new Hotel("hotel3", "latitude3", "longitude3", new ArrayList<>());

        List<Hotel> hotels = Arrays.asList(hotel1, hotel2, hotel3);

        Mockito.when(hotelService.getAll())
                .thenReturn(hotels);

        hotelController.getHotels();

        Mockito.verify(this.hotelService, Mockito.times(1)).getAll();

    }

    @Test
    void getHotelByIdTest() {
        Hotel hotel1 = new Hotel("hotel1", "latitude1", "longitude1", new ArrayList<>());

        Mockito.when(hotelService.getById(1))
                .thenReturn(Optional.of(hotel1));

        hotelController.getHotelById(1);

        Mockito.verify(this.hotelService, Mockito.times(1)).getById(1);
    }

    @Test
    void getHotelsIInRange() {
        Hotel hotel1 = new Hotel("hotel1", "latitude1", "longitude1", new ArrayList<>());

        Mockito.when(hotelService.getAllInRange(1d, 1d, 2))
                .thenReturn(List.of(hotel1));

        hotelController.getHotelsInRange(1, 1, 2);

        Mockito.verify(this.hotelService, Mockito.times(1)).getAllInRange(1, 1, 2);
    }


}
