package com.adsync.campaign.infrastructure.config;

import com.adsync.campaign.application.ports.CampaignRepository;
import com.adsync.campaign.application.usecases.*;
import com.adsync.campaign.infrastructure.persistence.SqlCampaignRepository;
import com.adsync.campaign.infrastructure.persistence.mappers.CampaignMapper;
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
  public CreateCampaignCommandHandler createCampaignCommandHandler(
      CampaignRepository campaignRepository) {
    return new CreateCampaignCommandHandler(campaignRepository);
  }

  @Bean
  public DeleteCampaignCommandHandler deleteCampaignCommandHandler(
      CampaignRepository campaignRepository) {
    return new DeleteCampaignCommandHandler(campaignRepository);
  }

  @Bean
  public UpdateCampaignCommandHandler updateCampaignCommandHandler(
      CampaignRepository campaignRepository) {
    return new UpdateCampaignCommandHandler(campaignRepository);
  }

  @Bean
  public GetAllCampaignsQuery getAllCampaignsQuery(CampaignRepository campaignRepository) {
    return new GetAllCampaignsQuery(campaignRepository);
  }

  @Bean
  public GetCampaignByIdQuery getCampaignByIdQuery(CampaignRepository campaignRepository) {
    return new GetCampaignByIdQuery(campaignRepository);
  }
}
