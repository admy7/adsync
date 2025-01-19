package com.adsync.auth.infrastructure.api;

import an.awesome.pipelinr.Pipeline;
import com.adsync.auth.application.usecases.CreateUserCommand;
import com.adsync.auth.application.usecases.LoginCommand;
import com.adsync.auth.domain.viewmodels.TokenResponse;
import com.adsync.auth.infrastructure.api.dto.CreateUserDTO;
import com.adsync.auth.infrastructure.api.dto.LogInDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UserController {

  private final Pipeline pipeline;

  public UserController(Pipeline pipeline) {
    this.pipeline = pipeline;
  }

  @PostMapping("/register")
  public ResponseEntity<Void> createUser(@Valid @RequestBody CreateUserDTO dto) {
    CreateUserCommand command = new CreateUserCommand(dto.email(), dto.password());

    pipeline.send(command);

    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @PostMapping("/login")
  public ResponseEntity<TokenResponse> logIn(@Valid @RequestBody LogInDTO dto) {
    var command = new LoginCommand(dto.email(), dto.password());

    TokenResponse response = pipeline.send(command);

    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
