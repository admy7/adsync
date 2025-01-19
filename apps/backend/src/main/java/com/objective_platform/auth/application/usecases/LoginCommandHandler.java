package com.objective_platform.auth.application.usecases;

import an.awesome.pipelinr.Command;
import com.objective_platform.auth.application.ports.UserRepository;
import com.objective_platform.auth.application.services.JwtService;
import com.objective_platform.auth.application.services.PasswordHasher;
import com.objective_platform.auth.domain.exceptions.IncorrectPasswordException;
import com.objective_platform.auth.domain.exceptions.UserDoesNotExistException;
import com.objective_platform.auth.domain.models.User;
import com.objective_platform.auth.domain.viewmodels.TokenResponse;

public class LoginCommandHandler implements Command.Handler<LoginCommand, TokenResponse> {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordHasher passwordHasher;

    public LoginCommandHandler(UserRepository userRepository, JwtService jwtService, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordHasher = passwordHasher;
    }

    public TokenResponse handle(LoginCommand command) {
        User user = userRepository.findByEmail(command.email()).orElseThrow(() -> new UserDoesNotExistException(command.email()));

        if (!passwordHasher.match(command.password(), user.password())) {
            throw new IncorrectPasswordException(command.email());
        };

        var token = jwtService.tokenize(user);

        return new TokenResponse(token);
    }
}
