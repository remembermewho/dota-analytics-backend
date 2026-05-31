package com.dota2.dota2dataperser.exception;

public class ParsingException extends RuntimeException {

    public ParsingException(String message) {
        super(message);
    }

    public ParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}