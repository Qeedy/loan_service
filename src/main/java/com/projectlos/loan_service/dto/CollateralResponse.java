package com.projectlos.loan_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.projectlos.loan_service.enums.CollateralType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Collateral response")
public class CollateralResponse {
    
    @Schema(description = "Collateral ID", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;
    
    @Schema(description = "Application ID", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID applicationId;
    
    @Schema(description = "Collateral type", example = "PROPERTY")
    private CollateralType collateralType;
    
    @Schema(description = "Collateral description", example = "Residential property in Jakarta")
    private String description;
    
    @Schema(description = "Estimated value", example = "500000000")
    private BigDecimal estimatedValue;
    
    @Schema(description = "Verification status", example = "false")
    private Boolean verified;
    
    @Schema(description = "Verified by user ID", example = "maker")
    private String verifiedBy;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Schema(description = "Verification date", example = "2024-01-01T00:00:00Z")
    private LocalDateTime verifiedAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Schema(description = "Created date", example = "2024-01-01T00:00:00Z")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Schema(description = "Updated date", example = "2024-01-01T00:00:00Z")
    private LocalDateTime updatedAt;
}
