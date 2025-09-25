package com.projectlos.loan_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanDecision {
    
    private String decision; // APPROVED, REJECTED, MANUAL_REVIEW
    private String recommendation; // APPROVE_HIGH_LIMIT, APPROVE_STANDARD_LIMIT, etc.
    private BigDecimal approvedAmount;
    private int approvedTenure;
    private BigDecimal interestRate;
    private String decisionReason;
    private LocalDateTime decidedAt;
    private String decidedBy; // System or user ID
    
    public LoanDecision(String decision, String recommendation, String decisionReason) {
        this.decision = decision;
        this.recommendation = recommendation;
        this.decisionReason = decisionReason;
        this.decidedAt = LocalDateTime.now();
        this.decidedBy = "SYSTEM";
    }
    
    public static LoanDecision approve(BigDecimal amount, int tenure, BigDecimal rate, String reason) {
        LoanDecision decision = new LoanDecision();
        decision.setDecision("APPROVED");
        decision.setApprovedAmount(amount);
        decision.setApprovedTenure(tenure);
        decision.setInterestRate(rate);
        decision.setDecisionReason(reason);
        decision.setDecidedAt(LocalDateTime.now());
        decision.setDecidedBy("SYSTEM");
        return decision;
    }
    
    public static LoanDecision reject(String reason) {
        LoanDecision decision = new LoanDecision();
        decision.setDecision("REJECTED");
        decision.setDecisionReason(reason);
        decision.setDecidedAt(LocalDateTime.now());
        decision.setDecidedBy("SYSTEM");
        return decision;
    }
    
    public static LoanDecision manualReview(String reason) {
        LoanDecision decision = new LoanDecision();
        decision.setDecision("MANUAL_REVIEW");
        decision.setDecisionReason(reason);
        decision.setDecidedAt(LocalDateTime.now());
        decision.setDecidedBy("SYSTEM");
        return decision;
    }
}
