package com.objective_platform.campaign.infrastructure.api.dto;

import com.objective_platform.campaign.domain.models.Channel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateCampaignDTO(
        @NotNull(message = "Channel is mandatory") Channel channel,
        @NotNull(message = "Budget is mandatory") double budget,
        @NotBlank(message = "Start date is mandatory") String start,
        @NotBlank(message = "End date is mandatory") String end) {

    public LocalDateTime startDate() {
        return LocalDateTime.parse(start);
    }

    public LocalDateTime endDate() {
        return LocalDateTime.parse(end);
    }
}
