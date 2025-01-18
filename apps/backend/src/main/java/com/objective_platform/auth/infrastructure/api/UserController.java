package com.objective_platform.auth.infrastructure.api;

import an.awesome.pipelinr.Pipeline;
import com.objective_platform.auth.application.usecases.CreateUserCommand;
import com.objective_platform.auth.infrastructure.api.dto.CreateUserDTO;
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
}
