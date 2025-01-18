package com.objective_platform.campaign.infrastructure.api;

import com.objective_platform.OpCaseStudyTestConfiguration;
import com.objective_platform.campaign.application.ports.CampaignRepository;
import com.objective_platform.campaign.domain.models.Campaign;
import com.objective_platform.campaign.domain.models.Channel;
import com.objective_platform.campaign.domain.models.Period;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@Import(OpCaseStudyTestConfiguration.class)
public class DeleteCampaignE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CampaignRepository campaignRepository;

    private String campaignId = "5";

    @BeforeEach
    void setUp() {
        campaignRepository.clear();

        Period period = new Period(LocalDateTime.parse("2025-04-01T08:00:00"), LocalDateTime.parse("2025-05-01T08:00:00"));
        Campaign campaign = new Campaign(campaignId, Channel.SOCIAL_MEDIA, 10_000d, period);

        campaignRepository.save(campaign);
    }

    @Test
    void deleteCampaign() throws Exception {
        mockMvc
                .perform(
                        MockMvcRequestBuilders.delete("/campaigns/5"))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andReturn();

        var campaignQuery = campaignRepository.findById(campaignId);

        assertThat(campaignQuery.isPresent()).isFalse();
    }

    @Test
    void deleteNonExistingCampaign_ReturnsNotFound() throws Exception {
        mockMvc
                .perform(
                        MockMvcRequestBuilders.delete("/campaigns/non-existing-id"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
    }
}
