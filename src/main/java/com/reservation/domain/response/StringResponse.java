package com.reservation.domain.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Objects;

/**
 * Class for API responses containing a String
 */
public class StringResponse implements Serializable {
    @JsonProperty("body")
    private String body;
    @JsonProperty("error")
    private String error;
    @JsonProperty("status")
    private HttpStatus status;

    private StringResponse() {
    }

    /**
     * Constructor that creates a response with body, empty error and OK status
     * or with empty body, error message and ACCEPTED status
     *
     * @param body  The body of response - String
     * @param error The error message in case of error
     */
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public StringResponse(@JsonProperty("body") String body, @JsonProperty("error") String error) {
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
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
        StringResponse response = (StringResponse) o;
        return Objects.equals(body, response.body) && Objects.equals(error, response.error) && status == response.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(body, error, status);
    }
}
