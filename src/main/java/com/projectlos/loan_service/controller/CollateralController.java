package com.projectlos.loan_service.controller;

import com.projectlos.loan_service.dto.CollateralRequest;
import com.projectlos.loan_service.dto.CollateralResponse;
import com.projectlos.loan_service.service.CollateralService;
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
@RequestMapping("/api/loan/collaterals")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Collateral Management", description = "APIs for collateral management")
public class CollateralController {
    
    private final CollateralService collateralService;
    
    @PostMapping
    @Operation(summary = "Create collateral", description = "Create a new collateral")
    public ResponseEntity<CollateralResponse> createCollateral(@Valid @RequestBody CollateralRequest request) {
        log.info("Creating collateral for application: {}", request.getApplicationId());
        CollateralResponse response = collateralService.createCollateral(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get collateral by ID", description = "Retrieve collateral by ID")
    public ResponseEntity<CollateralResponse> getCollateralById(
            @Parameter(description = "Collateral ID") @PathVariable(value = "id") UUID id) {
        log.info("Getting collateral with ID: {}", id);
        CollateralResponse response = collateralService.getCollateralById(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    @Operation(summary = "Get all collaterals", description = "Retrieve all collaterals")
    public ResponseEntity<List<CollateralResponse>> getAllCollaterals() {
        log.info("Getting all collaterals");
        List<CollateralResponse> response = collateralService.getAllCollaterals();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/application/{applicationId}")
    @Operation(summary = "Get collaterals by application ID", description = "Retrieve collaterals for specific application")
    public ResponseEntity<List<CollateralResponse>> getCollateralsByApplicationId(
            @Parameter(description = "Application ID") @PathVariable(value = "applicationId") UUID applicationId) {
        log.info("Getting collaterals for application: {}", applicationId);
        List<CollateralResponse> response = collateralService.getCollateralsByApplicationId(applicationId);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update collateral", description = "Update existing collateral")
    public ResponseEntity<CollateralResponse> updateCollateral(
            @Parameter(description = "Collateral ID") @PathVariable(value = "id") UUID id,
            @Valid @RequestBody CollateralRequest request) {
        log.info("Updating collateral with ID: {}", id);
        CollateralResponse response = collateralService.updateCollateral(id, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete collateral", description = "Delete collateral by ID")
    public ResponseEntity<Void> deleteCollateral(
            @Parameter(description = "Collateral ID") @PathVariable(value = "id") UUID id) {
        log.info("Deleting collateral with ID: {}", id);
        collateralService.deleteCollateral(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{id}/verify")
    @Operation(summary = "Verify collateral", description = "Verify collateral")
    public ResponseEntity<CollateralResponse> verifyCollateral(
            @Parameter(description = "Collateral ID") @PathVariable(value = "id") UUID id,
            Authentication authentication) {
        
        String verifiedBy = extractUserIdFromAuth(authentication);
        log.info("Verifying collateral with ID: {} by user: {}", id, verifiedBy);
        CollateralResponse response = collateralService.verifyCollateral(id, verifiedBy);
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
    @Operation(summary = "Get verified collaterals", description = "Retrieve all verified collaterals")
    public ResponseEntity<List<CollateralResponse>> getVerifiedCollaterals() {
        log.info("Getting verified collaterals");
        List<CollateralResponse> response = collateralService.getVerifiedCollaterals();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/unverified")
    @Operation(summary = "Get unverified collaterals", description = "Retrieve all unverified collaterals")
    public ResponseEntity<List<CollateralResponse>> getUnverifiedCollaterals() {
        log.info("Getting unverified collaterals");
        List<CollateralResponse> response = collateralService.getUnverifiedCollaterals();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/application/{applicationId}/count")
    @Operation(summary = "Count collaterals by application ID", description = "Count collaterals for specific application")
    public ResponseEntity<Long> countCollateralsByApplicationId(
            @Parameter(description = "Application ID") @PathVariable(value = "applicationId") UUID applicationId) {
        log.info("Counting collaterals for application: {}", applicationId);
        Long count = collateralService.countCollateralsByApplicationId(applicationId);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/application/{applicationId}/verified-count")
    @Operation(summary = "Count verified collaterals by application ID", description = "Count verified collaterals for specific application")
    public ResponseEntity<Long> countVerifiedCollateralsByApplicationId(
            @Parameter(description = "Application ID") @PathVariable(value = "applicationId") UUID applicationId) {
        log.info("Counting verified collaterals for application: {}", applicationId);
        Long count = collateralService.countVerifiedCollateralsByApplicationId(applicationId);
        return ResponseEntity.ok(count);
    }
}
