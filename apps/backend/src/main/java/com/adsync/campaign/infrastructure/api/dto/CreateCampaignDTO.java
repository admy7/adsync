package com.adsync.campaign.infrastructure.api.dto;

import com.adsync.campaign.domain.models.Channel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;

public record CreateCampaignDTO(
    @NotNull(message = "Name is mandatory") String name,
    @NotNull(message = "Channel is mandatory") String channel,
    @NotNull(message = "Budget is mandatory") double budget,
    @Pattern(
            regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$",
            message = "Date must follow the ISO DateTime format: yyyy-MM-ddTHH:mm:ss")
        @NotBlank(message = "Start date is mandatory")
        String start,
    @Pattern(
            regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$",
            message = "Date must follow the ISO DateTime format: yyyy-MM-ddTHH:mm:ss")
        @NotBlank(message = "End date is mandatory")
        String end) {

  public Channel _channel() {
    return Channel.fromString(channel.toLowerCase());
  }

  public LocalDateTime _start() {
    return LocalDateTime.parse(start);
  }

  public LocalDateTime _end() {
    return LocalDateTime.parse(end);
  }
}
