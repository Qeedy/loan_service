package com.projectlos.loan_service.repository;

import com.projectlos.loan_service.entity.DocumentSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface DocumentSequenceRepository extends JpaRepository<DocumentSequence, Long> {
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ds FROM DocumentSequence ds WHERE ds.prefix = :prefix")
    Optional<DocumentSequence> findByPrefixWithLock(@Param("prefix") String prefix);
    
    boolean existsByPrefix(String prefix);
    
    Optional<DocumentSequence> findByPrefix(String prefix);
}
