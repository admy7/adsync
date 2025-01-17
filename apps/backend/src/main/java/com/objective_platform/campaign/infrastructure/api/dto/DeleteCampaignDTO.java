package com.objective_platform.campaign.infrastructure.api.dto;

import jakarta.validation.constraints.NotBlank;

public record DeleteCampaignDTO(@NotBlank String id) {}
