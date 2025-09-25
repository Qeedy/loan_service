package com.projectlos.loan_service.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// @Entity
// @Table(name = "disbursements")
// @Data
// @NoArgsConstructor
// @AllArgsConstructor
// @Builder
// @EntityListeners(AuditingEntityListener.class)
public class Disbursement {
    
    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    // private Long id;
    
    // @Column(unique = true, nullable = false)
    // private String disbursementId;
    
    // @Column(nullable = false)
    // private String applicationId;
    
    // @Column(nullable = false)
    // private String customerId;
    
    // @Column(nullable = false)
    // private BigDecimal amount;
    
    // @Column(nullable = false)
    // private String bankAccount;
    
    // @Column(nullable = false)
    // private String bankName;
    
    // @Column(nullable = false)
    // private String accountHolderName;
    
    // @Column(nullable = false)
    // private String status;
    
    // @Column
    // private String disbursementMethod;
    
    // @Column
    // private String referenceNumber;
    
    // @Column
    // private LocalDateTime scheduledDate;
    
    // @Column
    // private LocalDateTime disbursedAt;
    
    // @Column
    // private String disbursedBy;
    
    // @Column(columnDefinition = "CLOB")
    // private String remarks;
    
    // @CreatedDate
    // @Column(nullable = false, updatable = false)
    // private LocalDateTime createdAt;
    
    // @LastModifiedDate
    // @Column(nullable = false)
    // private LocalDateTime updatedAt;
}
