package com.adsync.campaign.domain.models;

import com.adsync.campaign.domain.exceptions.InvalidCampaignPeriodException;
import java.time.LocalDateTime;

public class Period {
  private LocalDateTime start;
  private LocalDateTime end;

  public Period(LocalDateTime start, LocalDateTime end) {
    requireValidDates(start, end);

    this.start = start;
    this.end = end;
  }

  private void requireValidDates(LocalDateTime start, LocalDateTime end) {
    if (start.isAfter(end)) {
      throw new InvalidCampaignPeriodException();
    }
  }

  public LocalDateTime start() {
    return start;
  }

  public LocalDateTime end() {
    return end;
  }
}
