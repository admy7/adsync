package com.adsync.campaign.application.usecases;

import an.awesome.pipelinr.Command;
import com.adsync.campaign.application.ports.CampaignRepository;
import com.adsync.campaign.domain.models.Campaign;
import com.adsync.campaign.domain.models.Period;
import com.adsync.campaign.domain.viewmodels.IdResponse;

import java.util.UUID;

public class CreateCampaignCommandHandler implements Command.Handler<CreateCampaignCommand, IdResponse> {
    private final CampaignRepository campaignRepository;

    public CreateCampaignCommandHandler(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    public IdResponse handle(CreateCampaignCommand command) {
        Campaign campaign = new Campaign(UUID.randomUUID().toString(), command.channel(), command.budget(), new Period(command.start(), command.end()));

        campaignRepository.save(campaign);

        return new IdResponse(campaign.id());
    }
}
