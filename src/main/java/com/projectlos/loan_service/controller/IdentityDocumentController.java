package com.projectlos.loan_service.controller;

import com.projectlos.loan_service.dto.IdentityDocumentRequest;
import com.projectlos.loan_service.dto.IdentityDocumentResponse;
import com.projectlos.loan_service.enums.DocumentType;
import com.projectlos.loan_service.service.IdentityDocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/loan/identity-documents")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Identity Document Management", description = "APIs for identity document management")
public class IdentityDocumentController {
    
    private final IdentityDocumentService identityDocumentService;
    
    @PostMapping
    @Operation(summary = "Create identity document", description = "Create a new identity document")
    public ResponseEntity<IdentityDocumentResponse> createIdentityDocument(@Valid @RequestBody IdentityDocumentRequest request) {
        log.info("Creating identity document for application: {}", request.getApplicationId());
        IdentityDocumentResponse response = identityDocumentService.createIdentityDocument(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get identity document by ID", description = "Retrieve identity document by ID")
    public ResponseEntity<IdentityDocumentResponse> getIdentityDocumentById(
            @Parameter(description = "Identity document ID") @PathVariable(value = "id") UUID id) {
        log.info("Getting identity document with ID: {}", id);
        IdentityDocumentResponse response = identityDocumentService.getIdentityDocumentById(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    @Operation(summary = "Get all identity documents", description = "Retrieve all identity documents")
    public ResponseEntity<List<IdentityDocumentResponse>> getAllIdentityDocuments() {
        log.info("Getting all identity documents");
        List<IdentityDocumentResponse> response = identityDocumentService.getAllIdentityDocuments();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/application/{applicationId}")
    @Operation(summary = "Get identity documents by application ID", description = "Retrieve identity documents for specific application")
    public ResponseEntity<List<IdentityDocumentResponse>> getIdentityDocumentsByApplicationId(
            @Parameter(description = "Application ID") @PathVariable(value = "applicationId") UUID applicationId) {
        log.info("Getting identity documents for application: {}", applicationId);
        List<IdentityDocumentResponse> response = identityDocumentService.getIdentityDocumentsByApplicationId(applicationId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/application/{applicationId}/type/{documentType}")
    @Operation(summary = "Get identity documents by application ID and type", description = "Retrieve identity documents for specific application and type")
    public ResponseEntity<List<IdentityDocumentResponse>> getIdentityDocumentsByType(
            @Parameter(description = "Application ID") @PathVariable(value = "applicationId") UUID applicationId,
            @Parameter(description = "Document type") @PathVariable(value = "documentType") DocumentType documentType) {
        log.info("Getting identity documents for application: {} with type: {}", applicationId, documentType);
        List<IdentityDocumentResponse> response = identityDocumentService.getIdentityDocumentsByType(applicationId, documentType);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update identity document", description = "Update existing identity document")
    public ResponseEntity<IdentityDocumentResponse> updateIdentityDocument(
            @Parameter(description = "Identity document ID") @PathVariable(value = "id") UUID id,
            @Valid @RequestBody IdentityDocumentRequest request) {
        log.info("Updating identity document with ID: {}", id);
        IdentityDocumentResponse response = identityDocumentService.updateIdentityDocument(id, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete identity document", description = "Delete identity document by ID")
    public ResponseEntity<Void> deleteIdentityDocument(
            @Parameter(description = "Identity document ID") @PathVariable(value = "id") UUID id) {
        log.info("Deleting identity document with ID: {}", id);
        identityDocumentService.deleteIdentityDocument(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{id}/verify")
    @Operation(summary = "Verify identity document", description = "Verify identity document")
    public ResponseEntity<IdentityDocumentResponse> verifyIdentityDocument(
            @Parameter(description = "Identity document ID") @PathVariable(value = "id") UUID id,
            Authentication authentication) {
        
        String verifiedBy = extractUserIdFromAuth(authentication);
        log.info("Verifying identity document with ID: {} by user: {}", id, verifiedBy);
        IdentityDocumentResponse response = identityDocumentService.verifyIdentityDocument(id, verifiedBy);
        return ResponseEntity.ok(response);
    }
    
    private String extractUserIdFromAuth(Authentication authentication) {
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            // Prioritize human-readable username for better logging and audit
            String username = jwt.getClaimAsString("preferred_username");
            if (username != null) {
                return username;
            }
            
            // Fallback to subject ID (UUID)
            String userId = jwt.getClaimAsString("sub");
            if (userId != null) {
                return userId;
            }
            
            // Last fallback to name claim
            return jwt.getClaimAsString("name");
        }
        return authentication.getName();
    }
    
    @GetMapping("/verified")
    @Operation(summary = "Get verified identity documents", description = "Retrieve all verified identity documents")
    public ResponseEntity<List<IdentityDocumentResponse>> getVerifiedIdentityDocuments() {
        log.info("Getting verified identity documents");
        List<IdentityDocumentResponse> response = identityDocumentService.getVerifiedIdentityDocuments();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/unverified")
    @Operation(summary = "Get unverified identity documents", description = "Retrieve all unverified identity documents")
    public ResponseEntity<List<IdentityDocumentResponse>> getUnverifiedIdentityDocuments() {
        log.info("Getting unverified identity documents");
        List<IdentityDocumentResponse> response = identityDocumentService.getUnverifiedIdentityDocuments();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/application/{applicationId}/count")
    @Operation(summary = "Count identity documents by application ID", description = "Count identity documents for specific application")
    public ResponseEntity<Long> countIdentityDocumentsByApplicationId(
            @Parameter(description = "Application ID") @PathVariable(value = "applicationId") UUID applicationId) {
        log.info("Counting identity documents for application: {}", applicationId);
        Long count = identityDocumentService.countIdentityDocumentsByApplicationId(applicationId);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/application/{applicationId}/verified-count")
    @Operation(summary = "Count verified identity documents by application ID", description = "Count verified identity documents for specific application")
    public ResponseEntity<Long> countVerifiedIdentityDocumentsByApplicationId(
            @Parameter(description = "Application ID") @PathVariable(value = "applicationId") UUID applicationId) {
        log.info("Counting verified identity documents for application: {}", applicationId);
        Long count = identityDocumentService.countVerifiedIdentityDocumentsByApplicationId(applicationId);
        return ResponseEntity.ok(count);
    }
}
