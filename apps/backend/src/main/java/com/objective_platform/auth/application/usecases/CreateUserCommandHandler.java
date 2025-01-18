package com.objective_platform.auth.application.usecases;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import com.objective_platform.auth.application.ports.UserRepository;
import com.objective_platform.auth.domain.exceptions.EmailAlreadyTakenException;
import com.objective_platform.auth.domain.models.User;
import com.objective_platform.auth.application.services.PasswordHasher;

import java.util.UUID;

public class CreateUserCommandHandler implements Command.Handler<CreateUserCommand, Voidy> {
    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public CreateUserCommandHandler(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public Voidy handle(CreateUserCommand command) {
        userRepository.findByEmail(command.email())
                .ifPresent(existingUser -> {
                    throw new EmailAlreadyTakenException(command.email());
                });

        User user = new User(UUID.randomUUID().toString(), command.email(), passwordHasher.hash(command.password()));

        userRepository.save(user);

        return new Voidy();
    }
}
