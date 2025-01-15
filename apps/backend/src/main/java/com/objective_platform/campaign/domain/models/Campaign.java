package com.objective_platform.campaign.domain.models;

import java.util.UUID;

public class Campaign {

    private String id;
    private String channel;
    private double budget;
    private Period period;

    public Campaign(String channel, double budget, Period period) {
        this.id = UUID.randomUUID().toString();
        this.channel = channel;
        this.budget = budget;
        this.period = period;
    }

    public String id() {
        return id;
    }
}
