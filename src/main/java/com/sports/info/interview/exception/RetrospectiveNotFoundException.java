package com.sports.info.interview.exception;

public class RetrospectiveNotFoundException  extends RuntimeException {

    public RetrospectiveNotFoundException(String message) {
        super(message);
    }

    public RetrospectiveNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}