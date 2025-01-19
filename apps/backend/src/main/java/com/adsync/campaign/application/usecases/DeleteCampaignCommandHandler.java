package com.adsync.campaign.application.usecases;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import com.adsync.campaign.application.ports.CampaignRepository;
import com.adsync.campaign.domain.models.exceptions.CampaignNotFoundException;

public class DeleteCampaignCommandHandler implements Command.Handler<DeleteCampaignCommand, Voidy> {
    private final CampaignRepository campaignRepository;

    public DeleteCampaignCommandHandler(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    public Voidy handle(DeleteCampaignCommand command) {
        campaignRepository.findById(command.id())
            .orElseThrow(() -> new CampaignNotFoundException(command.id()));

        campaignRepository.delete(command.id());

        return new Voidy();
    }
}
