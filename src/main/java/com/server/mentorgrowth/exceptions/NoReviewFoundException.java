package com.server.mentorgrowth.exceptions;

public class NoReviewFoundException extends RuntimeException {
    public NoReviewFoundException(String message) {
        super(message);
    }
}
