package com.reservation.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Class for storing reservation-related information
 */
@Entity
@Table(name = "reservations")
@Getter
@Setter
@NoArgsConstructor
public class Reservation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonProperty("user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    @JsonProperty("hotel")
    private Hotel hotel;

    @ManyToOne
    @JoinColumn(name = "room_id")
    @JsonProperty("room")
    private Room room;

    @JsonProperty("startDate")
    private Date startDate;
    @JsonProperty("endDate")
    private Date endDate;

    @JsonProperty("feedback")
    private String feedback;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Reservation(@JsonProperty("user") User user, @JsonProperty("hotel") Hotel hotel,
                       @JsonProperty("room") Room room, @JsonProperty("startDate") Date startDate,
                       @JsonProperty("endDate") Date endDate) {
        this.user = user;
        this.hotel = hotel;
        this.room = room;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id) && Objects.equals(user, that.user) && Objects.equals(hotel, that.hotel) && Objects.equals(room, that.room) && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate) && Objects.equals(feedback, that.feedback);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, hotel, room, startDate, endDate, feedback);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", user=" + user +
                ", hotel=" + hotel +
                ", room=" + room +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", feedback='" + feedback + '\'' +
                '}';
    }
}
