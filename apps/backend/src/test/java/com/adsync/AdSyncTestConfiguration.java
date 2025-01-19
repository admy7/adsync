package com.adsync;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class AdSyncTestConfiguration {

  @Bean
  @ServiceConnection
  PostgreSQLContainer<?> postgreSQLContainer() {
    return new PostgreSQLContainer<>(DockerImageName.parse("postgres:14.15-alpine3.21"))
        .withDatabaseName("db")
        .withUsername("user")
        .withPassword("password");
  }

  @Bean
  ObjectMapper objectMapper() {
    return new ObjectMapper();
  }
}
