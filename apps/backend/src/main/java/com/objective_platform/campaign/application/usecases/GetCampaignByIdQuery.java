package com.objective_platform.campaign.application.usecases;

import com.objective_platform.campaign.application.ports.CampaignRepository;
import com.objective_platform.campaign.domain.models.exceptions.CampaignNotFoundException;
import com.objective_platform.campaign.domain.viewmodels.CampaignViewModel;

public class GetCampaignByIdQuery {
    private final CampaignRepository campaignRepository;

    public GetCampaignByIdQuery(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    public CampaignViewModel handle(String id) {
        return campaignRepository.findById(id).map(CampaignViewModel::fromDomain).orElseThrow(() -> new CampaignNotFoundException(id));
    }
}
