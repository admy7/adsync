package com.adsync.auth.infrastructure.api.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateUserDTO(
    @NotBlank(message = "Email is mandatory") String email,
    @NotBlank(message = "Password is mandatory") String password) {}
