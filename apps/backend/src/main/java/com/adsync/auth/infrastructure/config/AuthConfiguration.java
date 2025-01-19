package com.adsync.auth.infrastructure.config;

import com.adsync.auth.application.ports.UserRepository;
import com.adsync.auth.application.services.hasher.BCryptPasswordHasher;
import com.adsync.auth.application.services.jwt.JwtService;
import com.adsync.auth.application.services.jwt.JwtServiceImpl;
import com.adsync.auth.application.services.hasher.PasswordHasher;
import com.adsync.auth.application.usecases.CreateUserCommandHandler;
import com.adsync.auth.application.usecases.LoginCommandHandler;
import com.adsync.auth.infrastructure.persistence.SqlUserRepository;
import jakarta.persistence.EntityManager;
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
    public JwtService jwtService(JwtPropertiesConfiguration jwtProperties) {
        return new JwtServiceImpl(jwtProperties.secretKey(), jwtProperties.expiration());
    }

    @Bean
    public CreateUserCommandHandler createUserCommandHandler(UserRepository userRepository, PasswordHasher passwordHasher) {
        return new CreateUserCommandHandler(userRepository, passwordHasher);
    }

    @Bean
    public LoginCommandHandler loginCommandHandler(UserRepository userRepository, JwtService jwtService, PasswordHasher passwordHasher) {
        return new LoginCommandHandler(userRepository, jwtService, passwordHasher);
    }
}
