package com.adsync.campaign.application.ports;

import com.adsync.campaign.domain.models.Campaign;

import java.util.List;
import java.util.Optional;

public interface CampaignRepository {
    Optional<Campaign> findById(String id);

    void save(Campaign campaign);

    void delete(String id);

    void clear();

    List<Campaign> findAll();
}
