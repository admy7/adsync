package com.adsync.campaign.infrastructure.api;

import static org.assertj.core.api.Assertions.assertThat;

import com.adsync.campaign.application.ports.CampaignRepository;
import com.adsync.campaign.domain.models.Campaign;
import com.adsync.campaign.domain.models.Channel;
import com.adsync.campaign.domain.models.Period;
import com.adsync.campaign.domain.viewmodels.CampaignViewModel;
import com.adsync.core.infrastructure.api.IntegrationTest;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class GetCampaignByIdE2ETest extends IntegrationTest {

  @Autowired private CampaignRepository campaignRepository;

  @BeforeEach
  void setUp() {
    campaignRepository.clear();

    Period period =
        new Period(
            LocalDateTime.parse("2025-04-01T08:00:00"), LocalDateTime.parse("2025-05-01T08:00:00"));
    Campaign campaign = new Campaign("7", "Summer Sale", Channel.SOCIAL_MEDIA, 10_000d, period);

    campaignRepository.save(campaign);
  }

  @Test
  void getCampaignById() throws Exception {
    var user = createAndSaveUser("1", "user", "password");

    var response =
        mockMvc
            .perform(
                MockMvcRequestBuilders.get("/campaigns/7").header("Authorization", createJwt(user)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

    var campaign =
        objectMapper.readValue(
            response.getResponse().getContentAsString(), CampaignViewModel.class);

    assertThat(campaign.id()).isEqualTo("7");
  }
}
