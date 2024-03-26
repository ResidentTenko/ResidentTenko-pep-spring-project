package com.example.exception;


/**
 * This custom exception is thrown when invalid auth details are encountered.
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}