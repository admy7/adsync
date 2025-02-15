package com.adsync.auth.infrastructure.api;

import static org.assertj.core.api.Assertions.assertThat;

import com.adsync.auth.application.services.hasher.PasswordHasher;
import com.adsync.auth.infrastructure.api.dto.CreateUserDTO;
import com.adsync.core.infrastructure.api.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.com.github.dockerjava.core.MediaType;

public class CreateUserE2ETest extends IntegrationTest {

  @Autowired private PasswordHasher passwordHasher;

  @BeforeEach
  void setUp() {
    userRepository.clear();
  }

  @Test
  void createUser() throws Exception {
    var dto = new CreateUserDTO("user@gmail.com", "password");

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON.getMediaType())
                .content(objectMapper.writeValueAsString(dto)))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andReturn();

    var actualUser = userRepository.findByEmail(dto.email()).get();

    assertThat(passwordHasher.match(dto.password(), actualUser.password())).isTrue();
  }

  @Test
  void createUserWithUnavailableEmail_SendBadRequest() throws Exception {
    createAndSaveUser("1", "user@gmail.com", "password");

    var dto = new CreateUserDTO("user@gmail.com", "password");

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON.getMediaType())
                .content(objectMapper.writeValueAsString(dto)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andReturn();
  }
}
