package com.objective_platform.auth.services;

public interface PasswordHasher {
    String hash(String password);

    boolean match(String clearPassword, String hashedPassword);
}
