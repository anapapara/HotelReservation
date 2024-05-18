package com.reservation.domain.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * DTO for Reservation class containing dates and just ids of hotel, user and room
 */
@Getter
@Setter
@NoArgsConstructor
public class ReservationDTO implements Serializable {
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("userId")
    private Integer userId;

    @JsonProperty("hotelId")
    private Integer hotelId;

    @JsonProperty("roomId")
    private Integer roomId;

    @JsonProperty("startDate")
    private Date startDate;

    @JsonProperty("endDate")
    private Date endDate;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ReservationDTO(@JsonProperty("userId") Integer userId, @JsonProperty("hotelId") Integer hotelId,
                          @JsonProperty("roomId") Integer roomId, @JsonProperty("startDate") Date startDate,
                          @JsonProperty("endDate") Date endDate) {
        this.userId = userId;
        this.hotelId = hotelId;
        this.roomId = roomId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationDTO that = (ReservationDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(userId, that.userId) && Objects.equals(hotelId, that.hotelId) && Objects.equals(roomId, that.roomId) && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, hotelId, roomId, startDate, endDate);
    }

    @Override
    public String toString() {
        return "ReservationDTO{" +
                "id=" + id +
                ", userId=" + userId +
                ", hotelId=" + hotelId +
                ", roomId=" + roomId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
