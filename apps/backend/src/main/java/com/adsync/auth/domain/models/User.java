package com.adsync.auth.domain.models;

public class User {

  private final String id;
  private final String email;
  private final String password;

  public User(String id, String email, String password) {
    this.id = id;
    this.email = email;
    this.password = password;
  }

  public String id() {
    return id;
  }

  public String email() {
    return email;
  }

  public String password() {
    return password;
  }

  public User deepClone() {
    return new User(id, email, password);
  }
}
