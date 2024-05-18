package com.reservation.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Class for storing rooms-related information
 */
@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
public class Room implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Integer id;

    @Column(name = "room_number")
    @JsonProperty("roomNumber")
    private String roomNumber;

    @JsonProperty("type")
    private Integer type;

    @JsonProperty("price")
    private Float price;

    @Column(name = "is_available")
    @JsonProperty("isAvailable")
    private boolean isAvailable;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    @JsonBackReference
    @JsonProperty("hotel")
    private Hotel hotel;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Room(@JsonProperty("roomNumber") String roomNumber, @JsonProperty("type") Integer type,
                @JsonProperty("price") Float price, @JsonProperty("isAvailable") boolean isAvailable) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.price = price;
        this.isAvailable = isAvailable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return isAvailable == room.isAvailable && Objects.equals(id, room.id) && Objects.equals(roomNumber, room.roomNumber) && Objects.equals(type, room.type) && Objects.equals(price, room.price) && Objects.equals(hotel, room.hotel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roomNumber, type, price, isAvailable, hotel);
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", roomNumber='" + roomNumber + '\'' +
                ", type=" + type +
                ", price=" + price +
                ", isAvailable=" + isAvailable +
                ", hotel=" + hotel +
                '}';
    }
}
