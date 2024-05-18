package com.reservation.exception;

/**
 * Custom class for exceptions
 */
public class ReservationException extends Exception {
    private final String message;

    public ReservationException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}