package com.adsync.campaign.domain.models;

import java.time.LocalDateTime;

public class Campaign {

    private String id;
    private String name;
    private Channel channel;
    private Double budget;
    private Period period;

    public Campaign(String id, String name, Channel channel, Double budget, Period period) {
        this.id = id;
        this.name = name;
        this.channel = channel;
        this.budget = budget;
        this.period = period;
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

    public Period period() {
        return period;
    }

    public void updateName(String name) {
        this.name = name;
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
        return new Campaign(id, name, channel, budget, new Period(period.start(), period.end()));
    }
}
