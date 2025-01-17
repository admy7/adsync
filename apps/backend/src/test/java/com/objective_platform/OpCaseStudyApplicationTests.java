package com.objective_platform;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.assertj.core.api.Assertions.*;

@Import(OpCaseStudyTestConfiguration.class)
@SpringBootTest
public class OpCaseStudyApplicationTests {

    @Autowired
    private PostgreSQLContainer<?> postgreSQLContainer;

    @Test
    void contextLoads() {
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }
}
