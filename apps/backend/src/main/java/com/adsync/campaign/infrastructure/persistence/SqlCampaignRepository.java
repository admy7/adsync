package com.adsync.campaign.infrastructure.persistence;

import com.adsync.campaign.application.ports.CampaignRepository;
import com.adsync.campaign.domain.models.Campaign;
import com.adsync.campaign.infrastructure.persistence.mappers.CampaignMapper;
import com.adsync.campaign.infrastructure.persistence.models.SqlCampaign;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public class SqlCampaignRepository implements CampaignRepository {
  private final CampaignMapper mapper;
  private final EntityManager entityManager;

  public SqlCampaignRepository(CampaignMapper mapper, EntityManager entityManager) {
    this.mapper = mapper;
    this.entityManager = entityManager;
  }

  @Override
  public Optional<Campaign> findById(String id) {
    return Optional.ofNullable(entityManager.find(SqlCampaign.class, id))
        .map(CampaignMapper::toDomain);
  }

  @Override
  public List<Campaign> findAll() {
    return entityManager
        .createQuery("SELECT c FROM SqlCampaign c", SqlCampaign.class)
        .getResultList()
        .stream()
        .map(CampaignMapper::toDomain)
        .toList();
  }

  @Override
  public void save(Campaign campaign) {
    SqlCampaign sqlCampaign = mapper.toSql(campaign);

    entityManager.merge(sqlCampaign);
  }

  @Override
  public void delete(String id) {
    entityManager.remove(entityManager.find(SqlCampaign.class, id));
  }

  @Override
  public void clear() {
    entityManager.createQuery("DELETE FROM SqlCampaign").executeUpdate();
  }
}
