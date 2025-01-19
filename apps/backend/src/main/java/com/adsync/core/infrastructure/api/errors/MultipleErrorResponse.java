package com.adsync.core.infrastructure.api.errors;

import java.util.List;

public record MultipleErrorResponse(int status, List<String> messages) {}
