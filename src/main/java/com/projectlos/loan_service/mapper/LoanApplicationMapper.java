package com.projectlos.loan_service.mapper;

import com.projectlos.loan_service.dto.*;
import com.projectlos.loan_service.entity.LoanApplication;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LoanApplicationMapper {
    
    
    public LoanApplication toEntity(LoanApplicationRequest request) {
        return LoanApplication.builder()
                .customerId(request.getCustomerId())
                .productType(request.getProductType())
                .requestedAmount(request.getRequestedAmount())
                .tenureMonths(request.getTenureMonths())
                .interestRate(request.getInterestRate())
                .purpose(request.getPurpose())
                .build();
    }
    
    public LoanApplication toEntityWithInherit(LoanApplicationRequest request) {
        return LoanApplication.builder()
                .customerName(request.getCustomerName())
                .customerId(request.getCustomerId())
                .productType(request.getProductType())
                .requestedAmount(request.getRequestedAmount())
                .tenureMonths(request.getTenureMonths())
                .interestRate(request.getInterestRate())
                .purpose(request.getPurpose())
                .build();
    }
    
    public LoanApplicationResponse toResponse(LoanApplication application) {
        return LoanApplicationResponse.builder()
                .id(application.getId())
                .applicationId(application.getApplicationNumber())
                .customerId(application.getCustomerId())
                .customerName(application.getCustomerName())
                .productType(application.getProductType())
                .requestedAmount(application.getRequestedAmount())
                .approvedAmount(application.getApprovedAmount())
                .tenureMonths(application.getTenureMonths())
                .interestRate(application.getInterestRate())
                .purpose(application.getPurpose())
                .status(application.getStatus().toString())
                .score(application.getScore())
                .scoreGrade(application.getScoreGrade())
                .scoringModel(application.getScoringModel())
                .scoringDetails(application.getScoringDetails())
                .approvedBy(application.getApprovedBy())
                .approvedAt(application.getApprovedAt())
                .addressVerified(application.getAddressVerified())
                .addressVerificationDate(application.getAddressVerificationDate())
                .contactVerified(application.getContactVerified())
                .contactVerificationDate(application.getContactVerificationDate())
                .contactVerificationMethod(application.getContactVerificationMethod())
                .createdAt(application.getCreatedAt())
                .updatedAt(application.getUpdatedAt())
                .build();
    }
    
    public LoanApplicationResponse toResponseWithInherit(LoanApplication application) {
        LoanApplicationResponse response = toResponse(application);
        
        // No related data to load for now
        
        return response;
    }
    
    public LoanApplicationListResponse toListResponse(LoanApplication application) {
        return LoanApplicationListResponse.builder()
                .id(application.getId())
                .applicationNumber(application.getApplicationNumber())
                .customerId(application.getCustomerId())
                .customerName(application.getCustomerName())
                .productType(application.getProductType())
                .requestedAmount(application.getRequestedAmount())
                .approvedAmount(application.getApprovedAmount())
                .status(application.getStatus().toString())
                .createdAt(application.getCreatedAt())
                .updatedAt(application.getUpdatedAt())
                .build();
    }
    
}
