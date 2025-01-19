package com.adsync.auth.application.ports;

import com.adsync.auth.domain.models.AuthUser;
import java.util.Optional;

public interface AuthContext {
  boolean isAuthenticated();

  Optional<AuthUser> getUser();
}
