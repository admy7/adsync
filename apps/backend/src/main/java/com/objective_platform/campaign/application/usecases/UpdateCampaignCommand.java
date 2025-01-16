package com.objective_platform.campaign.application.usecases;

import java.util.Optional;

public class UpdateCampaignCommand {
    private String id;
    private String channel;
    private Double budget;
    private String startDate;
    private String endDate;

    public UpdateCampaignCommand(String id, String channel, Double budget, String startDate, String endDate) {
        this.id = id;
        this.budget = budget;
        this.endDate = endDate;
        this.startDate = startDate;
        this.channel = channel;
    }

    public String id() {
        return id;
    }

    public Optional<Double> budget() {
        return Optional.ofNullable(budget);
    }

    public Optional<String> channel() {
        return Optional.ofNullable(channel);
    }

    public Optional<String> startDate() {
        return Optional.ofNullable(startDate);
    }

    public Optional<String> endDate() {
        return Optional.ofNullable(endDate);
    }
}
