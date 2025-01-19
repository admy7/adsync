package com.adsync.campaign.application.usecases;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import com.adsync.campaign.domain.models.Channel;

import java.time.LocalDateTime;
import java.util.Optional;

public record UpdateCampaignCommand(
        String id,
        Optional<Channel> channel,
        Optional<Double> budget,
        Optional<LocalDateTime> start,
        Optional<LocalDateTime> end) implements Command<Voidy> {
}
