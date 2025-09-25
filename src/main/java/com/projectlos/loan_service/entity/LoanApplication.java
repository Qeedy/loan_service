package com.projectlos.loan_service.entity;

import com.projectlos.loan_service.enums.LoanStatus;
import com.projectlos.loan_service.enums.ProductType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class LoanApplication {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = true, nullable = false)
    private String applicationNumber;
    @Column(nullable = false)
    private String customerId;
    @Column(nullable = false)
    private String customerName;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductType productType;
    @Column(nullable = false)
    private BigDecimal requestedAmount;
    @Column(nullable = false)
    private Integer tenureMonths;
    @Column(nullable = false)
    private BigDecimal interestRate;
    @Column(nullable = false)
    private String purpose;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanStatus status;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    // scoring
    @Column
    private Integer score;
    @Column
    private String scoreGrade;
    @Column
    private String scoringModel;
    @Column(columnDefinition = "TEXT")
    private String scoringDetails;
    //checker verification
    @Column
    private Boolean applicationVerified;
    @Column
    private LocalDateTime applicationVerificationDate;
    @Column
    private Boolean addressVerified;
    @Column
    private LocalDateTime addressVerificationDate;
    @Column
    private Boolean contactVerified;
    @Column
    private LocalDateTime contactVerificationDate;
    @Column
    private String contactVerificationMethod;
    @Column
    private Boolean collateralVerified;
    @Column
    private LocalDateTime collateralVerificationDate;
    @Column
    private String verifiedBy;
    @Column
    private LocalDateTime verifiedAt;
    @Column(columnDefinition = "TEXT")
    private String checkerMessage;
    //rejection application
    @Column(columnDefinition = "TEXT")
    private String rejectionReason;
    @Column
    private LocalDateTime rejectedAt;
    @Column
    private String rejectedBy;
    // approver approval
    @Column
    private String approvedBy;
    @Column
    private LocalDateTime approvedAt;
    @Column
    private Boolean collateralApproved;
    @Column
    private LocalDateTime collateralApprovalDate;
    @Column
    private BigDecimal approvedAmount;
}
