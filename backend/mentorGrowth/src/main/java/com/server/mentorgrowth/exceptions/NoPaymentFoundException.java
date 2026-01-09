package com.server.mentorgrowth.exceptions;

public class NoPaymentFoundException extends RuntimeException {
    public NoPaymentFoundException(String message) {
        super(message);
    }
}
