package com.objective_platform.campaign.application.usecases;

import com.objective_platform.campaign.domain.models.Channel;

import java.time.LocalDateTime;
import java.util.Optional;

public record UpdateCampaignCommand(
        String id,
        Optional<Channel> channel,
        Optional<Double> budget,
        Optional<LocalDateTime> start,
        Optional<LocalDateTime> end) {
}
