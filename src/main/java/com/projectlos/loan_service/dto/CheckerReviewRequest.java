package com.projectlos.loan_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Checker review request")
public class CheckerReviewRequest {
    
    @NotBlank(message = "Application ID is required")
    @Schema(description = "Application ID", example = "APP-2024-001")
    private String applicationId;
    
    @NotBlank(message = "Task ID is required")
    @Schema(description = "Task ID", example = "task-123")
    private String taskId;
    
    @NotNull(message = "Address verified status is required")
    @Schema(description = "Whether address is verified", example = "true")
    private Boolean addressVerified;
    
    @Schema(description = "Verification method", example = "Phone Call")
    private String verificationMethod;
    
    @Schema(description = "Checker comments", example = "Address verified via phone call")
    private String checkerComments;
}
