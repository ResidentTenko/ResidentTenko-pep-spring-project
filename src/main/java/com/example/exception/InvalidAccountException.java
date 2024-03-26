package com.example.exception;

/**
 * This custom exception is thrown when an invalid account is encountered.
 */
public class InvalidAccountException extends RuntimeException {
    public InvalidAccountException(String message) {
        super(message);
    }
}