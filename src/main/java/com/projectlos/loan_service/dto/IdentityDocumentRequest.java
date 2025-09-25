package com.projectlos.loan_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.projectlos.loan_service.enums.DocumentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Identity document request")
public class IdentityDocumentRequest {
    
    @NotNull(message = "Application ID is required")
    @Schema(description = "Application ID", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID applicationId;
    
    @NotNull(message = "Document type is required")
    @Schema(description = "Document type", example = "KTP")
    private DocumentType documentType;
    
    @NotBlank(message = "Document number is required")
    @Schema(description = "Document number", example = "1234567890123456")
    private String documentNumber;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Expiry date", example = "2029-12-31")
    private LocalDate expiryDate;
}
