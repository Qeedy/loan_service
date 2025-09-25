package com.projectlos.loan_service.repository;

import com.projectlos.loan_service.entity.LoanApplication;
import com.projectlos.loan_service.enums.LoanStatus;
import com.projectlos.loan_service.enums.ProductType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, UUID> {
    
    Optional<LoanApplication> findByApplicationNumber(String applicationNumber);
    
    List<LoanApplication> findByCustomerId(String customerId);
    
    List<LoanApplication> findByStatus(String status);
    
    List<LoanApplication> findByProductType(String productType);
    
    
    @Query("SELECT la FROM LoanApplication la WHERE la.customerId = :customerId AND la.status IN :statuses")
    List<LoanApplication> findByCustomerIdAndStatusIn(@Param("customerId") String customerId, 
                                                     @Param("statuses") List<String> statuses);
    
    @Query("SELECT la FROM LoanApplication la WHERE la.status = :status AND la.createdAt >= :fromDate")
    List<LoanApplication> findByStatusAndCreatedAtAfter(@Param("status") String status,
                                                       @Param("fromDate") LocalDateTime fromDate);
    
    @Query("SELECT la FROM LoanApplication la WHERE la.customerId = :customerId AND la.productType = :productType")
    List<LoanApplication> findByCustomerIdAndProductType(@Param("customerId") String customerId, 
                                                        @Param("productType") String productType);
    
    @Query("SELECT la FROM LoanApplication la WHERE la.status = :status AND la.productType = :productType")
    List<LoanApplication> findByStatusAndProductType(@Param("status") String status, 
                                                    @Param("productType") String productType);
    
    @Query("SELECT la FROM LoanApplication la WHERE la.approvedBy = :approvedBy")
    List<LoanApplication> findByApprovedBy(@Param("approvedBy") String approvedBy);
    
    @Query("SELECT la FROM LoanApplication la WHERE la.addressVerified = :addressVerified")
    List<LoanApplication> findByAddressVerified(@Param("addressVerified") Boolean addressVerified);
    
    @Query("SELECT la FROM LoanApplication la WHERE la.contactVerified = :contactVerified")
    List<LoanApplication> findByContactVerified(@Param("contactVerified") Boolean contactVerified);
    
    @Query("SELECT la FROM LoanApplication la WHERE la.score >= :minScore AND la.score <= :maxScore")
    List<LoanApplication> findByScoreRange(@Param("minScore") Integer minScore, 
                                          @Param("maxScore") Integer maxScore);
    
    @Query("SELECT la FROM LoanApplication la WHERE la.createdAt BETWEEN :startDate AND :endDate")
    Page<LoanApplication> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate,
                                                @Param("endDate") LocalDateTime endDate,
                                                Pageable pageable);
    
    @Query("SELECT la FROM LoanApplication la WHERE " +
           "(:customerName = '' OR LOWER(la.customerName) LIKE LOWER(CONCAT('%', :customerName, '%'))) AND " +
           "(:applicationNumber = '' OR LOWER(la.applicationNumber) LIKE LOWER(CONCAT('%', :applicationNumber, '%'))) AND " +
           "(:status is NULL OR la.status = :status) AND " +
           "(:productType is NULL OR la.productType = :productType)")
    Page<LoanApplication> searchLoanApplications(
            @Param("customerName") String customerName,
            @Param("applicationNumber") String applicationNumber,
            @Param("status") String status,
            @Param("productType") String productType,
            Pageable pageable);
}
