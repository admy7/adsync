package com.objective_platform.campaign.application.ports;

import com.objective_platform.campaign.domain.models.Campaign;

import java.util.Optional;

public interface CampaignRepository {
    Optional<Campaign> findById(String id);
    void save(Campaign campaign);

    void clear();

    void delete(String id);
}
