package com.objective_platform.campaign.application.usecases;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import com.objective_platform.campaign.application.ports.CampaignRepository;
import com.objective_platform.campaign.domain.models.exceptions.CampaignNotFoundException;

public class UpdateCampaignCommandHandler implements Command.Handler<UpdateCampaignCommand, Voidy> {
    private final CampaignRepository campaignRepository;

    public UpdateCampaignCommandHandler(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    public Voidy handle(UpdateCampaignCommand command) {
        var campaign = campaignRepository.findById(command.id()).orElseThrow(
                () -> new CampaignNotFoundException(command.id())
        );

        command.budget().ifPresent(campaign::updateBudget);
        command.channel().ifPresent(campaign::updateChannel);
        command.start().ifPresent(campaign::updateStart);
        command.end().ifPresent(campaign::updateEnd);

        campaignRepository.save(campaign);

        return new Voidy();
    }
}
