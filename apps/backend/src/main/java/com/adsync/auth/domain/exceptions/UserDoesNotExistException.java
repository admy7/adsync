package com.adsync.auth.domain.exceptions;

public class UserDoesNotExistException extends RuntimeException {
    public UserDoesNotExistException(String email) {
        super("User with email %s does not exist".formatted(email));
    }
}
