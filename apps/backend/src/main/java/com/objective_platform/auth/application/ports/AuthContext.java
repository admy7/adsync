package com.objective_platform.auth.application.ports;

import com.objective_platform.auth.domain.models.AuthUser;

import java.util.Optional;

public interface AuthContext {
    boolean isAuthenticated();
    Optional<AuthUser> getUser();
}
