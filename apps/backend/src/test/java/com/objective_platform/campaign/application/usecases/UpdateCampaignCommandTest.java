package com.objective_platform.campaign.application.usecases;

import com.objective_platform.campaign.application.ports.CampaignRepository;
import com.objective_platform.campaign.domain.models.Campaign;
import com.objective_platform.campaign.domain.models.Channel;
import com.objective_platform.campaign.domain.models.Period;
import com.objective_platform.campaign.domain.models.exceptions.CampaignNotFoundException;
import com.objective_platform.campaign.infrastructure.persistence.InMemoryCampaignRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

public class UpdateCampaignCommandTest {

    private CampaignRepository campaignRepository = new InMemoryCampaignRepository();
    private String campaignId = "2";

    @BeforeEach
    void setUp() {
        campaignRepository.clear();

        LocalDateTime start = LocalDateTime.parse("2025-05-01T08:00:00");
        LocalDateTime end = LocalDateTime.parse("2025-07-31T08:00:00");
        Campaign campaign = new Campaign(campaignId, Channel.RADIO, 1000d, new Period(start, end));

        campaignRepository.save(campaign);
    }

    @Test
    void updateCampaign() {
        var command = new UpdateCampaignCommand(campaignId, Channel.SOCIAL_MEDIA, 10_000d, LocalDateTime.parse("2025-03-01T08:00:00"), LocalDateTime.parse("2025-04-01T14:00:00"));

        var handler = new UpdateCampaignCommandHandler(campaignRepository);

        handler.handle(command);

        var updatedCampaign = campaignRepository.findById(campaignId).get();

        assertThat(updatedCampaign.channel()).isEqualTo(Channel.SOCIAL_MEDIA);
        assertThat(updatedCampaign.budget()).isEqualTo(10_000);
        assertThat(updatedCampaign.period().start()).isEqualTo("2025-03-01T08:00:00");
        assertThat(updatedCampaign.period().end()).isEqualTo("2025-04-01T14:00:00");
    }

    @Test
    void updateCampaignPartially() {
        var command = new UpdateCampaignCommand(campaignId, Channel.SOCIAL_MEDIA, null, null, LocalDateTime.parse("2025-10-01T14:00:00"));

        var handler = new UpdateCampaignCommandHandler(campaignRepository);

        handler.handle(command);

        var updatedCampaign = campaignRepository.findById(campaignId).get();

        assertThat(updatedCampaign.channel()).isEqualTo(Channel.SOCIAL_MEDIA);
        assertThat(updatedCampaign.budget()).isEqualTo(1000);
        assertThat(updatedCampaign.period().start()).isEqualTo("2025-05-01T08:00:00");
        assertThat(updatedCampaign.period().end()).isEqualTo("2025-10-01T14:00:00");
    }

    @Test
    void updateNonExistingCampaign_shouldThrow() {
        var command = new UpdateCampaignCommand("non-existing-id", Channel.SOCIAL_MEDIA, null, null, null);

        var handler = new UpdateCampaignCommandHandler(campaignRepository);

        assertThatThrownBy(() -> handler.handle(command))
                .isInstanceOf(CampaignNotFoundException.class)
                .hasMessage("Campaign with id non-existing-id does not exist");
    }
}
