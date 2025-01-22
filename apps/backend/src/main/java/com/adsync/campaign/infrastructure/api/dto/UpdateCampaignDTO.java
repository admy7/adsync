package com.adsync.campaign.infrastructure.api.dto;

import com.adsync.campaign.domain.models.Channel;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;
import java.util.Optional;

public record UpdateCampaignDTO(
        String name,
        String channel,
        Double budget,
        @Pattern(
                regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$",
                message = "Date must follow the ISO DateTime format: yyyy-MM-ddTHH:mm:ss")
        String start,
        @Pattern(
                regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$",
                message = "Date must follow the ISO DateTime format: yyyy-MM-ddTHH:mm:ss")
        String end) {

    public Optional<String> _name() {
        return Optional.ofNullable(name);
    }

    public Optional<Channel> _channel() {
        if (channel == null) {
            return Optional.empty();
        }

        return Optional.of(channel.toLowerCase())
                .map(Channel::fromString);
    }

    public Optional<Double> _budget() {
        return Optional.ofNullable(budget);
    }

    public Optional<LocalDateTime> _start() {
        return Optional.ofNullable(start).map(LocalDateTime::parse);
    }

    public Optional<LocalDateTime> _end() {
        return Optional.ofNullable(end).map(LocalDateTime::parse);
    }
}
