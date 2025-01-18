package com.objective_platform.campaign.application.usecases;

import com.objective_platform.campaign.application.ports.CampaignRepository;
import com.objective_platform.campaign.domain.viewmodels.CampaignViewModel;

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
