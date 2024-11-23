package com.suraj.careercraft.exceptions;

public class EmployerProfilePersistenceException extends RuntimeException {
    public EmployerProfilePersistenceException() {
    }

    public EmployerProfilePersistenceException(String message) {
        super(message);
    }

    public EmployerProfilePersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
