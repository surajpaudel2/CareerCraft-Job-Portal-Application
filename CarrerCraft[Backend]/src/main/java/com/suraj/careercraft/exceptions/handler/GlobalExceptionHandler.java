package com.suraj.careercraft.exceptions.handler;

import com.suraj.careercraft.dto.response.ErrorResponseDto;
import com.suraj.careercraft.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponseDto handleUserNotFoundException(UserNotFoundException ex) {
        return new ErrorResponseDto("USER_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(UserPersistenceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponseDto handleUserPersistenceException(UserPersistenceException ex) {
        return new ErrorResponseDto("USER_PERSISTENCE_ERROR", ex.getMessage());
    }

    @ExceptionHandler(EmployerProfilePersistenceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponseDto handleEmployerProfilePersistenceException(EmployerProfilePersistenceException ex) {
        return new ErrorResponseDto("EMPLOYER_PROFILE_PERSISTENCE_ERROR", ex.getMessage());
    }

    @ExceptionHandler(EmployerProfileNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponseDto handleEmployerProfileNotFoundException(EmployerProfileNotFoundException ex) {
        return new ErrorResponseDto("EMPLOYER_PROFILE_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(NoJobFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponseDto handleNoJobFoundException(NoJobFoundException ex) {
        return new ErrorResponseDto("REQUESTED_JOB_NOT_FOUND", ex.getMessage());
    }
}
