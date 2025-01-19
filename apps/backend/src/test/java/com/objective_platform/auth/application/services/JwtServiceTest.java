package com.objective_platform.auth.application.services;

import com.objective_platform.auth.domain.models.AuthUser;
import com.objective_platform.auth.domain.models.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtServiceTest {

    @Test
    void tokenizeUser() {
        JwtService service = new JwtServiceImpl("secret-key-for-jwt-test-very-long", 60);

        User user = new User("1", "user@gmail.com", "password");

        String token = service.tokenize(user);
        AuthUser authUser = service.parse(token);

        assertThat(authUser.id()).isEqualTo(user.id());
        assertThat(authUser.email()).isEqualTo(user.email());
    }
}
