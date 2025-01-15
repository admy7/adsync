package com.objective_platform.campaign.application.ports;

import com.objective_platform.campaign.domain.models.Campaign;

import java.util.HashSet;
import java.util.Set;

public class CampaignRepository {
    private final Set<Campaign> campaigns = new HashSet<>();

    public Campaign findById(String id) {
        return campaigns.stream()
            .filter(c -> c.id().equals(id))
            .findFirst()
            .orElse(null);
    }

    public void save(Campaign campaign) {
        campaigns.add(campaign);
    }
}
