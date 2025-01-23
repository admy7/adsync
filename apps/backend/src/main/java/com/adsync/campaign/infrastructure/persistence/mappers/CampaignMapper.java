package com.adsync.campaign.infrastructure.persistence.mappers;

import com.adsync.campaign.domain.models.Campaign;
import com.adsync.campaign.domain.models.Period;
import com.adsync.campaign.infrastructure.persistence.models.SqlCampaign;

public class CampaignMapper {

  public static Campaign toDomain(SqlCampaign sqlCampaign) {
    return new Campaign(
        sqlCampaign.id(),
        sqlCampaign.name(),
        sqlCampaign.channel(),
        sqlCampaign.budget(),
        new Period(sqlCampaign.startDate(), sqlCampaign.endDate()));
  }

  public static SqlCampaign toSql(Campaign campaign) {
    return new SqlCampaign(
        campaign.id(),
        campaign.name(),
        campaign.channel(),
        campaign.budget(),
        campaign.period().start(),
        campaign.period().end());
  }
}
