package com.objective_platform.auth.domain.exceptions;

public class IncorrectPasswordException extends RuntimeException {
    public IncorrectPasswordException(String email) {
        super("Incorrect password for user with email %s".formatted(email));
    }
}
