package com.projectlos.loan_service.mapper;

import com.projectlos.loan_service.dto.IdentityDocumentRequest;
import com.projectlos.loan_service.dto.IdentityDocumentResponse;
import com.projectlos.loan_service.entity.IdentityDocument;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class IdentityDocumentMapper {
    
    public IdentityDocument toEntity(IdentityDocumentRequest request) {
        return IdentityDocument.builder()
                .applicationId(request.getApplicationId())
                .documentType(request.getDocumentType())
                .documentNumber(request.getDocumentNumber())
                .expiryDate(request.getExpiryDate())
                .verified(false)
                .build();
    }
    
    public IdentityDocumentResponse toResponse(IdentityDocument identityDocument) {
        return IdentityDocumentResponse.builder()
                .id(identityDocument.getId())
                .applicationId(identityDocument.getApplicationId())
                .documentType(identityDocument.getDocumentType())
                .documentNumber(identityDocument.getDocumentNumber())
                .expiryDate(identityDocument.getExpiryDate())
                .verified(identityDocument.getVerified())
                .verifiedBy(identityDocument.getVerifiedBy())
                .verifiedAt(identityDocument.getVerifiedAt())
                .createdAt(identityDocument.getCreatedAt())
                .updatedAt(identityDocument.getUpdatedAt())
                .build();
    }
    
    public void updateEntity(IdentityDocument entity, IdentityDocumentRequest request) {
        entity.setDocumentType(request.getDocumentType());
        entity.setDocumentNumber(request.getDocumentNumber());
        entity.setExpiryDate(request.getExpiryDate());
    }
}
