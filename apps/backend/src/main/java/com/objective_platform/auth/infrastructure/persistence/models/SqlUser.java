package com.objective_platform.auth.infrastructure.persistence.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class SqlUser {
    @Id
    private String id;

    private String email;

    private String password;

    public SqlUser() {
    }

    public SqlUser(String id, String email, String password) {
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
}
