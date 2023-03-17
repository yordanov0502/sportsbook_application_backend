package com.example.sportsbook_application_backend.exception;

public class DuplicateEmailException extends RuntimeException{
    public DuplicateEmailException(String message) {super(message);}
}