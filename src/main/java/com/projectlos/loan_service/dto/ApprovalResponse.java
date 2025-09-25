package com.projectlos.loan_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Approval process response")
public class ApprovalResponse {
    
    @Schema(description = "Application ID", example = "APP-2024-001")
    private String applicationId;
    
    @Schema(description = "Task ID", example = "task-123")
    private String taskId;
    
    @Schema(description = "Process status", example = "COMPLETED")
    private String status;
    
    @Schema(description = "Process message", example = "Task completed successfully")
    private String message;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Schema(description = "Processed at", example = "2024-01-01T00:00:00Z")
    private LocalDateTime processedAt;
    
    @Schema(description = "Next step", example = "Bureau Checking")
    private String nextStep;
}
