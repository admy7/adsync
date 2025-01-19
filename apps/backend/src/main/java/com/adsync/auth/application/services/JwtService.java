package com.adsync.auth.application.services;

import com.adsync.auth.domain.models.AuthUser;
import com.adsync.auth.domain.models.User;

public interface JwtService {
    String tokenize(User user);

    AuthUser parse(String token);
}
