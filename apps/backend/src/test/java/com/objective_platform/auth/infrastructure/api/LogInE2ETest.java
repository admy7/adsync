package com.objective_platform.auth.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.objective_platform.OpCaseStudyTestConfiguration;
import com.objective_platform.auth.application.ports.UserRepository;
import com.objective_platform.auth.application.services.JwtService;
import com.objective_platform.auth.application.services.PasswordHasher;
import com.objective_platform.auth.domain.models.AuthUser;
import com.objective_platform.auth.domain.models.User;
import com.objective_platform.auth.domain.viewmodels.TokenResponse;
import com.objective_platform.auth.infrastructure.api.dto.LogInDTO;
import com.objective_platform.core.infrastructure.api.errors.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.com.github.dockerjava.core.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@Import(OpCaseStudyTestConfiguration.class)
public class LogInE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordHasher passwordHasher;

    @BeforeEach
    void setUp() {
        userRepository.clear();
    }

    @Test
    void logUserIn() throws Exception {
        var user = new User("1", "user@gmail.com", passwordHasher.hash("password"));
        userRepository.save(user);

        var dto = new LogInDTO("user@gmail.com", "password");

        var response = mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON.getMediaType())
                                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        TokenResponse tokenResponse = objectMapper.readValue(response.getResponse().getContentAsString(), TokenResponse.class);

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

        ErrorResponse errorResponse = objectMapper.readValue(response.getResponse().getContentAsString(), ErrorResponse.class);

        assertThat(errorResponse.message()).isEqualTo("Invalid credentials");
    }

    @Test
    void logUserInWithInvalidPassword_SendBadRequest() throws Exception {
        var user = new User("1", "user@gmail.com", passwordHasher.hash("password"));
        userRepository.save(user);

        var dto = new LogInDTO("user@gmail.com", "1234password");

        var response = mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON.getMediaType())
                                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        ErrorResponse errorResponse = objectMapper.readValue(response.getResponse().getContentAsString(), ErrorResponse.class);

        assertThat(errorResponse.message()).isEqualTo("Invalid credentials");
    }
}
