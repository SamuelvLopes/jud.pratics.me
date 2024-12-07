package com.ifpe.userApi.exceptions;

public class ChatCreationException extends RuntimeException {
    public ChatCreationException(String message) {
        super(message);
    }

    public ChatCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
