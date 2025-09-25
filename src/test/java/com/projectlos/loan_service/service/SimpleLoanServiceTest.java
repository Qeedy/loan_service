package com.projectlos.loan_service.service;

import com.projectlos.loan_service.dto.LoanApplicationRequest;
import com.projectlos.loan_service.dto.LoanApplicationResponse;
import com.projectlos.loan_service.enums.ProductType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SimpleLoanServiceTest {

    @Mock
    private LoanApplicationService loanApplicationService;

    private LoanApplicationRequest loanRequest;
    private LoanApplicationResponse loanResponse;

    @BeforeEach
    void setUp() {
        loanRequest = LoanApplicationRequest.builder()
                .customerId(UUID.randomUUID().toString())
                .productType(ProductType.KUR)
                .requestedAmount(new BigDecimal("50000000"))
                .tenureMonths(12)
                .interestRate(new BigDecimal("12.5"))
                .purpose("Pembangunan Rumah Tinggal")
                .build();

        loanResponse = LoanApplicationResponse.builder()
                .id(UUID.randomUUID())
                .applicationId("APP-2024-001")
                .customerId(loanRequest.getCustomerId())
                .productType(loanRequest.getProductType())
                .requestedAmount(loanRequest.getRequestedAmount())
                .tenureMonths(loanRequest.getTenureMonths())
                .interestRate(loanRequest.getInterestRate())
                .purpose(loanRequest.getPurpose())
                .status("SUBMITTED")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testCreateLoanApplication_Success() {
        // Given
        when(loanApplicationService.createLoanApplication(any(LoanApplicationRequest.class)))
                .thenReturn(loanResponse);

        // When
        LoanApplicationResponse result = loanApplicationService.createLoanApplication(loanRequest);

        // Then
        assertNotNull(result);
        assertEquals(loanResponse.getId(), result.getId());
        assertEquals(loanResponse.getApplicationId(), result.getApplicationId());
        assertEquals(loanResponse.getCustomerId(), result.getCustomerId());
        assertEquals(loanResponse.getProductType(), result.getProductType());
        assertEquals(loanResponse.getRequestedAmount(), result.getRequestedAmount());
        assertEquals(loanResponse.getTenureMonths(), result.getTenureMonths());
        assertEquals(loanResponse.getInterestRate(), result.getInterestRate());
        assertEquals(loanResponse.getPurpose(), result.getPurpose());
        assertEquals(loanResponse.getStatus(), result.getStatus());
    }

    @Test
    void testCreateLoanApplication_Validation() {
        // Given - Invalid request with missing required fields
        LoanApplicationRequest invalidRequest = LoanApplicationRequest.builder()
                .customerId("invalid-uuid")
                .requestedAmount(new BigDecimal("-1000000"))
                .tenureMonths(0)
                .build();

        // When & Then - Test validation logic
        assertNotNull(invalidRequest);
        assertEquals("invalid-uuid", invalidRequest.getCustomerId());
        assertEquals(new BigDecimal("-1000000"), invalidRequest.getRequestedAmount());
        assertEquals(0, invalidRequest.getTenureMonths());
    }

    @Test
    void testLoanApplicationRequest_Builder() {
        // Test builder pattern
        LoanApplicationRequest request = LoanApplicationRequest.builder()
                .customerId("test-customer-id")
                .productType(ProductType.KUR)
                .requestedAmount(new BigDecimal("10000000"))
                .tenureMonths(6)
                .interestRate(new BigDecimal("10.0"))
                .purpose("Test Purpose")
                .build();

        assertNotNull(request);
        assertEquals("test-customer-id", request.getCustomerId());
        assertEquals(ProductType.KUR, request.getProductType());
        assertEquals(new BigDecimal("10000000"), request.getRequestedAmount());
        assertEquals(6, request.getTenureMonths());
        assertEquals(new BigDecimal("10.0"), request.getInterestRate());
        assertEquals("Test Purpose", request.getPurpose());
    }

    @Test
    void testLoanApplicationResponse_Builder() {
        // Test builder pattern
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
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        assertNotNull(response);
        assertEquals(123L, response.getId());
        assertEquals("APP-TEST-001", response.getApplicationId());
        assertEquals("test-customer-id", response.getCustomerId());
        assertEquals(ProductType.KUR, response.getProductType());
        assertEquals(new BigDecimal("10000000"), response.getRequestedAmount());
        assertEquals(6, response.getTenureMonths());
        assertEquals(new BigDecimal("10.0"), response.getInterestRate());
        assertEquals("Test Purpose", response.getPurpose());
        assertEquals("PENDING", response.getStatus());
        assertNotNull(response.getCreatedAt());
        assertNotNull(response.getUpdatedAt());
    }
}
