package com.example.MiniEvent.adapter.web.exception;

import org.springframework.http.HttpStatus;

public class UserCreationException extends BaseApiException {

  public UserCreationException(String message, HttpStatus status) {
    super(message, status);
  }

  public UserCreationException(String message, HttpStatus status, Throwable cause) {
    super(message, status, cause);
  }
}
