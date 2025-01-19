package com.objective_platform.auth.application.services;

public interface PasswordHasher {
    String hash(String password);

    boolean match(String plainPassword, String hashedPassword);
}
