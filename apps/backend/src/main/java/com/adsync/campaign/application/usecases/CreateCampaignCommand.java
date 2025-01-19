package com.adsync.campaign.application.usecases;

import an.awesome.pipelinr.Command;
import com.adsync.campaign.domain.models.Channel;
import com.adsync.campaign.domain.viewmodels.IdResponse;

import java.time.LocalDateTime;


public record CreateCampaignCommand(
        Channel channel,
        Double budget,
        LocalDateTime start,
        LocalDateTime end) implements Command<IdResponse> {
}

