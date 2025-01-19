package com.adsync.campaign.application.usecases;

import com.adsync.campaign.application.ports.CampaignRepository;
import com.adsync.campaign.domain.models.exceptions.CampaignNotFoundException;
import com.adsync.campaign.domain.viewmodels.CampaignViewModel;

public class GetCampaignByIdQuery {
  private final CampaignRepository campaignRepository;

  public GetCampaignByIdQuery(CampaignRepository campaignRepository) {
    this.campaignRepository = campaignRepository;
  }

  public CampaignViewModel handle(String id) {
    return campaignRepository
        .findById(id)
        .map(CampaignViewModel::fromDomain)
        .orElseThrow(() -> new CampaignNotFoundException(id));
  }
}
