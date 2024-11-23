package com.suraj.careercraft.exceptions;

public class UserPersistenceException extends RuntimeException {
    public UserPersistenceException() {
    }
    public UserPersistenceException(String message) {
        super(message);
    }

    public UserPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
