package com.projectlos.loan_service.service;

import com.projectlos.loan_service.dto.IdentityDocumentRequest;
import com.projectlos.loan_service.dto.IdentityDocumentResponse;
import com.projectlos.loan_service.entity.IdentityDocument;
import com.projectlos.loan_service.enums.DocumentType;
import com.projectlos.loan_service.exception.IdentityDocumentNotFoundException;
import com.projectlos.loan_service.exception.InvalidLoanApplicationException;
import com.projectlos.loan_service.mapper.IdentityDocumentMapper;
import com.projectlos.loan_service.repository.IdentityDocumentRepository;
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
public class IdentityDocumentService {
    
    private final IdentityDocumentRepository identityDocumentRepository;
    private final IdentityDocumentMapper identityDocumentMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    
    public IdentityDocumentResponse createIdentityDocument(IdentityDocumentRequest request) {
        log.info("Creating identity document for application: {}", request.getApplicationId());
        
        IdentityDocument identityDocument = identityDocumentMapper.toEntity(request);
        IdentityDocument savedDocument = identityDocumentRepository.save(identityDocument);
        
        log.info("Identity document created with ID: {}", savedDocument.getId());
        
        return identityDocumentMapper.toResponse(savedDocument);
    }
    
    public IdentityDocumentResponse getIdentityDocumentById(UUID id) {
        log.info("Getting identity document by ID: {}", id);
        
        IdentityDocument identityDocument = identityDocumentRepository.findById(id)
                .orElseThrow(() -> new IdentityDocumentNotFoundException("Identity document not found with ID: " + id));
        
        return identityDocumentMapper.toResponse(identityDocument);
    }
    
    public List<IdentityDocumentResponse> getIdentityDocumentsByApplicationId(UUID applicationId) {
        log.info("Getting identity documents for application: {}", applicationId);
        
        List<IdentityDocument> documents = identityDocumentRepository.findByApplicationId(applicationId);
        
        return documents.stream()
                .map(identityDocumentMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    public List<IdentityDocumentResponse> getAllIdentityDocuments() {
        log.info("Getting all identity documents");
        
        List<IdentityDocument> documents = identityDocumentRepository.findAll();
        
        return documents.stream()
                .map(identityDocumentMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    public IdentityDocumentResponse updateIdentityDocument(UUID id, IdentityDocumentRequest request) {
        log.info("Updating identity document with ID: {}", id);
        
        IdentityDocument identityDocument = identityDocumentRepository.findById(id)
                .orElseThrow(() -> new IdentityDocumentNotFoundException("Identity document not found with ID: " + id));
        
        identityDocumentMapper.updateEntity(identityDocument, request);
        IdentityDocument savedDocument = identityDocumentRepository.save(identityDocument);
        
        log.info("Identity document updated successfully");
        
        return identityDocumentMapper.toResponse(savedDocument);
    }
    
    public void deleteIdentityDocument(UUID id) {
        log.info("Deleting identity document with ID: {}", id);
        
        if (!identityDocumentRepository.existsById(id)) {
            throw new RuntimeException("Identity document not found with ID: " + id);
        }
        
        identityDocumentRepository.deleteById(id);
        log.info("Identity document deleted successfully");
    }
    
    public IdentityDocumentResponse verifyIdentityDocument(UUID id, String verifiedBy) {
        log.info("Verifying identity document with ID: {} by user: {}", id, verifiedBy);
        
        IdentityDocument identityDocument = identityDocumentRepository.findById(id)
                .orElseThrow(() -> new IdentityDocumentNotFoundException("Identity document not found with ID: " + id));
        
        identityDocument.setVerified(true);
        identityDocument.setVerifiedBy(verifiedBy);
        identityDocument.setVerifiedAt(LocalDateTime.now());
        
        IdentityDocument savedDocument = identityDocumentRepository.save(identityDocument);
        
//        // Publish Kafka event
//        publishIdentityDocumentVerifiedEvent(savedDocument);
        
        log.info("Identity document verified successfully");
        
        return identityDocumentMapper.toResponse(savedDocument);
    }
    
    public List<IdentityDocumentResponse> getVerifiedIdentityDocuments() {
        log.info("Getting verified identity documents");
        
        List<IdentityDocument> documents = identityDocumentRepository.findByVerified(true);
        
        return documents.stream()
                .map(identityDocumentMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    public List<IdentityDocumentResponse> getUnverifiedIdentityDocuments() {
        log.info("Getting unverified identity documents");
        
        List<IdentityDocument> documents = identityDocumentRepository.findByVerified(false);
        
        return documents.stream()
                .map(identityDocumentMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    public List<IdentityDocumentResponse> getIdentityDocumentsByType(UUID applicationId, DocumentType documentType) {
        log.info("Getting identity documents for application: {} with type: {}", applicationId, documentType);
        
        List<IdentityDocument> documents = identityDocumentRepository.findByApplicationIdAndDocumentType(applicationId, documentType);
        
        return documents.stream()
                .map(identityDocumentMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    public Long countIdentityDocumentsByApplicationId(UUID applicationId) {
        return identityDocumentRepository.countByApplicationId(applicationId);
    }
    
    public Long countVerifiedIdentityDocumentsByApplicationId(UUID applicationId) {
        return identityDocumentRepository.countVerifiedByApplicationId(applicationId);
    }
    
    private void publishIdentityDocumentVerifiedEvent(IdentityDocument identityDocument) {
        try {
            String eventData = String.format(
                "{\"documentId\":\"%s\",\"applicationId\":\"%s\",\"documentType\":\"%s\",\"verifiedAt\":\"%s\",\"verifiedBy\":\"%s\"}",
                identityDocument.getId(),
                identityDocument.getApplicationId(),
                identityDocument.getDocumentType(),
                identityDocument.getVerifiedAt(),
                identityDocument.getVerifiedBy()
            );
            
            kafkaTemplate.send("identity-document-verified", identityDocument.getId().toString(), eventData);
            log.info("Published identity document verified event for document: {}", identityDocument.getId());
        } catch (Exception e) {
            log.error("Failed to publish identity document verified event for document: {}", identityDocument.getId(), e);
        }
    }
}
