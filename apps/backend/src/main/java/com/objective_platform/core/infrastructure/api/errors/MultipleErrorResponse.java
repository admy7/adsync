package com.objective_platform.core.infrastructure.api.errors;

import java.util.List;

public record MultipleErrorResponse(int status, List<String> messages) {}
