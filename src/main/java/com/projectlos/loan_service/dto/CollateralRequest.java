package com.projectlos.loan_service.dto;

import com.projectlos.loan_service.enums.CollateralType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Collateral request")
public class CollateralRequest {
    
    @NotNull(message = "Application ID is required")
    @Schema(description = "Application ID", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID applicationId;
    
    @NotNull(message = "Collateral type is required")
    @Schema(description = "Collateral type", example = "PROPERTY")
    private CollateralType collateralType;
    
    @NotBlank(message = "Description is required")
    @Schema(description = "Collateral description", example = "Residential property in Jakarta")
    private String description;
    
    @NotNull(message = "Estimated value is required")
    @Positive(message = "Estimated value must be positive")
    @Schema(description = "Estimated value", example = "500000000")
    private BigDecimal estimatedValue;
}
