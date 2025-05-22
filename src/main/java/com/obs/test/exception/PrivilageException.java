package com.obs.test.exception;

public class PrivilageException extends RuntimeException{
    private String message;

    public PrivilageException(String message) {
        super(message);
        this.message = message;
    }

    public PrivilageException() {
    }
}
