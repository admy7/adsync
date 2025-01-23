package com.adsync.campaign.domain.models;

import com.adsync.campaign.domain.exceptions.InvalidChannelException;

public enum Channel {
  RADIO("Radio"),
  TV("TV"),
  SOCIAL_MEDIA("Social Media"),
  SEARCH_ENGINE("Search Engine");

  private final String value;

  Channel(String value) {
    this.value = value;
  }

  public static Channel fromString(String channel) {
    switch (channel) {
      case "radio":
        return RADIO;
      case "tv":
        return TV;
      case "social media":
        return SOCIAL_MEDIA;
      case "search engine":
        return SEARCH_ENGINE;
      default:
        throw new InvalidChannelException(channel);
    }
  }

  public String getValue() {
    return value;
  }
}
