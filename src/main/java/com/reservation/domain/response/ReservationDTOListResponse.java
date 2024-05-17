package com.reservation.domain.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.reservation.domain.dto.ReservationDTO;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class ReservationDTOListResponse implements Serializable {
    @JsonProperty("body")
    private List<ReservationDTO> body;
    @JsonProperty("error")
    private String error;
    @JsonProperty("status")
    private HttpStatus status;

    private ReservationDTOListResponse() {
    }

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ReservationDTOListResponse(@JsonProperty("body") List<ReservationDTO> body, @JsonProperty("error") String error) {
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

    public List<ReservationDTO> getBody() {
        return body;
    }

    public void setBody(List<ReservationDTO> body) {
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
    public int hashCode() {
        return Objects.hash(body, error, status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationDTOListResponse that = (ReservationDTOListResponse) o;
        return Objects.equals(body, that.body) && Objects.equals(error, that.error) && status == that.status;
    }
}
