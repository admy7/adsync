package com.objective_platform.auth.application.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.objective_platform.OpCaseStudyTestConfiguration;
import com.objective_platform.auth.application.ports.UserRepository;
import com.objective_platform.auth.application.services.PasswordHasher;
import com.objective_platform.auth.domain.models.User;
import com.objective_platform.auth.infrastructure.api.dto.CreateUserDTO;
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
public class CreateUserE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordHasher passwordHasher;

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
        User user = new User("1", "user@gmail.com", "password");
        userRepository.save(user);

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
