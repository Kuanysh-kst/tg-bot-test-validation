package com.example.demo.exception.global;

import com.example.demo.dto.ApiErrorResponse;
import com.example.demo.exception.InitDataExpiredException;
import com.example.demo.exception.InvalidHashException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({InvalidHashException.class, InitDataExpiredException.class})
    public ResponseEntity<ApiErrorResponse> handleSignUpException(RuntimeException exception) {
        ApiErrorResponse response = new ApiErrorResponse(exception.getMessage(), "error", HttpStatus.UNAUTHORIZED.value());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
