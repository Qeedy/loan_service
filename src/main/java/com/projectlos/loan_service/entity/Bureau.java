package com.projectlos.loan_service.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bureaus")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Bureau {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String applicationId;
    
    @Column(nullable = false)
    private String bureauType; // SID, SLIK, FINTECH, etc
    
    @Column
    private String reportId;
    
    @Column
    private Integer creditScore;
    
    @Column
    private String riskGrade;
    
    @Column
    private BigDecimal totalOutstanding;
    
    @Column
    private Integer numberOfAccounts;
    
    @Column(columnDefinition = "TEXT")
    private String paymentHistory; // JSON or detailed text
    
    @Column
    private LocalDate reportDate;
    
    @Column
    @Builder.Default
    private Integer reportValidityDays = 90;
    
    @Column
    @Builder.Default
    private String status = "ACTIVE";
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
