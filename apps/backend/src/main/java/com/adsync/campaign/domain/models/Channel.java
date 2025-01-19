package com.adsync.campaign.domain.models;

public enum Channel {
    RADIO,
    TV,
    SOCIAL_MEDIA,
    SEARCH_ENGINE,
    ;

    public static Channel fromString(String channel) {
        switch(channel) {
            case "RADIO":
                return RADIO;
            case "TV":
                return TV;
            case "SOCIAL_MEDIA":
                return SOCIAL_MEDIA;
            case "SEARCH_ENGINE":
                return SEARCH_ENGINE;
            default:
                throw new IllegalArgumentException("Unknown channel: " + channel);
        }
    }
}
