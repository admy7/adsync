package com.objective_platform.auth.domain.exceptions;

public class EmailAlreadyTakenException extends RuntimeException{
    public EmailAlreadyTakenException(String email) {
        super("Email " + email + " is already taken");
    }
}
