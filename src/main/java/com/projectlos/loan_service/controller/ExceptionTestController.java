package com.projectlos.loan_service.controller;

import com.projectlos.loan_service.exception.InvalidLoanApplicationException;
import com.projectlos.loan_service.exception.LoanApplicationNotFoundException;
import com.projectlos.loan_service.exception.LoanApplicationConflictException;
import com.projectlos.loan_service.exception.UnauthorizedException;
import com.projectlos.loan_service.exception.CollateralNotFoundException;
import com.projectlos.loan_service.exception.IdentityDocumentNotFoundException;
import com.projectlos.loan_service.exception.TaskNotFoundException;
import com.projectlos.loan_service.exception.BureauServiceException;
import com.projectlos.loan_service.exception.DocumentSequenceException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/loan/exception-test")
@Slf4j
@Tag(name = "Exception Test", description = "Test endpoints for exception handling")
public class ExceptionTestController {

    @GetMapping("/not-found")
    @Operation(summary = "Test 404 Not Found Exception")
    public ResponseEntity<String> testNotFoundException() {
        throw new LoanApplicationNotFoundException("Test loan application not found");
    }

    @GetMapping("/bad-request")
    @Operation(summary = "Test 400 Bad Request Exception")
    public ResponseEntity<String> testBadRequestException() {
        throw new InvalidLoanApplicationException("Test invalid loan application data");
    }

    @GetMapping("/conflict")
    @Operation(summary = "Test 409 Conflict Exception")
    public ResponseEntity<String> testConflictException() {
        throw new LoanApplicationConflictException("Test loan application conflict");
    }

    @GetMapping("/unauthorized")
    @Operation(summary = "Test 401 Unauthorized Exception")
    public ResponseEntity<String> testUnauthorizedException() {
        throw new UnauthorizedException("Test unauthorized access");
    }

    @GetMapping("/runtime")
    @Operation(summary = "Test 500 Runtime Exception")
    public ResponseEntity<String> testRuntimeException() {
        throw new RuntimeException("Test runtime exception");
    }

    @GetMapping("/generic")
    @Operation(summary = "Test 500 Generic Exception")
    public ResponseEntity<String> testGenericException() throws Exception {
        throw new Exception("Test generic exception");
    }

    @GetMapping("/collateral-not-found")
    @Operation(summary = "Test 404 Collateral Not Found Exception")
    public ResponseEntity<String> testCollateralNotFoundException() {
        throw new CollateralNotFoundException("Test collateral not found");
    }

    @GetMapping("/identity-document-not-found")
    @Operation(summary = "Test 404 Identity Document Not Found Exception")
    public ResponseEntity<String> testIdentityDocumentNotFoundException() {
        throw new IdentityDocumentNotFoundException("Test identity document not found");
    }

    @GetMapping("/task-not-found")
    @Operation(summary = "Test 404 Task Not Found Exception")
    public ResponseEntity<String> testTaskNotFoundException() {
        throw new TaskNotFoundException("Test task not found");
    }

    @GetMapping("/bureau-service-error")
    @Operation(summary = "Test 503 Bureau Service Exception")
    public ResponseEntity<String> testBureauServiceException() {
        throw new BureauServiceException("Test bureau service unavailable");
    }

    @GetMapping("/document-sequence-error")
    @Operation(summary = "Test 500 Document Sequence Exception")
    public ResponseEntity<String> testDocumentSequenceException() {
        throw new DocumentSequenceException("Test document sequence generation failed");
    }
}
