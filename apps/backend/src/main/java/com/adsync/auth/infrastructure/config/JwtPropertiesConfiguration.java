package com.adsync.auth.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtPropertiesConfiguration {

  @Value("${jwt.secret-key}")
  private String secretKey;

  @Value("${jwt.expiration}")
  private int expiration;

  public String secretKey() {
    return secretKey;
  }

  public int expiration() {
    return expiration;
  }
}
