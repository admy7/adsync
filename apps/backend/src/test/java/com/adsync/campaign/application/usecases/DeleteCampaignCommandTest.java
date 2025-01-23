package com.adsync.campaign.application.usecases;

import static org.assertj.core.api.Assertions.*;

import com.adsync.campaign.application.ports.CampaignRepository;
import com.adsync.campaign.domain.models.Campaign;
import com.adsync.campaign.domain.models.Channel;
import com.adsync.campaign.domain.models.Period;
import com.adsync.campaign.domain.models.exceptions.CampaignNotFoundException;
import com.adsync.campaign.infrastructure.persistence.InMemoryCampaignRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DeleteCampaignCommandTest {

  private CampaignRepository campaignRepository = new InMemoryCampaignRepository();
  private String campaignId = "1";

  @BeforeEach
  void setUp() {
    campaignRepository.clear();

    LocalDateTime start = LocalDateTime.parse("2025-05-01T08:00:00");
    LocalDateTime end = LocalDateTime.parse("2025-07-31T08:00:00");
    Campaign campaign =
        new Campaign(campaignId, "Brand Awareness", Channel.RADIO, 1000d, new Period(start, end));

    campaignRepository.save(campaign);
  }

  @Test
  void deleteCampaign() {
    var command = new DeleteCampaignCommand(campaignId);

    var handler = new DeleteCampaignCommandHandler(campaignRepository);

    handler.handle(command);

    var campaignQuery = campaignRepository.findById(campaignId);
    assertThat(campaignQuery).isEmpty();
  }

  @Test
  void deleteCampaignThatDoesNotExist_ShouldThrow() {
    var command = new DeleteCampaignCommand("non-existing-id");

    var handler = new DeleteCampaignCommandHandler(campaignRepository);

    assertThatThrownBy(() -> handler.handle(command))
        .isInstanceOf(CampaignNotFoundException.class)
        .hasMessage("Campaign with id non-existing-id does not exist");
  }
}
