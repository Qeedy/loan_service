package com.projectlos.loan_service.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScoringResult {
    
    private int score;
    private String scoringModel;
    private String scoringDetails;
    private LocalDateTime scoredAt;
    
    public ScoringResult(int score, String scoringModel, String scoringDetails) {
        this.score = score;
        this.scoringModel = scoringModel;
        this.scoringDetails = scoringDetails;
        this.scoredAt = LocalDateTime.now();
    }
    
    public String getScoreGrade() {
        if (score >= 80) return "A";
        if (score >= 70) return "B";
        if (score >= 60) return "C";
        if (score >= 50) return "D";
        return "E";
    }
    
    public boolean isApproved() {
        return score >= 60; // Minimum score for approval
    }
    
    public String getRecommendation() {
        if (score >= 80) return "APPROVE_HIGH_LIMIT";
        if (score >= 70) return "APPROVE_STANDARD_LIMIT";
        if (score >= 60) return "APPROVE_REDUCED_LIMIT";
        if (score >= 50) return "REVIEW_MANUAL";
        return "REJECT";
    }
}
