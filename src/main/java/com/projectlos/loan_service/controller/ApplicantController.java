package com.projectlos.loan_service.controller;

import com.projectlos.loan_service.dto.LoanApplicationListResponse;
import com.projectlos.loan_service.dto.LoanApplicationRequest;
import com.projectlos.loan_service.dto.LoanApplicationResponse;
import com.projectlos.loan_service.enums.LoanStatus;
import com.projectlos.loan_service.enums.ProductType;
import com.projectlos.loan_service.service.LoanApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/loan/applicant")
@RequiredArgsConstructor
@Slf4j
public class ApplicantController {

    private final LoanApplicationService loanApplicationService;

    @PostMapping
    @Operation(summary = "Create new loan application", description = "Submit a new loan application")
    public ResponseEntity<LoanApplicationResponse> createLoanApplication(@Valid @RequestBody LoanApplicationRequest request) {
        log.info("Creating new loan application for customer: {}", request.getCustomerId());
        LoanApplicationResponse response = loanApplicationService.createLoanApplication(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Search loan applications", description = "Search loan applications with multiple criteria and pagination")
    public ResponseEntity<Page<LoanApplicationListResponse>> searchLoanApplications(
            @Parameter(description = "Customer name (partial match)") @RequestParam(value = "customerName", required = false) String customerName,
            @Parameter(description = "Application number (partial match)") @RequestParam(value = "applicationNumber", required = false) String applicationNumber,
            @Parameter(description = "Application status") @RequestParam(value = "status", required = false) LoanStatus status,
            @Parameter(description = "Product type") @RequestParam(value = "productType", required = false) ProductType productType,
            Pageable pageable,
            HttpServletRequest request,
            Authentication authentication) {

        log.info("=== LOAN APPLICATION SEARCH REQUEST ===");
        log.info("Request URI: {}", request.getRequestURI());
        log.info("Request URL: {}", request.getRequestURL());
        log.info("Authentication: {}", authentication != null ? authentication.getName() : "NULL");
        log.info("Authorities: {}", authentication != null ? authentication.getAuthorities() : "NULL");
        log.info("Search criteria - customerName: {}, applicationNumber: {}, status: {}, productType: {}",
                customerName, applicationNumber, status, productType);

        Page<LoanApplicationListResponse> response = loanApplicationService.searchLoanApplications(
                customerName, applicationNumber, status, productType, pageable);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get loan application by ID", description = "Retrieve loan application by ID")
    public ResponseEntity<LoanApplicationResponse> getLoanApplicationById(
            @Parameter(description = "Loan application ID") @PathVariable(value = "id") UUID id) {
        log.info("Fetching loan application with ID: {}", id);
        LoanApplicationResponse response = loanApplicationService.getLoanApplicationById(id);
        return ResponseEntity.ok(response);
    }

}
