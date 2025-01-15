package com.objective_platform.campaign.infrastructure.persistence;

import com.objective_platform.campaign.application.ports.CampaignRepository;
import com.objective_platform.campaign.domain.models.Campaign;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class InMemoryCampaignRepository implements CampaignRepository {
    private final Set<Campaign> campaigns = new HashSet<>();

    @Override
    public Optional<Campaign> findById(String id) {
        return campaigns.stream()
            .filter(c -> c.id().equals(id))
            .findFirst();
    }

    @Override
    public void save(Campaign campaign) {
        campaigns.add(campaign);
    }
}
