package com.adsync.campaign.application.usecases;

import static org.assertj.core.api.Assertions.*;

import com.adsync.campaign.domain.models.Channel;
import com.adsync.campaign.domain.viewmodels.IdResponse;
import com.adsync.campaign.infrastructure.persistence.InMemoryCampaignRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class CreateCampaignCommandTest {

  @Test
  public void createCampaign() {
    LocalDateTime start = LocalDateTime.parse("2025-02-01T08:00:00");
    LocalDateTime end = LocalDateTime.parse("2025-03-01T14:00:00");
    var command = new CreateCampaignCommand(Channel.TV, 2500d, start, end);

    var repository = new InMemoryCampaignRepository();
    var handler = new CreateCampaignCommandHandler(repository);

    IdResponse response = handler.handle(command);

    var result = repository.findById(response.id()).get();

    assertThat(result).isNotNull();
    assertThat(result.channel()).isEqualTo(Channel.TV);
    assertThat(result.budget()).isEqualTo(2500);
    assertThat(result.period().start()).isEqualTo("2025-02-01T08:00:00");
    assertThat(result.period().end()).isEqualTo("2025-03-01T14:00:00");
  }
}
