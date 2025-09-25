package com.projectlos.loan_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Simplified task detail response")
public class TaskDetailResponse {
    
    @Schema(description = "Task ID", example = "task_123")
    private String taskId;
    
    @Schema(description = "Task name", example = "Maker Submit Application")
    private String taskName;
    
    @Schema(description = "Application number", example = "APP-2024-001")
    private String applicationNumber;
    
    @Schema(description = "Customer name", example = "John Doe")
    private String customerName;
    
    @Schema(description = "Requested amount", example = "50000000")
    private BigDecimal requestedAmount;
    
    @Schema(description = "Current status", example = "PENDING_APPROVAL")
    private String status;
    
    @Schema(description = "Task assignee", example = "checker01")
    private String assignee;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Task creation time", example = "2024-01-01T00:00:00")
    private LocalDateTime createTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Application submission time", example = "2024-01-01T00:00:00")
    private LocalDateTime applicationDate;
}
