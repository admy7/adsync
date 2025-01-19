package com.objective_platform.auth.domain.models;

public class AuthUser {
    private String id;
    private String email;

    public AuthUser(String id, String email) {
        this.id = id;
        this.email = email;
    }

    public String id() {
        return id;
    }

    public String email() {
        return email;
    }
}
