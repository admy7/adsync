package com.objective_platform.campaign.application.usecases;

public record CreateCampaignCommand(
        String channel,
        double budget,
        String startDate,
        String endDate) {
}

