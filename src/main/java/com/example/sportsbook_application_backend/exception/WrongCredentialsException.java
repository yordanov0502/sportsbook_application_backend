package com.example.sportsbook_application_backend.exception;

public class WrongCredentialsException extends RuntimeException {
    public WrongCredentialsException(String message) {super(message);}
}