package com.example.MiniEvent.adapter.web.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends BaseApiException {

    public ForbiddenException(String message, HttpStatus status) {
        super(message, status);
    }

    public ForbiddenException(String message, HttpStatus status, Throwable cause) {
        super(message, status, cause);
    }
}
