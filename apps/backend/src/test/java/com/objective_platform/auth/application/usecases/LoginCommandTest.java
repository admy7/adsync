package com.objective_platform.auth.application.usecases;

import com.objective_platform.auth.application.ports.UserRepository;
import com.objective_platform.auth.application.services.BCryptPasswordHasher;
import com.objective_platform.auth.application.services.JwtService;
import com.objective_platform.auth.application.services.JwtServiceImpl;
import com.objective_platform.auth.application.services.PasswordHasher;
import com.objective_platform.auth.domain.exceptions.IncorrectPasswordException;
import com.objective_platform.auth.domain.exceptions.UserDoesNotExistException;
import com.objective_platform.auth.domain.models.AuthUser;
import com.objective_platform.auth.domain.models.User;
import com.objective_platform.auth.domain.viewmodels.TokenResponse;
import com.objective_platform.auth.infrastructure.persistence.InMemoryUserRepository;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LoginCommandTest {

    private final UserRepository userRepository = new InMemoryUserRepository();
    private final JwtService jwtService = new JwtServiceImpl("secret-key-very-long-for-more-security", 60);
    private final PasswordHasher passwordHasher = new BCryptPasswordHasher();

    @Test
    void logUserIn() {
        User user = new User("1", "user@gmail.com", passwordHasher.hash("password"));
        userRepository.save(user);

        var command = new LoginCommand("user@gmail.com", "password");

        var handler = createHandler();

        TokenResponse response = handler.handle(command);

        AuthUser loggedInUser = jwtService.parse(response.token());

        assertThat(loggedInUser.id()).isEqualTo("1");
        assertThat(loggedInUser.email()).isEqualTo(command.email());
    }

    @Test
    void logUserInWithNonExistingEmail_shouldThrow() {
        var command = new LoginCommand("user@gmail.com", "password");

        var handler = createHandler();

        assertThatThrownBy(() -> handler.handle(command))
                .isInstanceOf(UserDoesNotExistException.class)
                .hasMessage("User with email user@gmail.com does not exist");
    }

    @Test
    void logUserInWithWrongPassword_shouldThrow() {
        User user = new User("1", "user@gmail.com", "password");
        userRepository.save(user);

        var command = new LoginCommand("user@gmail.com", "1234");

        var handler = createHandler();

        assertThatThrownBy(() -> handler.handle(command))
                .isInstanceOf(IncorrectPasswordException.class)
                .hasMessage("Incorrect password for user with email user@gmail.com");
    }

    private LoginCommandHandler createHandler() {
        return new LoginCommandHandler(userRepository, jwtService, passwordHasher);
    }
}
