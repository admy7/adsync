package com.adsync.auth.application.services;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PasswordHasherTest {
    private final PasswordHasher hasher = new BCryptPasswordHasher();

    @Test
    void hashPassword() {
        String clearPassword = "test_1234";
        String hashedPassword = hasher.hash(clearPassword);

        assertThat(hasher.match(clearPassword, hashedPassword)).isTrue();
    }
}
