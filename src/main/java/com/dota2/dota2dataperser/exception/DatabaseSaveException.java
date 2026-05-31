package com.dota2.dota2dataperser.exception;

public class DatabaseSaveException extends RuntimeException {

    public DatabaseSaveException(String message) {
        super(message);
    }

    public DatabaseSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}