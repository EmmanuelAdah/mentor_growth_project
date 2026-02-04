package com.server.mentorgrowth.exceptions;

public class ServerConnectionException extends RuntimeException {
    public ServerConnectionException(String message) {
        super(message);
    }
}
