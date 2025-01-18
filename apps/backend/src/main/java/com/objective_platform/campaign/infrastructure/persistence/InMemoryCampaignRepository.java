package com.objective_platform.campaign.infrastructure.persistence;

import com.objective_platform.campaign.application.ports.CampaignRepository;
import com.objective_platform.campaign.domain.models.Campaign;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryCampaignRepository implements CampaignRepository {
    private final Map<String, Campaign> campaigns = new HashMap<>();

    @Override
    public Optional<Campaign> findById(String id) {
        return campaigns.get(id) == null ? Optional.empty() : Optional.of(campaigns.get(id).deepClone());
    }

    @Override
    public void save(Campaign campaign) {
        campaigns.put(campaign.id(), campaign);
    }

    @Override
    public void delete(String id) {
        campaigns.remove(id);
    }

    @Override
    public void clear() {
        campaigns.clear();
    }

    @Override
    public List<Campaign> findAll() {
        return List.copyOf(campaigns.values());
    }
}
