package com.ifpe.userApi.exceptions;

public class InvalidDateFormatException extends RuntimeException {
    public InvalidDateFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
