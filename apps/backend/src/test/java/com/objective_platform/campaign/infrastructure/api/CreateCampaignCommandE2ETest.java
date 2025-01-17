package com.objective_platform.campaign.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.objective_platform.OpCaseStudyTestConfiguration;
import com.objective_platform.campaign.application.ports.CampaignRepository;
import com.objective_platform.campaign.domain.models.Channel;
import com.objective_platform.campaign.domain.viewmodels.IdResponse;
import com.objective_platform.campaign.infrastructure.api.dto.CreateCampaignDTO;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@Import(OpCaseStudyTestConfiguration.class)
public class CreateCampaignCommandE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CampaignRepository campaignRepository;

    @BeforeEach
    void setUp() {
        campaignRepository.clear();
    }

    @Test
    void createCampaign() throws Exception {
        var dto = new CreateCampaignDTO(Channel.SOCIAL_MEDIA, 10_000, "2025-04-01T08:00:00", "2025-05-01T08:00:00");

        var response =
                mockMvc
                        .perform(
                                MockMvcRequestBuilders.post("/campaigns")
                                        .contentType(MediaType.APPLICATION_JSON.getMediaType())
                                        .content(objectMapper.writeValueAsString(dto)))
                        .andExpect(MockMvcResultMatchers.status().isCreated())
                        .andReturn();

        IdResponse idResponse =
                objectMapper.readValue(response.getResponse().getContentAsString(), IdResponse.class);

        var campaignQuery = campaignRepository.findById(idResponse.id());

        assertThat(campaignQuery.isPresent()).isTrue();

        var campaign = campaignQuery.get();

        assertThat(campaign.channel()).isEqualTo(dto.channel());
        assertThat(campaign.budget()).isEqualTo(dto.budget());
        assertThat(campaign.period().start()).isEqualTo(dto.startDate());
        assertThat(campaign.period().end()).isEqualTo(dto.endDate());
    }

    @Test
    void createCampaignWithInvalidDateFormat_ReturnsBadRequest() throws Exception {
        var dto = new CreateCampaignDTO(Channel.SOCIAL_MEDIA, 10_000, "2025-04-01", "2025-05-01T08:00:00");

        mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/campaigns")
                                .contentType(MediaType.APPLICATION_JSON.getMediaType())
                                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }

    @Test
    void createCampaignWithMissingField_ReturnsBadRequest() throws Exception {
        var dto = new CreateCampaignDTO(null, 10_000, "2025-04-01", "2025-05-01T08:00:00");

        mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/campaigns")
                                .contentType(MediaType.APPLICATION_JSON.getMediaType())
                                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }
}
