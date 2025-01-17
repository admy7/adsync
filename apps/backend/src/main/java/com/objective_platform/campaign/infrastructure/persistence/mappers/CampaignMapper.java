package com.objective_platform.campaign.infrastructure.persistence.mappers;

import com.objective_platform.campaign.domain.models.Campaign;
import com.objective_platform.campaign.domain.models.Period;
import com.objective_platform.campaign.infrastructure.persistence.models.SqlCampaign;

public class CampaignMapper {

    public static Campaign toDomain(SqlCampaign sqlCampaign) {
        return new Campaign(
            sqlCampaign.id(),
            sqlCampaign.channel(),
            sqlCampaign.budget(),
            new Period(sqlCampaign.startDate(), sqlCampaign.endDate())
        );
    }

    public static SqlCampaign toSql(Campaign campaign) {
        return new SqlCampaign(
            campaign.id(),
            campaign.channel(),
            campaign.budget(),
            campaign.period().start(),
            campaign.period().end()
        );
    }
}
