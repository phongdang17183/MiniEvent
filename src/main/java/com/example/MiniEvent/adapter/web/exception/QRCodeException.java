package com.example.MiniEvent.adapter.web.exception;

import org.springframework.http.HttpStatus;

public class QRCodeException extends BaseApiException{

    public QRCodeException(String message, HttpStatus status) {
        super(message, status);
    }

    public QRCodeException(String message, HttpStatus status, Throwable cause) {
        super(message, status, cause);
    }
}
