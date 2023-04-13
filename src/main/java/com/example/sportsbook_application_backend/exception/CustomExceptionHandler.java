package com.example.sportsbook_application_backend.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler
    public ProblemDetail handleException(FieldException fieldException){
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, fieldException.getMessage());
    }

    @ExceptionHandler
    public ProblemDetail handleException(DuplicateUsernameException duplicateUsernameException){
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, duplicateUsernameException.getMessage());
    }

    @ExceptionHandler
    public ProblemDetail handleException(DuplicateEmailException duplicateEmailException){
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, duplicateEmailException.getMessage());
    }

    @ExceptionHandler
    public ProblemDetail handleException(UpdateException updateException){
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, updateException.getMessage());
    }

    @ExceptionHandler
    public ProblemDetail handleException(WrongCredentialsException wrongCredentialsException){
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, wrongCredentialsException.getMessage());
    }

    @ExceptionHandler
    public ProblemDetail handleException(DuplicatePasswordException duplicatePasswordException){
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, duplicatePasswordException.getMessage());
    }

    @ExceptionHandler
    public ProblemDetail handleException(NonexistentDataException nonexistentDataException){
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, nonexistentDataException.getMessage());
    }

    @ExceptionHandler
    public ProblemDetail handleException(UserStatusException userStatusException){
        return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, userStatusException.getMessage());
    }

    @ExceptionHandler
    public ProblemDetail handleException(BadCredentialsException badCredentialsException){
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, badCredentialsException.getMessage());
    }

    @ExceptionHandler
    public ProblemDetail handleException(ConstraintViolationException constraintViolationException){
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, constraintViolationException.getMessage());
    }

    @ExceptionHandler
    public ProblemDetail handleException(ProfileMismatchException profileMismatchException){
        return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, profileMismatchException.getMessage());
    }
}