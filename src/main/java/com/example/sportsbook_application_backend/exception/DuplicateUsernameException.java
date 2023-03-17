package com.example.sportsbook_application_backend.exception;

public class DuplicateUsernameException extends RuntimeException {
    public DuplicateUsernameException(String message) {super(message);}
}