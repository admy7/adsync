package com.objective_platform.campaign.infrastructure.api.dto;

import com.objective_platform.campaign.domain.models.Channel;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.Optional;

public record UpdateCampaignDTO(@NotBlank(message = "Campaign id is mandatory") String id, Channel channel,
                                Double budget, String start, String end) {

    public Optional<Channel> _channel() {
        return Optional.ofNullable(channel);
    }

    public Optional<Double> _budget() {
        return Optional.ofNullable(budget);
    }

    public Optional<LocalDateTime> _startDate() {
        return Optional.ofNullable(start).map(LocalDateTime::parse);
    }

    public Optional<LocalDateTime> _endDate() {
        return Optional.ofNullable(end).map(LocalDateTime::parse);
    }
}
