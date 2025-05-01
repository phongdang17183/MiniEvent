package com.example.MiniEvent.adapter.web.exception;

import org.springframework.http.HttpStatus;

public class EmailException extends BaseApiException{

    public EmailException(String message, HttpStatus status) {
        super(message, status);
    }

    public EmailException(String message, HttpStatus status, Throwable cause) {
        super(message, status, cause);
    }

}
