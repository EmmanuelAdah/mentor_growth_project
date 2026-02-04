package com.server.mentorgrowth.exceptions;

public class InvalidUserIdentityException extends RuntimeException {
    public InvalidUserIdentityException(String message) {
        super(message);
    }
}
