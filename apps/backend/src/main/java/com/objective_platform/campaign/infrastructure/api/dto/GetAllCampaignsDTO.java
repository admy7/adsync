package com.objective_platform.campaign.infrastructure.api.dto;

import com.objective_platform.campaign.domain.viewmodels.CampaignViewModel;

import java.util.List;

public record GetAllCampaignsDTO(List<CampaignViewModel> campaigns, Integer count) {}
