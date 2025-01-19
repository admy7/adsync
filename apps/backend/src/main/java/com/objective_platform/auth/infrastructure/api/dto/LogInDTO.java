package com.objective_platform.auth.infrastructure.api.dto;

import jakarta.validation.constraints.NotBlank;

public record LogInDTO(@NotBlank(message = "Email is mandatory") String email, @NotBlank(message = "Password is mandatory") String password) {}
