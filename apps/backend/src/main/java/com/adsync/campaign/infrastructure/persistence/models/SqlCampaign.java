package com.adsync.campaign.infrastructure.persistence.models;

import com.adsync.campaign.domain.models.Channel;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "campaigns")
public class SqlCampaign {

  @Id private String id;

  private String name;

  private Channel channel;

  private Double budget;

  private LocalDateTime startDate;

  private LocalDateTime endDate;

  public SqlCampaign() {}

  public SqlCampaign(
      String id,
      String name,
      Channel channel,
      Double budget,
      LocalDateTime startDate,
      LocalDateTime endDate) {
    this.id = id;
    this.name = name;
    this.channel = channel;
    this.budget = budget;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public String id() {
    return id;
  }

  public String name() {
    return name;
  }

  public Channel channel() {
    return channel;
  }

  public Double budget() {
    return budget;
  }

  public LocalDateTime startDate() {
    return startDate;
  }

  public LocalDateTime endDate() {
    return endDate;
  }
}
