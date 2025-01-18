package com.objective_platform.campaign.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.objective_platform.OpCaseStudyTestConfiguration;
import com.objective_platform.campaign.application.ports.CampaignRepository;
import com.objective_platform.campaign.domain.models.Campaign;
import com.objective_platform.campaign.domain.models.Channel;
import com.objective_platform.campaign.domain.models.Period;
import com.objective_platform.campaign.infrastructure.api.dto.GetAllCampaignsDTO;
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
public class GetAllCampaignsE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        campaignRepository.clear();

        Period period1 = new Period(LocalDateTime.parse("2025-04-01T08:00:00"), LocalDateTime.parse("2025-05-01T08:00:00"));
        Campaign campaign1 = new Campaign("1", Channel.SOCIAL_MEDIA, 10_000d, period1);

        Period period2 = new Period(LocalDateTime.parse("2025-09-01T08:00:00"), LocalDateTime.parse("2025-12-01T08:00:00"));
        Campaign campaign2 = new Campaign("2", Channel.RADIO, 2000d, period2);


        campaignRepository.save(campaign1);
        campaignRepository.save(campaign2);
    }

    @Test
    void getAllCampaigns() throws Exception {
        var response = mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/campaigns"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        GetAllCampaignsDTO dto = objectMapper.readValue(response.getResponse().getContentAsString(), GetAllCampaignsDTO.class);

        assertThat(dto.campaigns().get(0).id()).isEqualTo("1");
        assertThat(dto.campaigns().get(1).id()).isEqualTo("2");
    }
}
