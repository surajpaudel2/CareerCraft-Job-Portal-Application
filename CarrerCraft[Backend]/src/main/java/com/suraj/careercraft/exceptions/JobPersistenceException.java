package com.suraj.careercraft.exceptions;

public class JobPersistenceException extends RuntimeException {
    public JobPersistenceException() {

    }

    public JobPersistenceException(String message) {
        super(message);
    }

    public JobPersistenceException(String message, Exception e) {
        super(message, e);
    }
}
