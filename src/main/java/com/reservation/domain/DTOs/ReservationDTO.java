package com.reservation.domain.DTOs;

import java.io.Serializable;
import java.util.Date;

public class ReservationDTO implements Serializable {
    private Integer userId;
    private Integer hotelId;
    private Integer roomId;
    private Date startDate;
    private Date endDate;

    public ReservationDTO(Integer userId, Integer hotelId, Integer roomId, Date startDate, Date endDate) {
        this.userId = userId;
        this.hotelId = hotelId;
        this.roomId = roomId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getHotelId() {
        return hotelId;
    }

    public void setHotelId(Integer hotelId) {
        this.hotelId = hotelId;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
