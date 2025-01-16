package com.objective_platform.campaign.application.usecases;

import com.objective_platform.campaign.domain.models.Channel;
import com.objective_platform.campaign.domain.viewmodels.IdResponse;
import com.objective_platform.campaign.infrastructure.persistence.InMemoryCampaignRepository;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class CreateCampaignCommandTest {

    @Test
    public void createCampaign() {
        var command = new CreateCampaignCommand("TV", 2500, "2025-02-01T08:00:00", "2025-03-01T14:00:00");

        var repository = new InMemoryCampaignRepository();
        var handler = new CreateCampaignCommandHandler(repository);

        IdResponse response = handler.handle(command);

        var result = repository.findById(response.id()).get();

        assertThat(result).isNotNull();
        assertThat(result.channel()).isEqualTo(Channel.TV);
        assertThat(result.budget()).isEqualTo(2500);
        assertThat(result.period().start()).isEqualTo("2025-02-01T08:00:00");
        assertThat(result.period().end()).isEqualTo("2025-03-01T14:00:00");
    }
}
