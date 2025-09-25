package com.projectlos.loan_service.dto;

import com.projectlos.loan_service.enums.ProductType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Loan application response")
public class LoanApplicationResponse {
    
    @Schema(description = "Application ID", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;
    
    @Schema(description = "Application unique identifier", example = "APP-2024-001")
    private String applicationId;
    
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
    
    @Schema(description = "Loan tenure in months", example = "12")
    private Integer tenureMonths;
    
    @Schema(description = "Interest rate per annum", example = "12.5")
    private BigDecimal interestRate;
    
    @Schema(description = "Purpose of the loan", example = "Working Capital")
    private String purpose;
    
    @Schema(description = "Application status", example = "SUBMITTED")
    private String status;
    
    // Scoring fields
    @Schema(description = "Application score", example = "750")
    private Integer score;
    
    @Schema(description = "Score grade", example = "A")
    private String scoreGrade;
    
    @Schema(description = "Scoring model used", example = "KUR_MODEL_V1")
    private String scoringModel;
    
    @Schema(description = "Scoring details", example = "Detailed scoring breakdown")
    private String scoringDetails;
    
    @Schema(description = "Decision reason", example = "Approved based on good credit history")
    private String decisionReason;
    
    @Schema(description = "Approved by", example = "LOAN_OFFICER_001")
    private String approvedBy;
    
    @Schema(description = "Approval date", example = "2024-01-01T00:00:00")
    private LocalDateTime approvedAt;
    
    // Verification fields
    @Schema(description = "Address verified", example = "true")
    private Boolean addressVerified;
    
    @Schema(description = "Address verification date", example = "2024-01-01T00:00:00")
    private LocalDateTime addressVerificationDate;
    
    @Schema(description = "Address verification method", example = "MANUAL")
    private String addressVerificationMethod;
    
    @Schema(description = "Contact verified", example = "true")
    private Boolean contactVerified;
    
    @Schema(description = "Contact verification date", example = "2024-01-01T00:00:00")
    private LocalDateTime contactVerificationDate;
    
    @Schema(description = "Contact verification method", example = "MANUAL")
    private String contactVerificationMethod;
    
    @Schema(description = "Process instance ID", example = "proc_inst_123")
    private String processInstanceId;
    
    @Schema(description = "Application creation date", example = "2024-01-01T00:00:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "Last update date", example = "2024-01-01T00:00:00")
    private LocalDateTime updatedAt;
    
    // Related data - removed unused fields
}
