package com.objective_platform.campaign.domain.models;

public class Campaign {

    private String id;
    private Channel channel;
    private double budget;
    private Period period;

    public Campaign(String id, String channel, double budget, Period period) {
        this.id = id;
        this.channel = Channel.valueOf(channel);
        this.budget = budget;
        this.period = period;
    }

    public String id() {
        return id;
    }

    public Channel channel() {
        return channel;
    }

    public double budget() {
        return budget;
    }

    public Period period() {
        return period;
    }

    public void updateBudget(double budget) {
        this.budget = budget;
    }

    public void updateChannel(String channel) {
        this.channel = Channel.valueOf(channel);
    }

    public void updateStartDate(String startDate) {
        this.period = new Period(startDate, period.end().toString());
    }

    public void updateEndDate(String endDate) {
        this.period = new Period(period.start().toString(), endDate);
    }

    public Campaign deepClone() {
        return new Campaign(id, channel.toString(), budget, new Period(period.start().toString(), period.end().toString()));
    }
}
