package com.example.demo.exception;

public class InvalidHashException extends RuntimeException  {
    public InvalidHashException(String message) {
        super(message);
    }
}
