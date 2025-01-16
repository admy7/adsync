package com.objective_platform.campaign.application;

import com.objective_platform.campaign.application.ports.CampaignRepository;
import com.objective_platform.campaign.domain.models.Campaign;
import com.objective_platform.campaign.domain.models.Channel;
import com.objective_platform.campaign.domain.models.Period;
import com.objective_platform.campaign.domain.models.exceptions.CampaignNotFoundException;
import com.objective_platform.campaign.infrastructure.persistence.InMemoryCampaignRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class UpdateCampaignCommandTest {

    private CampaignRepository campaignRepository = new InMemoryCampaignRepository();
    private String savedCampaignId;

    @BeforeEach
    void setUp() {
        campaignRepository.clear();

        Campaign campaign = new Campaign("1", "RADIO", 1000, new Period("2025-05-01T08:00:00", "2025-07-31T08:00:00"));
        savedCampaignId = campaign.id();

        campaignRepository.save(campaign);
    }

    @Test
    void updateCampaign() {
        var command = new UpdateCampaignCommand(savedCampaignId, "SOCIAL_MEDIA", 10_000d, "2025-03-01T08:00:00", "2025-04-01T14:00:00");

        var handler = new UpdateCampaignCommandHandler(campaignRepository);

        handler.handle(command);

        var result = campaignRepository.findById(savedCampaignId).get();

        assertThat(result.channel()).isEqualTo(Channel.SOCIAL_MEDIA);
        assertThat(result.budget()).isEqualTo(10_000);
        assertThat(result.period().start()).isEqualTo("2025-03-01T08:00:00");
        assertThat(result.period().end()).isEqualTo("2025-04-01T14:00:00");
    }

    @Test
    void updateCampaignPartially() {
        var command = new UpdateCampaignCommand(savedCampaignId, "SOCIAL_MEDIA", null, null, "2025-10-01T14:00:00");

        var handler = new UpdateCampaignCommandHandler(campaignRepository);

        handler.handle(command);

        var result = campaignRepository.findById(savedCampaignId).get();

        assertThat(result.channel()).isEqualTo(Channel.SOCIAL_MEDIA);
        assertThat(result.budget()).isEqualTo(1000);
        assertThat(result.period().start()).isEqualTo("2025-05-01T08:00:00");
        assertThat(result.period().end()).isEqualTo("2025-10-01T14:00:00");
    }

    @Test
    void updateNonExistingCampaign_shouldThrow() {
        var command = new UpdateCampaignCommand("non-existing-id", "SOCIAL_MEDIA", null, null, "2025-10-01T14:00:00");

        var handler = new UpdateCampaignCommandHandler(campaignRepository);

        assertThatThrownBy(() -> handler.handle(command))
                .isInstanceOf(CampaignNotFoundException.class)
                .hasMessage("Campaign with id non-existing-id does not exist");
    }
}
