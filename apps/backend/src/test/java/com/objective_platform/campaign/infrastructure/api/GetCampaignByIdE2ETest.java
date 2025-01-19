package com.objective_platform.campaign.infrastructure.api;

import com.objective_platform.campaign.application.ports.CampaignRepository;
import com.objective_platform.campaign.domain.models.Campaign;
import com.objective_platform.campaign.domain.models.Channel;
import com.objective_platform.campaign.domain.models.Period;
import com.objective_platform.campaign.domain.viewmodels.CampaignViewModel;
import com.objective_platform.core.infrastructure.api.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class GetCampaignByIdE2ETest extends IntegrationTest {

    @Autowired
    private CampaignRepository campaignRepository;

    @BeforeEach
    void setUp() {
        campaignRepository.clear();

        Period period = new Period(LocalDateTime.parse("2025-04-01T08:00:00"), LocalDateTime.parse("2025-05-01T08:00:00"));
        Campaign campaign = new Campaign("7", Channel.SOCIAL_MEDIA, 10_000d, period);

        campaignRepository.save(campaign);
    }

    @Test
    void getCampaignById() throws Exception {
        var user = createAndSaveUser("1", "user", "password");

        var response = mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/campaigns/7")
                                .header("Authorization", createJwt(user)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var campaign = objectMapper.readValue(response.getResponse().getContentAsString(), CampaignViewModel.class);

        assertThat(campaign.id()).isEqualTo("7");
    }
}
