package com.objective_platform.auth.infrastructure.auth;

import com.objective_platform.auth.application.ports.AuthContext;
import com.objective_platform.auth.domain.models.AuthUser;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SpringAuthContext implements AuthContext {
    @Override
    public boolean isAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
    }

    @Override
    public Optional<AuthUser> getUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(authentication -> authentication.getPrincipal() instanceof AuthUser ? (AuthUser) authentication.getPrincipal(): null);
    }
}
