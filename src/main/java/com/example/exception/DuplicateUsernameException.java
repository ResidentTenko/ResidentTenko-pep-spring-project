package com.example.exception;

/**
 * This custom exception is thrown when a duplicate username is encountered.
 */
public class DuplicateUsernameException extends RuntimeException {
    public DuplicateUsernameException(String message) {
        super(message);
    }
}