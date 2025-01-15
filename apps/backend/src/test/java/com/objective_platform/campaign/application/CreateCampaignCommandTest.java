package com.objective_platform.campaign.application;

import com.objective_platform.campaign.domain.viewmodels.IdResponse;
import com.objective_platform.campaign.infrastructure.persistence.InMemoryCampaignRepository;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class CreateCampaignCommandTest {

    @Test
    public void createCampaign() {
        var command = new CreateCampaignCommand("TV", 2500, "2025-02-01 08:00:00", "2025-03-01 14:00:00");

        var repository = new InMemoryCampaignRepository();
        var handler = new CreateCampaignCommandHandler(repository);

        IdResponse response = handler.handle(command);

        var result = repository.findById(response.id()).get();
        assertThat(result).isNotNull();
    }
}
