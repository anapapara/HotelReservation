package com.reservation.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Class for storing hotel-related information
 */
@Entity
@Table(name = "hotels")
@Getter
@Setter
@NoArgsConstructor
public class Hotel implements Serializable {
    @Id
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("latitude")
    private String latitude;

    @JsonProperty("longitude")
    private String longitude;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @JsonProperty("rooms")
    private List<Room> rooms;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Hotel(@JsonProperty("name") String name,
                 @JsonProperty("latitude") String latitude, @JsonProperty("longitude") String longitude,
                 @JsonProperty("rooms") List<Room> rooms) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rooms = rooms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hotel hotel = (Hotel) o;
        return Objects.equals(id, hotel.id) && Objects.equals(name, hotel.name) && Objects.equals(latitude, hotel.latitude) && Objects.equals(longitude, hotel.longitude) && Objects.equals(rooms, hotel.rooms);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, latitude, longitude, rooms);
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", rooms=" + rooms +
                '}';
    }
}
