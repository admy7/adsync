package com.adsync.campaign.domain.models;

import java.time.LocalDateTime;

public class Campaign {

  private String id;
  private Channel channel;
  private Double budget;
  private Period period;

  public Campaign(String id, Channel channel, Double budget, Period period) {
    this.id = id;
    this.channel = channel;
    this.budget = budget;
    this.period = period;
  }

  public String id() {
    return id;
  }

  public Channel channel() {
    return channel;
  }

  public Double budget() {
    return budget;
  }

  public Period period() {
    return period;
  }

  public void updateChannel(Channel channel) {
    this.channel = channel;
  }

  public void updateBudget(Double budget) {
    this.budget = budget;
  }

  public void updateStart(LocalDateTime start) {
    this.period = new Period(start, period.end());
  }

  public void updateEnd(LocalDateTime end) {
    this.period = new Period(period.start(), end);
  }

  public Campaign deepClone() {
    return new Campaign(id, channel, budget, new Period(period.start(), period.end()));
  }
}
