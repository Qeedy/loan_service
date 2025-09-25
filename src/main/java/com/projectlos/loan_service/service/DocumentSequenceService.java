package com.projectlos.loan_service.service;

import com.projectlos.loan_service.entity.DocumentSequence;
import com.projectlos.loan_service.exception.DocumentSequenceException;
import com.projectlos.loan_service.repository.DocumentSequenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DocumentSequenceService {
    
    private final DocumentSequenceRepository documentSequenceRepository;
    
    @Transactional
    public String generateNextSequence(String prefix) {
        if (prefix == null || prefix.trim().isEmpty()) {
            throw new DocumentSequenceException("Prefix cannot be null or empty");
        }
        prefix = prefix.trim().toUpperCase();
        Optional<DocumentSequence> existingSequence = documentSequenceRepository
                .findByPrefixWithLock(prefix);
        if (existingSequence.isPresent()) {
            DocumentSequence sequence = existingSequence.get();
            sequence.setNumber(sequence.getNumber() + 1);
            documentSequenceRepository.save(sequence);
            return prefix + String.format("%06d", sequence.getNumber());
        } else {
            DocumentSequence newSequence = DocumentSequence.builder()
                    .prefix(prefix)
                    .number(1L)
                    .build();
            documentSequenceRepository.save(newSequence);
            return prefix + "000001";
        }
    }
    
    public Optional<DocumentSequence> getSequenceByPrefix(String prefix) {
        return documentSequenceRepository.findByPrefix(prefix);
    }
    
    public boolean prefixExists(String prefix) {
        return documentSequenceRepository.existsByPrefix(prefix);
    }
}
