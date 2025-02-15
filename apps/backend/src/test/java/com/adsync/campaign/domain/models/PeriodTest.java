package com.adsync.campaign.domain.models;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.adsync.campaign.domain.exceptions.InvalidCampaignPeriodException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class PeriodTest {

  @Test
  public void periodWithValidDates_shouldNotThrow() {
    LocalDateTime start = LocalDateTime.parse("2025-02-01T08:00:00");
    LocalDateTime end = LocalDateTime.parse("2025-03-01T08:00:00");
    new Period(start, end);
  }

  @Test
  public void periodWithStartDatePriorToEndDate_shouldThrow() {
    LocalDateTime start = LocalDateTime.parse("2025-04-01T08:00:00");
    LocalDateTime end = LocalDateTime.parse("2025-03-01T08:00:00");

    assertThatThrownBy(() -> new Period(start, end))
        .isInstanceOf(InvalidCampaignPeriodException.class)
        .hasMessage("Start date must be prior to end date");
  }
}
