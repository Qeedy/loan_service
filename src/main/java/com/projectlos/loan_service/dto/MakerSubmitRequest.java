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
@Schema(description = "Maker submit application request")
public class MakerSubmitRequest {
    
    @NotBlank(message = "Application ID is required")
    @Schema(description = "Application ID", example = "APP-2024-001")
    private String applicationId;
    
    @NotNull(message = "Application complete status is required")
    @Schema(description = "Whether application is complete", example = "true")
    private Boolean applicationComplete;
    
    @NotNull(message = "Documents attached status is required")
    @Schema(description = "Whether documents are attached", example = "true")
    private Boolean documentsAttached;
    
    @Schema(description = "Maker comments", example = "Application ready for review")
    private String makerComments;
}
