package com.server.mentorgrowth.exceptions;

public class InvalidPaymentReferenceException extends RuntimeException {
    public InvalidPaymentReferenceException(String message) {
        super(message);
    }
}
