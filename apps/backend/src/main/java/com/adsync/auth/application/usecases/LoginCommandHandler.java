package com.adsync.auth.application.usecases;

import an.awesome.pipelinr.Command;
import com.adsync.auth.application.ports.UserRepository;
import com.adsync.auth.application.services.JwtService;
import com.adsync.auth.application.services.PasswordHasher;
import com.adsync.auth.domain.exceptions.IncorrectPasswordException;
import com.adsync.auth.domain.exceptions.UserDoesNotExistException;
import com.adsync.auth.domain.models.User;
import com.adsync.auth.domain.viewmodels.TokenResponse;

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
