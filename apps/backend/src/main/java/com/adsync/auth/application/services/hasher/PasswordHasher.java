package com.adsync.auth.application.services.hasher;

public interface PasswordHasher {
  String hash(String password);

  boolean match(String plainPassword, String hashedPassword);
}
