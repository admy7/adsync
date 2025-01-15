package com.objective_platform.campaign.domain.exceptions;

public class InvalidCampaignPeriodException extends RuntimeException {
    public InvalidCampaignPeriodException() {
        super("Start date must be prior to end date");
    }
}
