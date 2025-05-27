package com.example.demo.exception;

public class InitDataExpiredException extends RuntimeException {
    public InitDataExpiredException(String message) {
        super(message);
    }
}
