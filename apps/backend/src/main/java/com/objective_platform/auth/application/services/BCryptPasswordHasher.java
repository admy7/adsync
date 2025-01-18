package com.objective_platform.auth.application.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptPasswordHasher implements PasswordHasher {
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String hash(String password) {
        return encoder.encode(password);
    }

    @Override
    public boolean match(String clearPassword, String hashedPassword) {
        return encoder.matches(clearPassword, hashedPassword);
    }
}
