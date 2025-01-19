package com.adsync.auth.application.usecases;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;

public record CreateUserCommand(String email, String password) implements Command<Voidy> {}
