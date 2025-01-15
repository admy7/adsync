package com.objective_platform.campaign.application;

import com.objective_platform.campaign.application.ports.CampaignRepository;
import com.objective_platform.campaign.domain.exceptions.InvalidCampaignDatesException;
import com.objective_platform.campaign.domain.exceptions.InvalidCampaignPeriodException;
import com.objective_platform.campaign.domain.viewmodels.IdResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class CreateCampaignCommandTest {

    @Test
    public void createCampaign() {
        var command = new CreateCampaignCommand("Social Media", 2500, "2025-02-01 08:00:00", "2025-03-01 14:00:00");

        var repository = new CampaignRepository();
        var handler = new CreateCampaignCommandHandler(repository);

        IdResponse response = handler.handle(command);

        var result = repository.findById(response.id());

        assertThat(result).isNotNull();
    }
}
