package com.projectlos.loan_service.repository;

import com.projectlos.loan_service.entity.BureauReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BureauReportRepository extends JpaRepository<BureauReport, Long> {
    
    Optional<BureauReport> findByApplicationId(String applicationId);
    
    Optional<BureauReport> findByCustomerIdAndApplicationId(String customerId, String applicationId);
}
