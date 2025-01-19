package com.objective_platform.auth.application.usecases;

import an.awesome.pipelinr.Command;
import com.objective_platform.auth.domain.viewmodels.TokenResponse;

public record LoginCommand(String email, String password) implements Command<TokenResponse> {}
