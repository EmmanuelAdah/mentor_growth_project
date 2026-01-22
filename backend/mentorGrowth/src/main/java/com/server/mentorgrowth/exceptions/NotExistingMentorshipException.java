package com.server.mentorgrowth.exceptions;

public class NotExistingMentorshipException extends RuntimeException {
    public NotExistingMentorshipException(String message) {
        super(message);
    }
}
