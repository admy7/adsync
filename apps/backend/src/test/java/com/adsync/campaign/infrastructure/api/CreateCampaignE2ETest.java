package com.adsync.campaign.infrastructure.api;

import com.adsync.campaign.application.ports.CampaignRepository;
import com.adsync.campaign.domain.models.Channel;
import com.adsync.campaign.domain.viewmodels.IdResponse;
import com.adsync.campaign.infrastructure.api.dto.CreateCampaignDTO;
import com.adsync.core.infrastructure.api.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.com.github.dockerjava.core.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateCampaignE2ETest extends IntegrationTest {

    @Autowired
    private CampaignRepository campaignRepository;

    @BeforeEach
    void setUp() {
        campaignRepository.clear();
    }

    @Test
    void createCampaign() throws Exception {
        var dto = new CreateCampaignDTO(Channel.SOCIAL_MEDIA, 10_000, "2025-04-01T08:00:00", "2025-05-01T08:00:00");

        var user = createAndSaveUser("1", "user", "password");

        var response =
                mockMvc
                        .perform(
                                MockMvcRequestBuilders.post("/campaigns")
                                        .contentType(MediaType.APPLICATION_JSON.getMediaType())
                                        .content(objectMapper.writeValueAsString(dto))
                                        .header("Authorization", createJwt(user)))

                        .andExpect(MockMvcResultMatchers.status().isCreated())
                        .andReturn();

        var idResponse =
                objectMapper.readValue(response.getResponse().getContentAsString(), IdResponse.class);

        var campaign = campaignRepository.findById(idResponse.id()).get();

        assertThat(campaign.channel()).isEqualTo(dto.channel());
        assertThat(campaign.budget()).isEqualTo(dto.budget());
        assertThat(campaign.period().start()).isEqualTo(dto.startDate());
        assertThat(campaign.period().end()).isEqualTo(dto.endDate());
    }

    @Test
    void createCampaignWithInvalidDateFormat_ReturnsBadRequest() throws Exception {
        var dto = new CreateCampaignDTO(Channel.SOCIAL_MEDIA, 10_000, "2025-04-01", "2025-05-01T08:00:00");

        var user = createAndSaveUser("1", "user", "password");

        mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/campaigns")
                                .contentType(MediaType.APPLICATION_JSON.getMediaType())
                                .content(objectMapper.writeValueAsString(dto))
                                .header("Authorization", createJwt(user)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }

    @Test
    void createCampaignWithMissingField_ReturnsBadRequest() throws Exception {
        var dto = new CreateCampaignDTO(null, 10_000, "2025-04-01T08:00:00", "2025-05-01T08:00:00");

        var user = createAndSaveUser("1", "user", "password");

        mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/campaigns")
                                .contentType(MediaType.APPLICATION_JSON.getMediaType())
                                .content(objectMapper.writeValueAsString(dto))
                                .header("Authorization", createJwt(user)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }
}
