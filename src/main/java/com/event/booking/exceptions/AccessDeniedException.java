package com.event.booking.exceptions;

public class AccessDeniedException extends RuntimeException {
    private String message;

    public AccessDeniedException(String message) {
        super(message);
        this.message = message;
    }
}
