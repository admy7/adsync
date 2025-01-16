package com.objective_platform.campaign.application.usecases;

import com.objective_platform.campaign.domain.models.Channel;

import java.time.LocalDateTime;

public record CreateCampaignCommand(
        Channel channel,
        Double budget,
        LocalDateTime start,
        LocalDateTime end) {
}

