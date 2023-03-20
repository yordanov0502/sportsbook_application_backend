package com.example.sportsbook_application_backend.exception;

public class DuplicatePasswordException extends RuntimeException{
    public DuplicatePasswordException(String message) {super(message);}
}