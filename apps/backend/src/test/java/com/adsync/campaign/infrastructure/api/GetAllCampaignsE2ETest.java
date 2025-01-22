package com.adsync.campaign.infrastructure.api;

import static org.assertj.core.api.Assertions.assertThat;

import com.adsync.campaign.application.ports.CampaignRepository;
import com.adsync.campaign.domain.models.Campaign;
import com.adsync.campaign.domain.models.Channel;
import com.adsync.campaign.domain.models.Period;
import com.adsync.campaign.infrastructure.api.dto.GetAllCampaignsDTO;
import com.adsync.core.infrastructure.api.IntegrationTest;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class GetAllCampaignsE2ETest extends IntegrationTest {

  @Autowired private CampaignRepository campaignRepository;

  @BeforeEach
  void setUp() {
    campaignRepository.clear();

    Period period1 =
        new Period(
            LocalDateTime.parse("2025-04-01T08:00:00"), LocalDateTime.parse("2025-05-01T08:00:00"));
    Campaign campaign1 = new Campaign("1", "Summer Sale", Channel.SOCIAL_MEDIA, 10_000d, period1);

    Period period2 =
        new Period(
            LocalDateTime.parse("2025-09-01T08:00:00"), LocalDateTime.parse("2025-12-01T08:00:00"));
    Campaign campaign2 = new Campaign("2", "Brand Awareness", Channel.RADIO, 2000d, period2);

    campaignRepository.save(campaign1);
    campaignRepository.save(campaign2);
  }

  @Test
  void getAllCampaigns() throws Exception {
    var user = createAndSaveUser("1", "user", "password");

    var response =
        mockMvc
            .perform(
                MockMvcRequestBuilders.get("/campaigns").header("Authorization", createJwt(user)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

    var dto =
        objectMapper.readValue(
            response.getResponse().getContentAsString(), GetAllCampaignsDTO.class);

    assertThat(dto.campaigns().get(0).id()).isEqualTo("1");
    assertThat(dto.campaigns().get(1).id()).isEqualTo("2");
  }
}
