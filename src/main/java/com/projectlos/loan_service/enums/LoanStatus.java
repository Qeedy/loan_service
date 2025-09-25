package com.projectlos.loan_service.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "Loan Status types")
public enum LoanStatus {
    DRAFT("Draft"),
    SUBMITTED("Submitted"),
    VERIFIED("Verified"),
    APPROVED("Approved"),
    DISBURSED("Disbursed"),
    SETTLED("Settled"),
    REJECTED("Rejected"),
    ALL("All");
    public String description;
}
