package com.adsync.auth.application.usecases;

import an.awesome.pipelinr.Command;
import com.adsync.auth.domain.viewmodels.TokenResponse;

public record LoginCommand(String email, String password) implements Command<TokenResponse> {}
