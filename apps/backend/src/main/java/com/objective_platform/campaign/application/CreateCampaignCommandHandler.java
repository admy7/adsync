package com.objective_platform.campaign.application;

import com.objective_platform.campaign.application.ports.CampaignRepository;
import com.objective_platform.campaign.domain.models.Campaign;
import com.objective_platform.campaign.domain.models.Period;
import com.objective_platform.campaign.domain.viewmodels.IdResponse;

public class CreateCampaignCommandHandler {
    private final CampaignRepository inMemoryCampaignRepository;

    public CreateCampaignCommandHandler(CampaignRepository inMemoryCampaignRepository) {
        this.inMemoryCampaignRepository = inMemoryCampaignRepository;
    }

    public IdResponse handle(CreateCampaignCommand command) {
        Campaign campaign = new Campaign(command.channel(), command.budget(), new Period(command.startDate(), command.endDate()));

        inMemoryCampaignRepository.save(campaign);

        return new IdResponse(campaign.id());
    }
}
