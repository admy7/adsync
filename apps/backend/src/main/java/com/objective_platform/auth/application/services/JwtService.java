package com.objective_platform.auth.application.services;

import com.objective_platform.auth.domain.models.AuthUser;
import com.objective_platform.auth.domain.models.User;

public interface JwtService {
    String tokenize(User user);

    AuthUser parse(String token);
}
