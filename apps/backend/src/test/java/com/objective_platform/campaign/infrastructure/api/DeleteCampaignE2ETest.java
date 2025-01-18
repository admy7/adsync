package com.objective_platform.campaign.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.objective_platform.OpCaseStudyTestConfiguration;
import com.objective_platform.campaign.application.ports.CampaignRepository;
import com.objective_platform.campaign.domain.models.Campaign;
import com.objective_platform.campaign.domain.models.Channel;
import com.objective_platform.campaign.domain.models.Period;
import com.objective_platform.campaign.infrastructure.api.dto.DeleteCampaignDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.com.github.dockerjava.core.MediaType;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@Import(OpCaseStudyTestConfiguration.class)
public class DeleteCampaignE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
        var dto = new DeleteCampaignDTO(campaignId);

        mockMvc
                .perform(
                        MockMvcRequestBuilders.delete("/campaigns")
                                .contentType(MediaType.APPLICATION_JSON.getMediaType())
                                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andReturn();

        var campaignQuery = campaignRepository.findById(campaignId);

        assertThat(campaignQuery.isPresent()).isFalse();
    }

    @Test
    void deleteNonExistingCampaign_ReturnsNotFound() throws Exception {
        var dto = new DeleteCampaignDTO("non-existing-id");

        mockMvc
                .perform(
                        MockMvcRequestBuilders.delete("/campaigns")
                                .contentType(MediaType.APPLICATION_JSON.getMediaType())
                                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
    }
}
