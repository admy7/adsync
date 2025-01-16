package com.objective_platform.campaign.application.usecases;

import com.objective_platform.campaign.domain.models.Channel;

import java.time.LocalDateTime;
import java.util.Optional;

public class UpdateCampaignCommand {
    private String id;
    private Channel channel;
    private Double budget;
    private LocalDateTime start;
    private LocalDateTime end;

    public UpdateCampaignCommand(String id, Channel channel, Double budget, LocalDateTime start, LocalDateTime end) {
        this.id = id;
        this.channel = channel;
        this.budget = budget;
        this.start = start;
        this.end = end;
    }

    public String id() {
        return id;
    }

    public Optional<Double> budget() {
        return Optional.ofNullable(budget);
    }

    public Optional<Channel> channel() {
        return Optional.ofNullable(channel);
    }

    public Optional<LocalDateTime> startDate() {
        return Optional.ofNullable(start);
    }

    public Optional<LocalDateTime> endDate() {
        return Optional.ofNullable(end);
    }
}
