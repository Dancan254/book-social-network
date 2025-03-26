package com.mongs.book_social_network.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OperationNotPermitted.class)
    public ResponseEntity<ExceptionResponse> handleOperationNotPermitted(OperationNotPermitted e) {
        return ResponseEntity.status(BAD_REQUEST)
                .body(ExceptionResponse.builder()
                        .errorBusinessDescription(e.getMessage())
                .build());
    }
}
