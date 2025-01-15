package com.objective_platform.campaign.domain.models;

import com.objective_platform.campaign.domain.exceptions.InvalidCampaignDatesException;
import com.objective_platform.campaign.domain.exceptions.InvalidCampaignPeriodException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Period {
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private final static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Period(String startDate, String endDate) {
        requireValidDates(startDate, endDate);

        this.startDate = LocalDateTime.parse(startDate, FORMATTER);
        this.endDate = LocalDateTime.parse(endDate, FORMATTER);
    }

    private void requireValidDates(String startDate, String endDate) {
        LocalDateTime startDateParsed;
        LocalDateTime endDateParsed;

        try {
            startDateParsed = LocalDateTime.parse(startDate, FORMATTER);
            endDateParsed = LocalDateTime.parse(endDate, FORMATTER);
        } catch (Exception e) {
            throw new InvalidCampaignDatesException();
        }

        if (startDateParsed.isAfter(endDateParsed)) {
            throw new InvalidCampaignPeriodException();
        }
    }
}
