package com.projectlos.loan_service.service;

import com.projectlos.loan_service.dto.LoanApplicationRequest;
import com.projectlos.loan_service.entity.LoanApplication;
import com.projectlos.loan_service.entity.ScoringResult;
import com.projectlos.loan_service.enums.LoanStatus;
import com.projectlos.loan_service.enums.ProductType;
import com.projectlos.loan_service.mapper.LoanApplicationMapper;
import com.projectlos.loan_service.repository.LoanApplicationRepository;
import com.projectlos.loan_service.scoring.ScoringEngine;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanApplicationServiceTest {

    @Mock
    private LoanApplicationRepository loanApplicationRepository;

    @Mock
    private LoanApplicationMapper loanApplicationMapper;

    @Mock
    private ProcessEngine processEngine;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ScoringEngine scoringEngine;

    @InjectMocks
    private LoanApplicationService loanApplicationService;

    private LoanApplicationRequest loanApplicationRequest;
    private LoanApplication loanApplication;
    private ProcessInstance processInstance;
    private ScoringResult scoringResult;

    @BeforeEach
    void setUp() {
        loanApplicationRequest = LoanApplicationRequest.builder()
                .customerId("550e8400-e29b-41d4-a716-446655440000")
                .productType(ProductType.PERSONAL)
                .requestedAmount(new BigDecimal("50000000"))
                .tenureMonths(12)
                .interestRate(new BigDecimal("12.5"))
                .purpose("Personal Loan")
                .build();

        loanApplication = LoanApplication.builder()
                .id(UUID.randomUUID())
                .applicationNumber("APP-123456")
                .customerId("550e8400-e29b-41d4-a716-446655440000")
                .productType(ProductType.PERSONAL)
                .requestedAmount(new BigDecimal("50000000"))
                .tenureMonths(12)
                .interestRate(new BigDecimal("12.5"))
                .purpose("Personal Loan")
                .status(LoanStatus.SUBMITTED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        processInstance = mock(ProcessInstance.class);

        scoringResult = ScoringResult.builder()
                .score(85)
                .scoringModel("PERSONAL_SCORING_V1")
                .scoringDetails("Test scoring details")
                .scoredAt(LocalDateTime.now())
                .build();
    }

    @Test
    void createLoanApplication_Success() {
        // Given
        RuntimeService runtimeService = mock(RuntimeService.class);
        when(processEngine.getRuntimeService()).thenReturn(runtimeService);
        when(processInstance.getId()).thenReturn("proc_inst_123");
        when(runtimeService.startProcessInstanceByKey(eq("loanApplicationProcess"), any(Map.class))).thenReturn(processInstance);
        when(scoringEngine.calculateScore(any(LoanApplicationRequest.class))).thenReturn(scoringResult);
        when(loanApplicationMapper.toEntityWithInherit(any(LoanApplicationRequest.class))).thenReturn(loanApplication);
        when(loanApplicationRepository.save(any(LoanApplication.class))).thenReturn(loanApplication);
        when(loanApplicationMapper.toResponseWithInherit(any(LoanApplication.class))).thenReturn(mock(com.projectlos.loan_service.dto.LoanApplicationResponse.class));

        // When
        var result = loanApplicationService.createLoanApplication(loanApplicationRequest);

        // Then
        assertNotNull(result);
        verify(scoringEngine, times(1)).calculateScore(any(LoanApplicationRequest.class));
        verify(loanApplicationRepository, times(1)).save(any(LoanApplication.class));
        verify(kafkaTemplate, times(1)).send(anyString(), anyString(), anyString());
    }

    @Test
    void getLoanApplicationById_Success() {
        // Given
        UUID testId = loanApplication.getId();
        when(loanApplicationRepository.findById(testId)).thenReturn(Optional.of(loanApplication));
        when(loanApplicationMapper.toResponseWithInherit(any(LoanApplication.class))).thenReturn(mock(com.projectlos.loan_service.dto.LoanApplicationResponse.class));

        // When
        var result = loanApplicationService.getLoanApplicationById(testId);

        // Then
        assertNotNull(result);
        verify(loanApplicationRepository, times(1)).findById(testId);
        verify(loanApplicationMapper, times(1)).toResponseWithInherit(any(LoanApplication.class));
    }

    @Test
    void getLoanApplicationById_NotFound_ThrowsException() {
        // Given
        UUID testId = UUID.randomUUID();
        when(loanApplicationRepository.findById(testId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> loanApplicationService.getLoanApplicationById(testId));
        verify(loanApplicationRepository, times(1)).findById(testId);
    }
}
