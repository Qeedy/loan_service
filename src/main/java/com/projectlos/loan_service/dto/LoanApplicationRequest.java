package com.projectlos.loan_service.dto;

import com.projectlos.loan_service.enums.ProductType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Loan application request")
public class LoanApplicationRequest {
    
    @NotBlank(message = "Customer ID is required")
    @Schema(description = "Customer ID (UUID from customer service)", example = "550e8400-e29b-41d4-a716-446655440000")
    private String customerId;

    @NotBlank(message = "Customer Name is required")
    @Schema(description = "Customer Name (Name from customer service)", example = "John Doe")
    private String customerName;
    
    @NotNull(message = "Product type is required")
    @Schema(description = "Loan product type", example = "KUR")
    private ProductType productType;
    
    @NotNull(message = "Requested amount is required")
    @DecimalMin(value = "1000000", message = "Minimum loan amount is 1,000,000")
    @DecimalMax(value = "1000000000", message = "Maximum loan amount is 1,000,000,000")
    @Schema(description = "Requested loan amount", example = "50000000")
    private BigDecimal requestedAmount;
    
    @NotNull(message = "Tenure is required")
    @Min(value = 1, message = "Minimum tenure is 1 month")
    @Max(value = 60, message = "Maximum tenure is 60 months")
    @Schema(description = "Loan tenure in months", example = "12")
    private Integer tenureMonths;
    
    @NotNull(message = "Interest rate is required")
    @DecimalMin(value = "0.01", message = "Interest rate must be at least 0.01%")
    @DecimalMax(value = "30.0", message = "Interest rate cannot exceed 30%")
    @Schema(description = "Interest rate per annum", example = "12.5")
    private BigDecimal interestRate;
    
    @NotBlank(message = "Purpose is required")
    @Schema(description = "Purpose of the loan", example = "Working Capital")
    private String purpose;
    
    
}
