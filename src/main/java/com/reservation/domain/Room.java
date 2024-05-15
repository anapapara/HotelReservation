package com.reservation.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Room implements Serializable {
    @JsonProperty("roomNumber")
    private String roomNumber;
    @JsonProperty("type")
    private Integer type;
    @JsonProperty("price")
    private Float price;
    @JsonProperty("isAvailable")
    private boolean isAvailable;

    public Room() {
    }

    public Room(String roomNumber, Integer type, Float price, boolean isAvailable) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.price = price;
        this.isAvailable = isAvailable;
    }
}
