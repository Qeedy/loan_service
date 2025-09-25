package com.projectlos.loan_service.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "risk_assessments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class RiskAssessment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String assessmentId;
    
    @Column(nullable = false)
    private String applicationId;
    
    @Column(nullable = false)
    private String customerId;
    
    // Assessment Details
    @Column(nullable = false)
    private String assessmentType; // MANUAL
    
    @Column(nullable = false)
    private String riskLevel; // LOW, MEDIUM, HIGH, CRITICAL
    
    @Column(nullable = false)
    private Integer riskScore; // 0-100
    
    // Assessment Metadata
    @Column(nullable = false)
    private String assessedBy; // CHECKER_USER_ID
    
    @Column
    private LocalDateTime assessmentDate;
    
    @Column(columnDefinition = "TEXT")
    private String remarks; // Checker notes
    
    // Status
    @Column
    @Builder.Default
    private String status = "ACTIVE"; // ACTIVE, EXPIRED, SUPERSEDED
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
