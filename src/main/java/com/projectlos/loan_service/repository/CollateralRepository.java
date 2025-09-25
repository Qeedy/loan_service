package com.projectlos.loan_service.repository;

import com.projectlos.loan_service.entity.Collateral;
import com.projectlos.loan_service.enums.CollateralType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CollateralRepository extends JpaRepository<Collateral, UUID> {
    
    List<Collateral> findByApplicationId(UUID applicationId);
    
    List<Collateral> findByVerified(Boolean verified);
    
    @Query("SELECT c FROM Collateral c WHERE c.applicationId = :applicationId AND c.verified = :verified")
    List<Collateral> findByApplicationIdAndVerified(@Param("applicationId") UUID applicationId, 
                                                   @Param("verified") Boolean verified);
    
    @Query("SELECT COUNT(c) FROM Collateral c WHERE c.applicationId = :applicationId")
    Long countByApplicationId(@Param("applicationId") UUID applicationId);
    
    @Query("SELECT COUNT(c) FROM Collateral c WHERE c.applicationId = :applicationId AND c.verified = true")
    Long countVerifiedByApplicationId(@Param("applicationId") UUID applicationId);
    
    List<Collateral> findByCollateralType(CollateralType collateralType);
    
    @Query("SELECT c FROM Collateral c WHERE c.applicationId = :applicationId AND c.collateralType = :collateralType")
    List<Collateral> findByApplicationIdAndCollateralType(@Param("applicationId") UUID applicationId, 
                                                          @Param("collateralType") CollateralType collateralType);
}
