package com.projectlos.loan_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "bureau_reports")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BureauReport {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "customer_id", nullable = false)
    private String customerId;
    
    @Column(name = "application_id", nullable = false)
    private String applicationId;
    
    @Column(name = "bureau_name")
    private String bureauName;
    
    @Column(name = "check_date")
    private LocalDateTime checkDate;
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "score")
    private Integer score;
    
    @Column(name = "recommendation")
    private String recommendation;
    
    @Column(name = "details", columnDefinition = "TEXT")
    private String details;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
