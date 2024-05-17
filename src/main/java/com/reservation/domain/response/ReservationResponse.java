package com.reservation.domain.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.reservation.domain.Reservation;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Objects;

public class ReservationResponse implements Serializable {
    @JsonProperty("body")
    private Reservation body;
    @JsonProperty("error")
    private String error;
    @JsonProperty("status")
    private HttpStatus status;

    private ReservationResponse() {
    }

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ReservationResponse(@JsonProperty("body") Reservation body, @JsonProperty("error") String error) {
        if (error == null) {
            this.status = HttpStatus.OK;
            this.body = body;
            this.error = null;
        } else {
            this.error = error;
            this.body = null;
            this.status = HttpStatus.ACCEPTED;
        }
    }

    public Reservation getBody() {
        return body;
    }

    public void setBody(Reservation body) {
        this.body = body;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationResponse response = (ReservationResponse) o;
        return Objects.equals(body, response.body) && Objects.equals(error, response.error) && status == response.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(body, error, status);
    }
}
