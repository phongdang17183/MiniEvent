package com.example.MiniEvent.adapter.web.exception;

import lombok.Getter;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;

@Getter
public class NotAllowAccessException extends DataAccessException {
    private final HttpStatus status;

    public NotAllowAccessException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
