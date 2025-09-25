package com.projectlos.loan_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.projectlos.loan_service.enums.LoanStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    
    private String taskId;
    private String taskName;
    private String applicationId;
    private String applicationNumber;
    private String customerId;
    private String customerName;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime createdAt;
    private LoanStatus status;
}
