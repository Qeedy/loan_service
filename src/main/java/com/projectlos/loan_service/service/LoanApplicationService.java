package com.projectlos.loan_service.service;

import com.projectlos.loan_service.dto.LoanApplicationRequest;
import com.projectlos.loan_service.dto.LoanApplicationResponse;
import com.projectlos.loan_service.dto.LoanApplicationListResponse;
import com.projectlos.loan_service.entity.LoanApplication;
import com.projectlos.loan_service.entity.LoanDecision;
import com.projectlos.loan_service.entity.ScoringResult;
import com.projectlos.loan_service.enums.LoanStatus;
import com.projectlos.loan_service.enums.ProductType;
import com.projectlos.loan_service.exception.LoanApplicationNotFoundException;
import com.projectlos.loan_service.mapper.LoanApplicationMapper;
import com.projectlos.loan_service.repository.LoanApplicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.ProcessEngine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LoanApplicationService {
    
    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanApplicationMapper loanApplicationMapper;
    private final ProcessEngine processEngine;
    private final DocumentSequenceService documentSequenceService;
    
    public LoanApplicationResponse createLoanApplication(LoanApplicationRequest request) {
        log.info("Creating new loan application for customer: {}", request.getCustomerId());
        // 1. Calculate scoring
        // ScoringResult scoringResult = scoringEngine.calculateScore(request);
        // // 2. Make decision based on scoring
        // LoanDecision decision = makeDecision(scoringResult, request);
        // log.info("Decision made for customer {}: {}", request.getCustomerId(), decision.getDecision());
        
        // 3. Start Flowable process with scoring data
        String applicationNumber = documentSequenceService.generateNextSequence("LN");
        // 4. Create loan application entity with inherited relationships
        LoanApplication loanApplication = loanApplicationMapper.toEntityWithInherit(request);
        loanApplication.setApplicationNumber(applicationNumber);
        loanApplication.setStatus(LoanStatus.DRAFT);
        
        // Store scoring and decision information
        // loanApplication.setScore(scoringResult.getScore());
        // loanApplication.setScoreGrade(scoringResult.getScoreGrade());
        // loanApplication.setScoringModel(scoringResult.getScoringModel());
        // loanApplication.setScoringDetails(scoringResult.getScoringDetails());
        // loanApplication.setDecisionReason(decision.getDecisionReason());
        
        // If approved, set approved details
        // if (decision.getDecision().equals("APPROVED")) {
        //     loanApplication.setApprovedAmount(decision.getApprovedAmount());
        //     loanApplication.setApprovedBy("SYSTEM");
        //     loanApplication.setApprovedAt(LocalDateTime.now());
        // }
        LoanApplication savedApplication = loanApplicationRepository.save(loanApplication);
        
        Map<String, Object> variables = new HashMap<>();
        variables.put("customerId", request.getCustomerId());
        variables.put("customerName", request.getCustomerName());
        variables.put("applicationNumber", applicationNumber);
        variables.put("requestedAmount", request.getRequestedAmount());
        variables.put("tenureMonths", request.getTenureMonths());
        variables.put("interestRate", request.getInterestRate());
        variables.put("purpose", request.getPurpose());
        variables.put("productType", request.getProductType());
        variables.put("applicationId", savedApplication.getId().toString());
        processEngine.getRuntimeService()
                .startProcessInstanceByKey("loanApplicationProcess", applicationNumber, variables);

        return loanApplicationMapper.toResponseWithInherit(savedApplication);
    }
    
    @Transactional(readOnly = true)
    public Page<LoanApplicationListResponse> searchLoanApplications(
            String customerName, 
            String applicationNumber, 
            LoanStatus status, 
            ProductType productType, 
            Pageable pageable) {
        log.info("Searching loan applications with criteria - customerName: {}, applicationNumber: {}, status: {}, productType: {}", 
                customerName, applicationNumber, status, productType);
        
        // Convert null/empty to default values to avoid Hibernate enum issues
        String customerNameParam = StringUtils.isBlank(customerName) ? "" : customerName.trim();
        String applicationNumberParam = StringUtils.isBlank(applicationNumber) ? "" : applicationNumber.trim();
        String statusParam = status != null ? status.name() : null;
        String productTypeParam = productType != null ? productType.name() : null;
        
        Page<LoanApplication> applications = loanApplicationRepository.searchLoanApplications(
                customerNameParam, applicationNumberParam, statusParam, productTypeParam, pageable);
        log.info("Found {} loan applications matching search criteria", applications.getTotalElements());
        return applications.map(loanApplicationMapper::toListResponse);
    }
    
    @Transactional(readOnly = true)
    public LoanApplicationResponse getLoanApplicationById(UUID id) {
        log.info("Fetching loan application with ID: {}", id);
        LoanApplication application = loanApplicationRepository.findById(id)
                .orElseThrow(() -> new LoanApplicationNotFoundException("Loan application not found with ID: " + id));
        return loanApplicationMapper.toResponseWithInherit(application);
    }
    
    @Transactional(readOnly = true)
    public LoanApplicationResponse getLoanApplicationByApplicationNumber(String applicationNumber) {
        log.info("Fetching loan application with application number: {}", applicationNumber);
        LoanApplication application = loanApplicationRepository.findByApplicationNumber(applicationNumber)
                .orElseThrow(() -> new LoanApplicationNotFoundException("Loan application not found with application number: " + applicationNumber));
        return loanApplicationMapper.toResponseWithInherit(application);
    }


    //will use it in future feature
    private LoanDecision makeDecision(ScoringResult scoringResult, LoanApplicationRequest request) {
        String recommendation = scoringResult.getRecommendation();
        BigDecimal requestedAmount = request.getRequestedAmount();
        Integer requestedTenure = request.getTenureMonths();
        BigDecimal requestedRate = request.getInterestRate();
        
        return switch (recommendation) {
            case "APPROVE_HIGH_LIMIT" -> {
                BigDecimal approvedAmount = requestedAmount.multiply(BigDecimal.valueOf(1.2)); // 20% more
                yield LoanDecision.approve(approvedAmount, requestedTenure, requestedRate, 
                        "High score - approved with increased limit");
            }
            case "APPROVE_STANDARD_LIMIT" -> {
                yield LoanDecision.approve(requestedAmount, requestedTenure, requestedRate, 
                        "Good score - approved with standard terms");
            }
            case "APPROVE_REDUCED_LIMIT" -> {
                BigDecimal reducedAmount = requestedAmount.multiply(BigDecimal.valueOf(0.8)); // 20% less
                yield LoanDecision.approve(reducedAmount, requestedTenure, requestedRate, 
                        "Acceptable score - approved with reduced limit");
            }
            case "REVIEW_MANUAL" -> {
                yield LoanDecision.manualReview("Score requires manual review for decision");
            }
            default -> {
                yield LoanDecision.reject("Score below minimum threshold for approval");
            }
        };
    }    
    
    // Methods for BPMN Process Integration
    public void approveApplication(String applicationNumber, Long approvedAmount, String approverUser) {
        log.info("Approving loan application: {} with amount: {} by user: {}", 
                applicationNumber, approvedAmount, approverUser);
        LoanApplication application = loanApplicationRepository.findByApplicationNumber(applicationNumber)
                .orElseThrow(() -> new RuntimeException("Application not found: " + applicationNumber));
        // Update application status
        application.setStatus(LoanStatus.APPROVED);
        application.setApprovedAmount(BigDecimal.valueOf(approvedAmount));
        application.setApprovedBy(approverUser);
        application.setApprovedAt(LocalDateTime.now());
        application.setCollateralApproved(true);
        application.setCollateralApprovalDate(LocalDateTime.now());
        application.setApprovedAt(LocalDateTime.now());
        loanApplicationRepository.save(application);
    }
    
    public void rejectApplication(String applicationNumber, String reason, String rejectedBy) {
        log.info("Rejecting loan application: {} with reason: {}", applicationNumber, reason);
        LoanApplication application = loanApplicationRepository.findByApplicationNumber(applicationNumber)
                .orElseThrow(() -> new RuntimeException("Application not found: " + applicationNumber));
        application.setStatus(LoanStatus.REJECTED);
        application.setRejectionReason(reason);
        application.setRejectedAt(LocalDateTime.now());
        application.setRejectedBy(rejectedBy);
        loanApplicationRepository.save(application);
    }
    
    public void updateApplicationVerification(String applicationNumber, 
        Boolean applicationVerified, Boolean addressVerified,
        Boolean contactVerified, Boolean collateralVerified,
        String checkerMessage, String checkerName) {
        log.info("Updating application verification for: {} - Address: {}", 
                applicationNumber, addressVerified);
        LoanApplication application = loanApplicationRepository.findByApplicationNumber(applicationNumber)
                .orElseThrow(() -> new RuntimeException("Application not found: " + applicationNumber));
        // Update verification status
        application.setAddressVerified(addressVerified);
        application.setAddressVerificationDate(LocalDateTime.now());
        application.setApplicationVerified(applicationVerified);
        application.setApplicationVerificationDate(LocalDateTime.now());
        application.setContactVerified(contactVerified);
        application.setContactVerificationDate(LocalDateTime.now());
        application.setCollateralVerified(collateralVerified);
        application.setCollateralVerificationDate(LocalDateTime.now());
        // Store checker comments
        if (StringUtils.isNotBlank(checkerMessage)) {
            application.setCheckerMessage(checkerMessage);
        }
        application.setVerifiedBy(checkerName);
        application.setVerifiedAt(LocalDateTime.now());
        application.setStatus(LoanStatus.VERIFIED);
        loanApplicationRepository.save(application);
    }
}
