package com.example.MiniEvent.adapter.web.exception;

import org.springframework.http.HttpStatus;

public class ImageUploadException extends BaseApiException {

    public ImageUploadException(String message, HttpStatus status) {
        super(message, status);
    }

    public ImageUploadException(String message, HttpStatus status, Throwable cause) {
        super(message, status, cause);
    }
}
