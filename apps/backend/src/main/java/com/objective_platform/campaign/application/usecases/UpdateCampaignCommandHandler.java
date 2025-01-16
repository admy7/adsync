package com.objective_platform.campaign.application.usecases;

import com.objective_platform.campaign.application.ports.CampaignRepository;
import com.objective_platform.campaign.domain.models.exceptions.CampaignNotFoundException;

public class UpdateCampaignCommandHandler {
    private final CampaignRepository campaignRepository;

    public UpdateCampaignCommandHandler(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    public void handle(UpdateCampaignCommand command) {
        var campaign = campaignRepository.findById(command.id()).orElseThrow(
                () -> new CampaignNotFoundException(command.id())
        );

        command.budget().ifPresent(campaign::updateBudget);
        command.channel().ifPresent(campaign::updateChannel);
        command.startDate().ifPresent(campaign::updateStartDate);
        command.endDate().ifPresent(campaign::updateEndDate);

        campaignRepository.save(campaign);
    }
}
