package com.popcorn.jrp.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {
    private final HttpStatus status;
    private final String message;
    private final String details;

    public CustomException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
        this.details = null;
    }

    public CustomException(HttpStatus status, String message, String details) {
        super(message);
        this.status = status;
        this.message = message;
        this.details = details;
    }
}