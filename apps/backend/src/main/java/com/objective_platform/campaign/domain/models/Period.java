package com.objective_platform.campaign.domain.models;

import java.time.LocalDateTime;

public class Period {
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public Period(String startDate, String endDate) {
        this.startDate = LocalDateTime.parse(startDate);
        this.endDate = LocalDateTime.parse(endDate);
    }
}
