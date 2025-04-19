package com.example.MiniEvent.web.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseApiException {
    public BadRequestException(String message, HttpStatus status, Throwable cause) {
        super(message, status, cause);
    }

    public BadRequestException(String message, HttpStatus status) {
        super(message, status);
    }
}
