package com.objective_platform.campaign.domain.models;

import com.objective_platform.campaign.domain.exceptions.InvalidCampaignDatesException;
import com.objective_platform.campaign.domain.exceptions.InvalidCampaignPeriodException;

import java.time.LocalDateTime;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

public class Period {
    private LocalDateTime start;
    private LocalDateTime end;

    public Period(String start, String end) {
        requireValidDates(start, end);

        this.start = LocalDateTime.parse(start, ISO_LOCAL_DATE_TIME);
        this.end = LocalDateTime.parse(end, ISO_LOCAL_DATE_TIME);
    }

    private void requireValidDates(String startDate, String endDate) {
        LocalDateTime startDateParsed;
        LocalDateTime endDateParsed;

        try {
            startDateParsed = LocalDateTime.parse(startDate, ISO_LOCAL_DATE_TIME);
            endDateParsed = LocalDateTime.parse(endDate, ISO_LOCAL_DATE_TIME);
        } catch (Exception e) {
            throw new InvalidCampaignDatesException();
        }

        if (startDateParsed.isAfter(endDateParsed)) {
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
