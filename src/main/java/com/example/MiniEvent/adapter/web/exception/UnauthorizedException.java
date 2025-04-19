package com.example.MiniEvent.adapter.web.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends BaseApiException {
    public UnauthorizedException(String message, HttpStatus status) {
        super(message, status);
    }

    public UnauthorizedException(String message, HttpStatus status, Throwable cause) {
        super(message, status, cause);
    }
}
