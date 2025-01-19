package com.adsync.campaign.infrastructure.api.dto;

import com.adsync.campaign.domain.models.Channel;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;
import java.util.Optional;

public record UpdateCampaignDTO(
        Channel channel,

        Double budget,

        @Pattern(
                regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$",
                message = "Date must follow the ISO DateTime format: yyyy-MM-ddTHH:mm:ss"
        )
        String start,

        @Pattern(
                regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$",
                message = "Date must follow the ISO DateTime format: yyyy-MM-ddTHH:mm:ss"
        )
        String end) {

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
