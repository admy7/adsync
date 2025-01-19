package com.adsync.auth.application.services;

import static org.assertj.core.api.Assertions.assertThat;

import com.adsync.auth.application.services.hasher.BCryptPasswordHasher;
import com.adsync.auth.application.services.hasher.PasswordHasher;
import org.junit.jupiter.api.Test;

public class PasswordHasherTest {
  private final PasswordHasher hasher = new BCryptPasswordHasher();

  @Test
  void hashPassword() {
    String clearPassword = "test_1234";
    String hashedPassword = hasher.hash(clearPassword);

    assertThat(hasher.match(clearPassword, hashedPassword)).isTrue();
  }
}
