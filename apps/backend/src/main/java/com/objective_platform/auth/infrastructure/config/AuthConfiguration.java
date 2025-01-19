package com.objective_platform.auth.infrastructure.config;

import com.objective_platform.auth.application.ports.UserRepository;
import com.objective_platform.auth.application.services.JwtService;
import com.objective_platform.auth.application.services.JwtServiceImpl;
import com.objective_platform.auth.application.usecases.CreateUserCommandHandler;
import com.objective_platform.auth.infrastructure.persistence.SqlUserRepository;
import com.objective_platform.auth.application.services.BCryptPasswordHasher;
import com.objective_platform.auth.application.services.PasswordHasher;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfiguration {

    @Bean
    public UserRepository userRepository(EntityManager entityManager) {
        return new SqlUserRepository(entityManager);
    }

    @Bean
    public PasswordHasher passwordHasher() {
        return new BCryptPasswordHasher();
    }

    @Bean
    public JwtService jwtService(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") int expiration) {
        return new JwtServiceImpl(secret, expiration);
    }

    @Bean
    public CreateUserCommandHandler createUserCommandHandler(UserRepository userRepository, PasswordHasher passwordHasher) {
        return new CreateUserCommandHandler(userRepository, passwordHasher);
    }
}
