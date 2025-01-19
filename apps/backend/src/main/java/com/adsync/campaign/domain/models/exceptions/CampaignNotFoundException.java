package com.adsync.campaign.domain.models.exceptions;

public class CampaignNotFoundException extends RuntimeException {
    public CampaignNotFoundException(String id) {
        super("Campaign with id " + id + " does not exist");
    }
}
