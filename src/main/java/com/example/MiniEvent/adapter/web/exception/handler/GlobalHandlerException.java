package com.example.MiniEvent.adapter.web.exception.handler;

import com.example.MiniEvent.adapter.web.exception.BaseApiException;
import com.example.MiniEvent.adapter.web.exception.NotAllowAccessException;
import com.example.MiniEvent.adapter.web.response.ResponseObject;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalHandlerException {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseObject> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ResponseObject.builder()
                        .message("System Error: " + ex.getMessage())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .data(null)
                        .build()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseObject> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ResponseObject.builder()
                        .message("Data not valid: " + ex.getMessage())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .data(null)
                        .build()
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseObject> handleConstraintViolation(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .findFirst()
                .orElse("Invalid input");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ResponseObject.builder()
                        .message(message)
                        .status(HttpStatus.BAD_REQUEST.value())
                        .data(null)
                        .build()
        );
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseObject> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Invalid input");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ResponseObject.builder()
                        .message(message)
                        .status(400)
                        .data(null)
                        .build()
        );
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity<ResponseObject> handleHttpMessageConversionException(HttpMessageConversionException ex) {
        return ResponseEntity.badRequest().body(
                ResponseObject.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message("Invalid request body: " + ex.getMessage())
                        .data(null)
                        .build()
        );
    }

    @ExceptionHandler(BaseApiException.class)
    public ResponseEntity<?> handleBaseApiException(BaseApiException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(ResponseObject.builder()
                        .status(ex.getStatus().value())
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(NotAllowAccessException.class)
    public ResponseEntity<ResponseObject> handleNotAllowAccessException(NotAllowAccessException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                ResponseObject.builder()
                        .message("Not allow access data: " + ex.getMessage())
                        .status(HttpStatus.FORBIDDEN.value())
                        .data(ex.getMessage())
                        .build()
        );
    }

}
