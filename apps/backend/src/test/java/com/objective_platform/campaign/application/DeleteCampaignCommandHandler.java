package com.objective_platform.campaign.application;

import com.objective_platform.campaign.application.ports.CampaignRepository;
import com.objective_platform.campaign.domain.models.exceptions.CampaignNotFoundException;

public class DeleteCampaignCommandHandler {
    private final CampaignRepository campaignRepository;

    public DeleteCampaignCommandHandler(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    public void handle(DeleteCampaignCommand command) {
        campaignRepository.findById(command.id())
            .orElseThrow(() -> new CampaignNotFoundException(command.id()));

        campaignRepository.delete(command.id());
    }
}
