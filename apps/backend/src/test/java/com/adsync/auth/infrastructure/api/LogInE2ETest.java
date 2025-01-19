package com.adsync.auth.infrastructure.api;

import com.adsync.auth.application.services.hasher.PasswordHasher;
import com.adsync.auth.domain.models.AuthUser;
import com.adsync.auth.domain.viewmodels.TokenResponse;
import com.adsync.auth.infrastructure.api.dto.LogInDTO;
import com.adsync.core.infrastructure.api.IntegrationTest;
import com.adsync.core.infrastructure.api.errors.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.com.github.dockerjava.core.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class LogInE2ETest extends IntegrationTest {

    @Autowired
    private PasswordHasher passwordHasher;

    @BeforeEach
    void setUp() {
        userRepository.clear();
    }

    @Test
    void logUserIn() throws Exception {
        var user = createAndSaveUser("1", "user@gmail.com", passwordHasher.hash("password"));

        var dto = new LogInDTO("user@gmail.com", "password");

        var response = mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON.getMediaType())
                                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var tokenResponse = objectMapper.readValue(response.getResponse().getContentAsString(), TokenResponse.class);

        AuthUser loggedInUser = jwtService.parse(tokenResponse.token());

        assertThat(loggedInUser.id()).isEqualTo(user.id());
        assertThat(loggedInUser.email()).isEqualTo(user.email());
    }

    @Test
    void logUserInWithInvalidUsername_SendBadRequest() throws Exception {
        var dto = new LogInDTO("user@gmail.com", "password");

        var response = mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON.getMediaType())
                                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var errorResponse = objectMapper.readValue(response.getResponse().getContentAsString(), ErrorResponse.class);

        assertThat(errorResponse.message()).isEqualTo("Invalid credentials");
    }

    @Test
    void logUserInWithInvalidPassword_SendBadRequest() throws Exception {
        var user = createAndSaveUser("1", "user@gmail.com", passwordHasher.hash("password"));

        var dto = new LogInDTO("user@gmail.com", "1234password");

        var response = mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON.getMediaType())
                                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var errorResponse = objectMapper.readValue(response.getResponse().getContentAsString(), ErrorResponse.class);

        assertThat(errorResponse.message()).isEqualTo("Invalid credentials");
    }
}
