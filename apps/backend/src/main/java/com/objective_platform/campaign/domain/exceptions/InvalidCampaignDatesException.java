package com.objective_platform.campaign.domain.exceptions;

public class InvalidCampaignDatesException extends RuntimeException {
    public InvalidCampaignDatesException() {
        super("Invalid campaign dates");
    }
}
