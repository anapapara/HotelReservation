package com.reservation.domain.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.reservation.domain.User;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Objects;

/**
 * Class for API responses containing User entity
 */
public class UserResponse implements Serializable {
    @JsonProperty("body")
    private User body;
    @JsonProperty("error")
    private String error;
    @JsonProperty("status")
    private HttpStatus status;

    private UserResponse() {
    }

    /**
     * Constructor that creates a response with body, empty error and OK status
     * or with empty body, error message and ACCEPTED status
     *
     * @param body  The body of response - User entity
     * @param error The error message in case of error
     */
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public UserResponse(@JsonProperty("body") User body, @JsonProperty("error") String error) {
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

    public User getBody() {
        return body;
    }

    public void setBody(User body) {
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
        UserResponse that = (UserResponse) o;
        return Objects.equals(body, that.body) && Objects.equals(error, that.error) && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(body, error, status);
    }
}
