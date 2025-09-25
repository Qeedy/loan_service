package com.projectlos.loan_service.dto;

import com.projectlos.loan_service.enums.ProductType;
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
@Schema(description = "Simple loan application response for list view")
public class LoanApplicationListResponse {
    
    @Schema(description = "Application ID", example = "1")
    private UUID id;
    
    @Schema(description = "Application Number", example = "LN-2024-001")
    private String applicationNumber;
    
    @Schema(description = "Customer ID", example = "CUST-123456")
    private String customerId;
    
    @Schema(description = "Customer Name", example = "John Doe")
    private String customerName;
    
    @Schema(description = "Product type", example = "KUR")
    private ProductType productType;
    
    @Schema(description = "Requested loan amount", example = "50000000")
    private BigDecimal requestedAmount;
    
    @Schema(description = "Approved loan amount", example = "45000000")
    private BigDecimal approvedAmount;
    
    @Schema(description = "Application status", example = "SUBMITTED")
    private String status;
    
    @Schema(description = "Application creation date", example = "2024-01-01T00:00:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "Last update date", example = "2024-01-01T00:00:00")
    private LocalDateTime updatedAt;
}
