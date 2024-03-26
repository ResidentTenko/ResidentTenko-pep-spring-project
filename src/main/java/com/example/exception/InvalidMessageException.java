package com.example.exception;

/**
 * This custom exception is thrown when an invalid message is encountered.
 */
public class InvalidMessageException extends RuntimeException {
    public InvalidMessageException(String message) {
        super(message);
    }
}