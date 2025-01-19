package com.adsync.auth.application.services;

import com.adsync.auth.application.services.jwt.JwtService;
import com.adsync.auth.application.services.jwt.JwtServiceImpl;
import com.adsync.auth.domain.models.AuthUser;
import com.adsync.auth.domain.models.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtServiceTest {

    @Test
    void tokenizeUser() {
        JwtService service = new JwtServiceImpl("secret-key-for-jwt-test-very-long", 60);

        var user = new User("1", "user@gmail.com", "password");

        String token = service.tokenize(user);
        AuthUser authUser = service.parse(token);

        assertThat(authUser.id()).isEqualTo(user.id());
        assertThat(authUser.email()).isEqualTo(user.email());
    }
}
