package com.objective_platform.campaign.application.usecases;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;

public record DeleteCampaignCommand(String id) implements Command<Voidy> {}
