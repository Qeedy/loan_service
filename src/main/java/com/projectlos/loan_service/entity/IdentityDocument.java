package com.projectlos.loan_service.entity;

import com.projectlos.loan_service.enums.DocumentType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "identity_documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class IdentityDocument {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false)
    private UUID applicationId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentType documentType;
    
    @Column(nullable = false)
    private String documentNumber; // Nomor identitas
    
    @Column
    private LocalDate expiryDate; // Tanggal kadaluarsa
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean verified = false;
    
    @Column
    private String verifiedBy;
    
    @Column
    private LocalDateTime verifiedAt;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}