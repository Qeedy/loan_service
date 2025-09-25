package com.projectlos.loan_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BureauCheckResponse {
    private String customerId;
    private String applicationId;
    private String bureauName;
    private LocalDateTime checkDate;
    private String status;
    private Integer score;
    private String recommendation;
    private String details;
}
