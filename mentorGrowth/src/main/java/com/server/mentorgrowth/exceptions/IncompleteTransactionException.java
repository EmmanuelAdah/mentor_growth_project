package com.server.mentorgrowth.exceptions;

public class IncompleteTransactionException extends RuntimeException {
    public IncompleteTransactionException(String message) {
        super(message);
    }
}
