package com.example.sportsbook_application_backend.exception;

import org.springframework.security.core.AuthenticationException;

public class FieldException extends AuthenticationException {
    public FieldException(String message) {super(message);}
}