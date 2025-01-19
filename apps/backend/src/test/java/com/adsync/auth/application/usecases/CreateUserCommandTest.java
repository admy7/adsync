package com.adsync.auth.application.usecases;

import com.adsync.auth.application.ports.UserRepository;
import com.adsync.auth.domain.exceptions.EmailAlreadyTakenException;
import com.adsync.auth.domain.models.User;
import com.adsync.auth.infrastructure.persistence.InMemoryUserRepository;
import com.adsync.auth.application.services.BCryptPasswordHasher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CreateUserCommandTest {

    private UserRepository userRepository = new InMemoryUserRepository();
    private BCryptPasswordHasher passwordHasher = new BCryptPasswordHasher();

    @BeforeEach
    void setUp() {
        userRepository.clear();
    }

    @Test
    void createUser() {
        var command = new CreateUserCommand("test@gmail.com", "test_1234");
        var handler = createHandler();

        handler.handle(command);

        var actualUser = userRepository.findByEmail(command.email()).get();

        assertThat(actualUser.email()).isEqualTo(command.email());
        assertThat(passwordHasher.match(command.password(), actualUser.password())).isTrue();
    }

    @Test
    void createUserWithExistingEmail_shouldThrow() {
        userRepository.save(new User("1", "test@gmail.com", "password"));

        var command = new CreateUserCommand("test@gmail.com", "test_1234");
        var handler = createHandler();

        assertThatThrownBy(() -> handler.handle(command))
                .isInstanceOf(EmailAlreadyTakenException.class)
                .hasMessage("Email test@gmail.com is already taken");
    }

    private CreateUserCommandHandler createHandler() {
        return new CreateUserCommandHandler(userRepository, passwordHasher);
    }
}
