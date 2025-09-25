package com.projectlos.loan_service;

import com.projectlos.loan_service.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class LoanServiceApplicationTests {

    @Test
    void contextLoads() {
        // Test passes if Spring context loads successfully
    }

}
