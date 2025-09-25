package com.projectlos.loan_service.service;

import com.projectlos.loan_service.dto.CollateralRequest;
import com.projectlos.loan_service.dto.CollateralResponse;
import com.projectlos.loan_service.entity.Collateral;
import com.projectlos.loan_service.exception.CollateralNotFoundException;
import com.projectlos.loan_service.exception.InvalidLoanApplicationException;
import com.projectlos.loan_service.mapper.CollateralMapper;
import com.projectlos.loan_service.repository.CollateralRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CollateralService {
    
    private final CollateralRepository collateralRepository;
    private final CollateralMapper collateralMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    
    public CollateralResponse createCollateral(CollateralRequest request) {
        log.info("Creating collateral for application: {}", request.getApplicationId());
        
        Collateral collateral = collateralMapper.toEntity(request);
        Collateral savedCollateral = collateralRepository.save(collateral);
        
        log.info("Collateral created with ID: {}", savedCollateral.getId());
        
        return collateralMapper.toResponse(savedCollateral);
    }
    
    public CollateralResponse getCollateralById(UUID id) {
        log.info("Getting collateral by ID: {}", id);
        
        Collateral collateral = collateralRepository.findById(id)
                .orElseThrow(() -> new CollateralNotFoundException("Collateral not found with ID: " + id));
        
        return collateralMapper.toResponse(collateral);
    }
    
    public List<CollateralResponse> getCollateralsByApplicationId(UUID applicationId) {
        log.info("Getting collaterals for application: {}", applicationId);
        
        List<Collateral> collaterals = collateralRepository.findByApplicationId(applicationId);
        
        return collaterals.stream()
                .map(collateralMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    public List<CollateralResponse> getAllCollaterals() {
        log.info("Getting all collaterals");
        
        List<Collateral> collaterals = collateralRepository.findAll();
        
        return collaterals.stream()
                .map(collateralMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    public CollateralResponse updateCollateral(UUID id, CollateralRequest request) {
        log.info("Updating collateral with ID: {}", id);
        
        Collateral collateral = collateralRepository.findById(id)
                .orElseThrow(() -> new CollateralNotFoundException("Collateral not found with ID: " + id));
        
        collateralMapper.updateEntity(collateral, request);
        Collateral savedCollateral = collateralRepository.save(collateral);
        
        log.info("Collateral updated successfully");
        
        return collateralMapper.toResponse(savedCollateral);
    }
    
    public void deleteCollateral(UUID id) {
        log.info("Deleting collateral with ID: {}", id);
        
        if (!collateralRepository.existsById(id)) {
            throw new RuntimeException("Collateral not found with ID: " + id);
        }
        
        collateralRepository.deleteById(id);
        log.info("Collateral deleted successfully");
    }
    
    public CollateralResponse verifyCollateral(UUID id, String verifiedBy) {
        log.info("Verifying collateral with ID: {} by user: {}", id, verifiedBy);
        
        Collateral collateral = collateralRepository.findById(id)
                .orElseThrow(() -> new CollateralNotFoundException("Collateral not found with ID: " + id));
        
        collateral.setVerified(true);
        collateral.setVerifiedBy(verifiedBy);
        collateral.setVerifiedAt(LocalDateTime.now());
        
        Collateral savedCollateral = collateralRepository.save(collateral);
//
//        // Publish Kafka event
//        publishCollateralVerifiedEvent(savedCollateral);
        
        log.info("Collateral verified successfully");
        
        return collateralMapper.toResponse(savedCollateral);
    }
    
    public List<CollateralResponse> getVerifiedCollaterals() {
        log.info("Getting verified collaterals");
        
        List<Collateral> collaterals = collateralRepository.findByVerified(true);
        
        return collaterals.stream()
                .map(collateralMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    public List<CollateralResponse> getUnverifiedCollaterals() {
        log.info("Getting unverified collaterals");
        
        List<Collateral> collaterals = collateralRepository.findByVerified(false);
        
        return collaterals.stream()
                .map(collateralMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    public Long countCollateralsByApplicationId(UUID applicationId) {
        return collateralRepository.countByApplicationId(applicationId);
    }
    
    public Long countVerifiedCollateralsByApplicationId(UUID applicationId) {
        return collateralRepository.countVerifiedByApplicationId(applicationId);
    }
    
    private void publishCollateralVerifiedEvent(Collateral collateral) {
        try {
            String eventData = String.format(
                "{\"collateralId\":\"%s\",\"applicationId\":\"%s\",\"collateralType\":\"%s\",\"verifiedAt\":\"%s\",\"verifiedBy\":\"%s\"}",
                collateral.getId(),
                collateral.getApplicationId(),
                collateral.getCollateralType(),
                collateral.getVerifiedAt(),
                collateral.getVerifiedBy()
            );
            
            kafkaTemplate.send("collateral-verified", collateral.getId().toString(), eventData);
            log.info("Published collateral verified event for collateral: {}", collateral.getId());
        } catch (Exception e) {
            log.error("Failed to publish collateral verified event for collateral: {}", collateral.getId(), e);
        }
    }
}
