package com.example.sportsbook_application_backend.exception;

import org.springframework.security.core.AuthenticationException;

public class ProfileMismatchException extends AuthenticationException {
    public ProfileMismatchException(String message) {super(message);}
}