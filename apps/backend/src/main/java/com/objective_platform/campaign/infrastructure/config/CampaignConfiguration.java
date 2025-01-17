package com.objective_platform.campaign.infrastructure.config;

import com.objective_platform.campaign.application.ports.CampaignRepository;
import com.objective_platform.campaign.application.usecases.CreateCampaignCommandHandler;
import com.objective_platform.campaign.infrastructure.persistence.SqlCampaignRepository;
import com.objective_platform.campaign.infrastructure.persistence.mappers.CampaignMapper;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CampaignConfiguration {

    @Bean
    public CampaignMapper campaignMapper() {
        return new CampaignMapper();
    }

    @Bean
    public CampaignRepository campaignRepository(CampaignMapper mapper, EntityManager entityManager) {
        return new SqlCampaignRepository(mapper, entityManager);
    }

    @Bean
    public CreateCampaignCommandHandler createCampaignCommandHandler(CampaignRepository campaignRepository) {
        return new CreateCampaignCommandHandler(campaignRepository);
    }
}
