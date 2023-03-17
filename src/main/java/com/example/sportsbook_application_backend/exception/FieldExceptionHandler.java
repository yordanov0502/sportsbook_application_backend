package com.example.sportsbook_application_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class FieldExceptionHandler {

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
    public ProblemDetail handleException(DatabaseException databaseException){
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, databaseException.getMessage());
    }

    @ExceptionHandler
    public ProblemDetail handleException(AuthenticationException authenticationException){
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, authenticationException.getMessage());
    }

}