package com.adsync.campaign.domain.exceptions;

public class InvalidChannelException extends RuntimeException {
  public InvalidChannelException(String channel) {
    super("Invalid channel: " + channel);
  }
}
