package com.projectlos.loan_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Bureau check request")
public class BureauCheckRequest {
    
    @NotBlank(message = "Application ID is required")
    @Schema(description = "Application ID", example = "APP-2024-001")
    private String applicationId;
    
    @NotBlank(message = "Customer ID is required")
    @Schema(description = "Customer ID", example = "550e8400-e29b-41d4-a716-446655440000")
    private String customerId;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Schema(description = "Check date", example = "2024-01-01T00:00:00Z")
    private LocalDateTime checkDate;
}