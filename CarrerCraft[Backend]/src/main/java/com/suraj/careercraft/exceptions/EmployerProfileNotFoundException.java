package com.suraj.careercraft.exceptions;

public class EmployerProfileNotFoundException extends RuntimeException {
    public EmployerProfileNotFoundException() {}
    public EmployerProfileNotFoundException(String message) {
        super(message);
    }
    public EmployerProfileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
