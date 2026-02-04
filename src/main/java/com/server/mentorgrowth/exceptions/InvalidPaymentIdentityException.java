package com.server.mentorgrowth.exceptions;

public class InvalidPaymentIdentityException extends RuntimeException {
    public InvalidPaymentIdentityException(String message) {
        super(message);
    }
}
