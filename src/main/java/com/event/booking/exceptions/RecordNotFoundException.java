package com.event.booking.exceptions;

public class RecordNotFoundException extends RuntimeException {
    private String message;

    public RecordNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
