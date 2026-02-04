package com.server.mentorgrowth.exceptions;

public class MentorshipNotFoundException extends RuntimeException {
    public MentorshipNotFoundException(String message) {
        super(message);
    }
}
