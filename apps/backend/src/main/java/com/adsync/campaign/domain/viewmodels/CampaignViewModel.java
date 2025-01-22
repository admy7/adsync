package com.adsync.campaign.domain.viewmodels;

import com.adsync.campaign.domain.models.Campaign;

public record CampaignViewModel(
        String id, String name, String channel, Double budget, String start, String end) {
    public static CampaignViewModel fromDomain(Campaign campaign) {
        return new CampaignViewModel(
                campaign.id(),
                campaign.name(),
                campaign.channel().getValue(),
                campaign.budget(),
                campaign.period().start().toString(),
                campaign.period().end().toString());
    }
}
