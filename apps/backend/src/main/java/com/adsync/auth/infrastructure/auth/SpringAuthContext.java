package com.adsync.auth.infrastructure.auth;

import com.adsync.auth.application.ports.AuthContext;
import com.adsync.auth.domain.models.AuthUser;
import java.util.Optional;
import org.springframework.security.core.context.SecurityContextHolder;

public class SpringAuthContext implements AuthContext {
  @Override
  public boolean isAuthenticated() {
    return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
  }

  @Override
  public Optional<AuthUser> getUser() {
    return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
        .map(
            authentication ->
                authentication.getPrincipal() instanceof AuthUser
                    ? (AuthUser) authentication.getPrincipal()
                    : null);
  }
}
