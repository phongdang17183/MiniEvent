package com.example.MiniEvent.adapter.web.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseApiException extends RuntimeException {
    private final HttpStatus status;

    public BaseApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public BaseApiException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.status = status;
    }
}

