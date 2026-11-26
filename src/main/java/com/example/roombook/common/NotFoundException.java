package com.example.roombook.common;

/**
 * The entity the actor refers to does not exist.
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
