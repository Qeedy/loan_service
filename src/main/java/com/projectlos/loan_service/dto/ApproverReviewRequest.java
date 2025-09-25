package com.projectlos.loan_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Approver review request")
public class ApproverReviewRequest {
    
    @NotBlank(message = "Application ID is required")
    @Schema(description = "Application ID", example = "APP-2024-001")
    private String applicationId;
    
    @NotBlank(message = "Task ID is required")
    @Schema(description = "Task ID", example = "task-123")
    private String taskId;
    
    @NotBlank(message = "Final decision is required")
    @Schema(description = "Final decision", example = "APPROVED", allowableValues = {"APPROVED", "REJECTED"})
    private String finalDecision;
    
    @Schema(description = "Approved amount", example = "50000000")
    private BigDecimal approvedAmount;
    
    @Schema(description = "Approver comments", example = "Application approved with conditions")
    private String approverComments;
}
