package com.adsync.auth.domain.exceptions;

public class InvalidUserException extends RuntimeException{
    public InvalidUserException() {
        super("Invalid user");
    }
}
