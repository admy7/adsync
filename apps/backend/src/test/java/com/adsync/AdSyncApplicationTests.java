package com.adsync;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;

@Import(AdSyncTestConfiguration.class)
@SpringBootTest
public class AdSyncApplicationTests {

  @Autowired private PostgreSQLContainer<?> postgreSQLContainer;

  @Test
  void contextLoads() {
    assertThat(postgreSQLContainer.isCreated()).isTrue();
    assertThat(postgreSQLContainer.isRunning()).isTrue();
  }
}
