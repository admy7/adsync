package com.adsync.campaign.infrastructure.api.dto;

import com.adsync.campaign.domain.viewmodels.CampaignViewModel;

import java.util.List;

public record GetAllCampaignsDTO(List<CampaignViewModel> campaigns, Integer count) {}
