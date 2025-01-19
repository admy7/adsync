package com.objective_platform.core.infrastructure.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.objective_platform.OpCaseStudyTestConfiguration;
import com.objective_platform.auth.application.ports.UserRepository;
import com.objective_platform.auth.application.services.JwtService;
import com.objective_platform.auth.domain.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Import(OpCaseStudyTestConfiguration.class)
public class IntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected JwtService jwtService;

    @Autowired
    protected UserRepository userRepository;

    protected String createJwt(User user) {
        return "Bearer " + jwtService.tokenize(user);
    }

    protected User createAndSaveUser(String id, String username, String password) {
        var user = new User(id, username, password);
        userRepository.save(user);

        return user;
    }
}
