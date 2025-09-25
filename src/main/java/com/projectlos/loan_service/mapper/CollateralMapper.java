package com.projectlos.loan_service.mapper;

import com.projectlos.loan_service.dto.CollateralRequest;
import com.projectlos.loan_service.dto.CollateralResponse;
import com.projectlos.loan_service.entity.Collateral;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CollateralMapper {
    
    public Collateral toEntity(CollateralRequest request) {
        return Collateral.builder()
                .applicationId(request.getApplicationId())
                .collateralType(request.getCollateralType())
                .description(request.getDescription())
                .estimatedValue(request.getEstimatedValue())
                .verified(false)
                .build();
    }
    
    public CollateralResponse toResponse(Collateral collateral) {
        return CollateralResponse.builder()
                .id(collateral.getId())
                .applicationId(collateral.getApplicationId())
                .collateralType(collateral.getCollateralType())
                .description(collateral.getDescription())
                .estimatedValue(collateral.getEstimatedValue())
                .verified(collateral.getVerified())
                .verifiedBy(collateral.getVerifiedBy())
                .verifiedAt(collateral.getVerifiedAt())
                .createdAt(collateral.getCreatedAt())
                .updatedAt(collateral.getUpdatedAt())
                .build();
    }
    
    public void updateEntity(Collateral entity, CollateralRequest request) {
        entity.setCollateralType(request.getCollateralType());
        entity.setDescription(request.getDescription());
        entity.setEstimatedValue(request.getEstimatedValue());
    }
}
