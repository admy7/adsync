package com.adsync.campaign.application.usecases;

import com.adsync.campaign.application.ports.CampaignRepository;
import com.adsync.campaign.domain.viewmodels.CampaignViewModel;

import java.util.List;

public class GetAllCampaignsQuery {
    private final CampaignRepository campaignRepository;

    public GetAllCampaignsQuery(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    public List<CampaignViewModel> handle() {
        return campaignRepository
                .findAll()
                .stream()
                .map(CampaignViewModel::fromDomain)
                .toList();
    }
}
