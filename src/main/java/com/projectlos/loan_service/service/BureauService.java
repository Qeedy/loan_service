package com.projectlos.loan_service.service;

import com.projectlos.loan_service.entity.BureauReport;
import com.projectlos.loan_service.entity.LoanApplication;
import com.projectlos.loan_service.enums.LoanStatus;
import com.projectlos.loan_service.exception.BureauServiceException;
import com.projectlos.loan_service.exception.LoanApplicationNotFoundException;
import com.projectlos.loan_service.repository.BureauReportRepository;
import com.projectlos.loan_service.repository.LoanApplicationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BureauService {
    
    private final BureauReportRepository bureauReportRepository;
    private final LoanApplicationRepository loanApplicationRepository;
    
    @Value("${bureau.service.url:http://bureau-service:8085}")
    private String bureauServiceUrl;
    

    //will use in future feature
    public void checkBureau(String customerId, String applicationNumber) {
        log.info("Checking bureau for customer: {} and application: {}", customerId, applicationNumber);
        LoanApplication application = loanApplicationRepository.findByApplicationNumber(applicationNumber)
                .orElseThrow(() -> new RuntimeException("Application not found: " + applicationNumber));
        // Update application status
        application.setStatus(LoanStatus.SUBMITTED);
        loanApplicationRepository.save(application);
        // try {
        //     // Prepare request
        //     BureauCheckRequest request = BureauCheckRequest.builder()
        //             .customerId(customerId)
        //             .applicationId(applicationId)
        //             .checkDate(LocalDateTime.now())
        //             .build();
            
        //     // Call bureau service
        //     String url = bureauServiceUrl + "/api/bureau/check";
        //     HttpHeaders headers = new HttpHeaders();
        //     headers.set("Content-Type", "application/json");
            
        //     HttpEntity<BureauCheckRequest> entity = new HttpEntity<>(request, headers);
        //     ResponseEntity<BureauCheckResponse> response = restTemplate.exchange(
        //             url, HttpMethod.POST, entity, BureauCheckResponse.class);
            
        //     BureauCheckResponse bureauResponse = response.getBody();
        //     if (bureauResponse != null) {
        //         // Save bureau report
        //         saveBureauReport(customerId, applicationId, bureauResponse);
        //         log.info("Bureau check completed successfully for customer: {}", customerId);
        //     }
            
        //     return bureauResponse;
            
        // } catch (Exception e) {
        //     log.error("Error checking bureau for customer: {}", customerId, e);
            
        //     // Return default response in case of error
        //     return BureauCheckResponse.builder()
        //             .customerId(customerId)
        //             .applicationId(applicationId)
        //             .checkDate(LocalDateTime.now())
        //             .status("ERROR")
        //             .score(0)
        //             .recommendation("MANUAL_REVIEW")
        //             .details("Bureau check failed: " + e.getMessage())
        //             .build();
        // }
    }
    
    // Will use in future feature
    // private void saveBureauReport(String customerId, String applicationId, BureauCheckResponse response) {
    //     log.info("Saving bureau report for customer: {} and application: {}", customerId, applicationId);
    //     
    //     BureauReport bureauReport = BureauReport.builder()
    //             .customerId(customerId)
    //             .applicationId(applicationId)
    //             .bureauName(response.getBureauName())
    //             .checkDate(response.getCheckDate())
    //             .status(response.getStatus())
    //             .score(response.getScore())
    //             .recommendation(response.getRecommendation())
    //             .details(response.getDetails())
    //             .createdAt(LocalDateTime.now())
    //             .build();
    //     
    //     bureauReportRepository.save(bureauReport);
    //     log.info("Bureau report saved successfully");
    // }
    
    public Map<String, Object> getBureauReport(String applicationId) {
        log.info("Getting bureau report for application: {}", applicationId);
        
        BureauReport report = bureauReportRepository.findByApplicationId(applicationId)
                .orElse(null);
        
        if (report == null) {
            return new HashMap<>();
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("customerId", report.getCustomerId());
        result.put("applicationId", report.getApplicationId());
        result.put("bureauName", report.getBureauName());
        result.put("checkDate", report.getCheckDate());
        result.put("status", report.getStatus());
        result.put("score", report.getScore());
        result.put("recommendation", report.getRecommendation());
        result.put("details", report.getDetails());
        
        return result;
    }
}
