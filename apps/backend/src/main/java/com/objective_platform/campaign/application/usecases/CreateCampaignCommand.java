package com.objective_platform.campaign.application.usecases;

import an.awesome.pipelinr.Command;
import com.objective_platform.campaign.domain.models.Channel;
import com.objective_platform.campaign.domain.viewmodels.IdResponse;

import java.time.LocalDateTime;


public record CreateCampaignCommand(
        Channel channel,
        Double budget,
        LocalDateTime start,
        LocalDateTime end) implements Command<IdResponse> {
}

