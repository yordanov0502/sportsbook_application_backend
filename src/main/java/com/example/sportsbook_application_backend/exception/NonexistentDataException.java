package com.example.sportsbook_application_backend.exception;

public class NonexistentDataException extends RuntimeException{
    public NonexistentDataException(String message){super(message);}
}