package com.reservation.controller;

import com.reservation.domain.Room;
import com.reservation.service.RoomService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Objects;

@ExtendWith(MockitoExtension.class)
public class RoomControllerTest {
    @Mock
    private RoomService roomService;
    @InjectMocks
    private RoomController roomController;

    @BeforeAll
    public static void beforeAll() {
        MockitoAnnotations.openMocks(RoomControllerTest.class);
    }

    @Test
    void getAllByHotelIdTest() {
        Room room = new Room("20", 1, 230f, true);

        Mockito.when(roomService.findAvailableByHotelId(7))
                .thenReturn(List.of(room));

        List<Room> response = roomController.getAllByHotelId(7).getBody();

        Mockito.verify(this.roomService, Mockito.times(1)).findAvailableByHotelId(7);
        assert (Objects.requireNonNull(response).get(0).equals(room));
    }


}
