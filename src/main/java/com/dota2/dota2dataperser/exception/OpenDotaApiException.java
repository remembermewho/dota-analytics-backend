package com.dota2.dota2dataperser.exception;

public class OpenDotaApiException extends RuntimeException {

    public OpenDotaApiException(String message) {
        super(message);
    }

    public OpenDotaApiException(String message, Throwable cause) {
        super(message, cause);
    }
}