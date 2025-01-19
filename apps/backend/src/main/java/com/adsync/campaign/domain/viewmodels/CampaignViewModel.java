package com.adsync.campaign.domain.viewmodels;

import com.adsync.campaign.domain.models.Campaign;
import com.adsync.campaign.domain.models.Channel;

public record CampaignViewModel(
    String id, Channel channel, Double budget, String start, String end) {
  public static CampaignViewModel fromDomain(Campaign campaign) {
    return new CampaignViewModel(
        campaign.id(),
        campaign.channel(),
        campaign.budget(),
        campaign.period().start().toString(),
        campaign.period().end().toString());
  }
}
