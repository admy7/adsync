package com.objective_platform.campaign.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.objective_platform.OpCaseStudyTestConfiguration;
import com.objective_platform.campaign.application.ports.CampaignRepository;
import com.objective_platform.campaign.domain.models.Campaign;
import com.objective_platform.campaign.domain.models.Channel;
import com.objective_platform.campaign.domain.models.Period;
import com.objective_platform.campaign.domain.viewmodels.CampaignViewModel;
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
public class GetCampaignByIdE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        campaignRepository.clear();

        Period period = new Period(LocalDateTime.parse("2025-04-01T08:00:00"), LocalDateTime.parse("2025-05-01T08:00:00"));
        Campaign campaign = new Campaign("7", Channel.SOCIAL_MEDIA, 10_000d, period);

        campaignRepository.save(campaign);
    }

    @Test
    void getCampaignById() throws Exception {
        var response = mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/campaigns/7"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        CampaignViewModel campaign = objectMapper.readValue(response.getResponse().getContentAsString(), CampaignViewModel.class);

        assertThat(campaign.id()).isEqualTo("7");
    }
}
