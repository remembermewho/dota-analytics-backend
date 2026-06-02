package com.dota2.dota2dataperser.exception;

public class OpenDotaDailyLimitExceededException extends RuntimeException {

    public OpenDotaDailyLimitExceededException(String message) {
        super(message);
    }
}