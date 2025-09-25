package com.projectlos.loan_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.projectlos.loan_service.enums.DocumentType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Identity document response")
public class IdentityDocumentResponse {
    
    @Schema(description = "Document ID", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;
    
    @Schema(description = "Application ID", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID applicationId;
    
    @Schema(description = "Document type", example = "KTP")
    private DocumentType documentType;
    
    @Schema(description = "Document number", example = "1234567890123456")
    private String documentNumber;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Expiry date", example = "2029-12-31")
    private LocalDate expiryDate;
    
    @Schema(description = "Verification status", example = "false")
    private Boolean verified;
    
    @Schema(description = "Verified by user ID", example = "maker")
    private String verifiedBy;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Schema(description = "Verification date", example = "2024-01-01T00:00:00Z")
    private LocalDateTime verifiedAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Schema(description = "Created date", example = "2024-01-01T00:00:00Z")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Schema(description = "Updated date", example = "2024-01-01T00:00:00Z")
    private LocalDateTime updatedAt;
}
