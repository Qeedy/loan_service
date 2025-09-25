package com.projectlos.loan_service.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

// @Entity
// @Table(name = "settlements")
// @Data
// @NoArgsConstructor
// @AllArgsConstructor
// @Builder
// @EntityListeners(AuditingEntityListener.class)
public class Settlement {
    
    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    // private Long id;
    
    // @Column(unique = true, nullable = false)
    // private String settlementId;
    
    // @Column(nullable = false)
    // private String applicationId;
    
    // @Column(nullable = false)
    // private String customerId;
    
    // @Column
    // private String settlementType; // EARLY_PAYOFF, REFINANCE, DEFAULT, NORMAL
    
    // @Column(nullable = false)
    // private BigDecimal settlementAmount;
    
    // @Column(nullable = false)
    // private BigDecimal outstandingAmount;
    
    // @Column(nullable = false)
    // private LocalDate settlementDate;
    
    // @Column
    // private String settlementMethod; // BANK_TRANSFER, CASH, CHECK
    
    // @Column
    // private String referenceNumber;
    
    // @Column(columnDefinition = "CLOB")
    // private String remarks;
    
    // @Column
    // @Builder.Default
    // private String status = "PENDING"; // PENDING, COMPLETED, FAILED
    
    // @Column
    // private String processedBy;
    
    // @Column
    // private LocalDateTime processedAt;
    
    // @CreatedDate
    // @Column(nullable = false, updatable = false)
    // private LocalDateTime createdAt;
}
