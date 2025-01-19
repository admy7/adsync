package com.adsync.campaign.infrastructure.api;

import static org.assertj.core.api.Assertions.assertThat;

import com.adsync.campaign.application.ports.CampaignRepository;
import com.adsync.campaign.domain.models.Campaign;
import com.adsync.campaign.domain.models.Channel;
import com.adsync.campaign.domain.models.Period;
import com.adsync.campaign.infrastructure.api.dto.UpdateCampaignDTO;
import com.adsync.core.infrastructure.api.IntegrationTest;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.com.github.dockerjava.core.MediaType;

public class UpdateCampaignE2ETest extends IntegrationTest {

  @Autowired private CampaignRepository campaignRepository;

  private String campaignId = "5";

  @BeforeEach
  void setUp() {
    campaignRepository.clear();

    campaignRepository.clear();

    Period period =
        new Period(
            LocalDateTime.parse("2025-04-01T08:00:00"), LocalDateTime.parse("2025-05-01T08:00:00"));
    Campaign campaign = new Campaign(campaignId, Channel.SOCIAL_MEDIA, 10_000d, period);

    campaignRepository.save(campaign);
  }

  @Test
  void updateCampaign() throws Exception {
    var user = createAndSaveUser("1", "user", "password");

    var dto =
        new UpdateCampaignDTO(Channel.TV, 10_000d, "2025-01-01T08:00:00", "2025-05-01T08:00:00");

    mockMvc
        .perform(
            MockMvcRequestBuilders.patch("/campaigns/5")
                .contentType(MediaType.APPLICATION_JSON.getMediaType())
                .content(objectMapper.writeValueAsString(dto))
                .header("Authorization", createJwt(user)))
        .andExpect(MockMvcResultMatchers.status().isNoContent())
        .andReturn();

    var campaign = campaignRepository.findById(campaignId).get();

    assertThat(campaign.channel()).isEqualTo(dto.channel());
    assertThat(campaign.budget()).isEqualTo(10_000d);
    assertThat(campaign.period().start()).isEqualTo(LocalDateTime.parse("2025-01-01T08:00:00"));
    assertThat(campaign.period().end()).isEqualTo(dto.end());
  }

  @Test
  void updatePartiallyCampaign() throws Exception {
    var user = createAndSaveUser("1", "user", "password");

    var dto = new UpdateCampaignDTO(null, 25_000d, null, "2025-05-01T08:00:00");

    mockMvc
        .perform(
            MockMvcRequestBuilders.patch("/campaigns/5")
                .contentType(MediaType.APPLICATION_JSON.getMediaType())
                .content(objectMapper.writeValueAsString(dto))
                .header("Authorization", createJwt(user)))
        .andExpect(MockMvcResultMatchers.status().isNoContent())
        .andReturn();

    var campaign = campaignRepository.findById(campaignId).get();

    assertThat(campaign.channel()).isEqualTo(Channel.SOCIAL_MEDIA);
    assertThat(campaign.budget()).isEqualTo(25_000d);
    assertThat(campaign.period().start()).isEqualTo("2025-04-01T08:00:00");
    assertThat(campaign.period().end()).isEqualTo(dto.end());
  }
}
