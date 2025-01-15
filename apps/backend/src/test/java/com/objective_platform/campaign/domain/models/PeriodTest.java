package com.objective_platform.campaign.domain.models;

import com.objective_platform.campaign.domain.exceptions.InvalidCampaignDatesException;
import com.objective_platform.campaign.domain.exceptions.InvalidCampaignPeriodException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PeriodTest {

    @Test
    public void periodWithValidDates_shouldNotThrow() {
        new Period("2025-02-01 08:00:00", "2025-03-01 14:00:00");
    }

    @Test
    public void periodWithStartDatePriorToEndDate_shouldThrow() {
        assertThatThrownBy(() -> new Period("2025-03-01 08:00:00", "2025-02-01 14:00:00"))
                .isInstanceOf(InvalidCampaignPeriodException.class)
                .hasMessage("Start date must be prior to end date");
    }

    @Test
    public void whenCreatingCampaignWithInvalidDates_shouldThrow() {
        assertThatThrownBy(() -> new Period("2025-03-01", "2025-02-01 14:00:00"))
                .isInstanceOf(InvalidCampaignDatesException.class)
                .hasMessage("Invalid campaign dates");
    }
}
