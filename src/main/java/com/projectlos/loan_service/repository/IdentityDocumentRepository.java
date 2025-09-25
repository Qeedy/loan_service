package com.projectlos.loan_service.repository;

import com.projectlos.loan_service.entity.IdentityDocument;
import com.projectlos.loan_service.enums.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IdentityDocumentRepository extends JpaRepository<IdentityDocument, UUID> {
    
    List<IdentityDocument> findByApplicationId(UUID applicationId);
    
    List<IdentityDocument> findByVerified(Boolean verified);
    
    @Query("SELECT id FROM IdentityDocument id WHERE id.applicationId = :applicationId AND id.verified = :verified")
    List<IdentityDocument> findByApplicationIdAndVerified(@Param("applicationId") UUID applicationId, 
                                                          @Param("verified") Boolean verified);
    
    @Query("SELECT id FROM IdentityDocument id WHERE id.applicationId = :applicationId AND id.documentType = :documentType")
    List<IdentityDocument> findByApplicationIdAndDocumentType(@Param("applicationId") UUID applicationId, 
                                                              @Param("documentType") DocumentType documentType);
    
    @Query("SELECT COUNT(id) FROM IdentityDocument id WHERE id.applicationId = :applicationId")
    Long countByApplicationId(@Param("applicationId") UUID applicationId);
    
    @Query("SELECT COUNT(id) FROM IdentityDocument id WHERE id.applicationId = :applicationId AND id.verified = true")
    Long countVerifiedByApplicationId(@Param("applicationId") UUID applicationId);
    
    List<IdentityDocument> findByDocumentType(DocumentType documentType);
}
