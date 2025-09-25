package com.projectlos.loan_service.integration;

import com.projectlos.loan_service.dto.LoanApplicationRequest;
import com.projectlos.loan_service.dto.LoanApplicationResponse;
import com.projectlos.loan_service.enums.ProductType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class LoanServiceIntegrationTest {

    @LocalServerPort
    private int port;

    private TestRestTemplate restTemplate;
    private String baseUrl;

    @BeforeEach
    void setUp() {
        restTemplate = new TestRestTemplate();
        baseUrl = "http://localhost:" + port;
    }

    @Test
    void testHealthCheck() {
        // Given
        String url = baseUrl + "/actuator/health";

        // When
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testCreateLoanApplication_WithoutAuth() {
        // Given
        String url = baseUrl + "/api/loans";
        
        LoanApplicationRequest request = LoanApplicationRequest.builder()
                .customerId(UUID.randomUUID().toString())
                .productType(ProductType.KUR)
                .requestedAmount(new BigDecimal("50000000"))
                .tenureMonths(12)
                .interestRate(new BigDecimal("12.5"))
                .purpose("Pembangunan Rumah Tinggal")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoanApplicationRequest> entity = new HttpEntity<>(request, headers);

        // When
        ResponseEntity<String> response = restTemplate.exchange(
                url, 
                HttpMethod.POST, 
                entity, 
                String.class
        );

        // Then
        // Should return 401 or 403 due to missing authentication
        assertTrue(response.getStatusCode() == HttpStatus.UNAUTHORIZED || 
                  response.getStatusCode() == HttpStatus.FORBIDDEN);
    }

    @Test
    void testGetLoanApplication_WithoutAuth() {
        // Given
        String url = baseUrl + "/api/loans/1";

        // When
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // Then
        // Should return 401 or 403 due to missing authentication
        assertTrue(response.getStatusCode() == HttpStatus.UNAUTHORIZED || 
                  response.getStatusCode() == HttpStatus.FORBIDDEN);
    }

    @Test
    void testInvalidEndpoint() {
        // Given
        String url = baseUrl + "/api/invalid-endpoint";

        // When
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // Then
        // Should return 401 or 404 due to security or endpoint not found
        assertTrue(response.getStatusCode() == HttpStatus.UNAUTHORIZED || 
                  response.getStatusCode() == HttpStatus.NOT_FOUND);
    }

    @Test
    void testLoanApplicationRequest_Validation() {
        // Test DTO validation
        LoanApplicationRequest request = LoanApplicationRequest.builder()
                .customerId("test-customer-id")
                .productType(ProductType.KUR)
                .requestedAmount(new BigDecimal("1000000")) // Minimum amount
                .tenureMonths(1) // Minimum tenure
                .interestRate(new BigDecimal("0.01")) // Minimum interest rate
                .purpose("Test Purpose")
                .build();

        assertNotNull(request);
        assertEquals("test-customer-id", request.getCustomerId());
        assertEquals(ProductType.KUR, request.getProductType());
        assertEquals(new BigDecimal("1000000"), request.getRequestedAmount());
        assertEquals(1, request.getTenureMonths());
        assertEquals(new BigDecimal("0.01"), request.getInterestRate());
        assertEquals("Test Purpose", request.getPurpose());
    }

    @Test
    void testLoanApplicationResponse_Structure() {
        // Test DTO structure
        LoanApplicationResponse response = LoanApplicationResponse.builder()
                .id(UUID.randomUUID())
                .applicationId("APP-TEST-001")
                .customerId("test-customer-id")
                .productType(ProductType.KUR)
                .requestedAmount(new BigDecimal("10000000"))
                .tenureMonths(6)
                .interestRate(new BigDecimal("10.0"))
                .purpose("Test Purpose")
                .status("PENDING")
                .build();

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("APP-TEST-001", response.getApplicationId());
        assertEquals("test-customer-id", response.getCustomerId());
        assertEquals(ProductType.KUR, response.getProductType());
        assertEquals(new BigDecimal("10000000"), response.getRequestedAmount());
        assertEquals(6, response.getTenureMonths());
        assertEquals(new BigDecimal("10.0"), response.getInterestRate());
        assertEquals("Test Purpose", response.getPurpose());
        assertEquals("PENDING", response.getStatus());
    }
}
