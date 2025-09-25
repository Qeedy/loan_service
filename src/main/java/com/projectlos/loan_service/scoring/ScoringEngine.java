package com.projectlos.loan_service.scoring;

import com.projectlos.loan_service.dto.LoanApplicationRequest;
import com.projectlos.loan_service.entity.ScoringResult;
import com.projectlos.loan_service.enums.ProductType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ScoringEngine {
    
    public ScoringResult calculateScore(LoanApplicationRequest request) {
        return switch (request.getProductType()) {
            case KUR -> calculateKURScore(request);
            case CONSUMER -> calculatePersonalScore(request);
            case WORKING_CAPITAL -> calculateWorkingCapitalScore(request);
            case KPR -> calculateMortgageScore(request);
            case KKB -> calculateVehicleScore(request);
            case INVESTMENT -> calculateInvestmentScore(request);
            case MICRO -> calculateMicroScore(request);
            case ULTRA_MICRO -> calculateUltraMicroScore(request);
            case KTA -> calculateKTAScore(request);
            default -> calculateDefaultScore(request);
        };
    }
    
    private ScoringResult calculateKURScore(LoanApplicationRequest request) {
        int score = 0;
        StringBuilder details = new StringBuilder();
        
        // Amount scoring (0-30 points)
        if (request.getRequestedAmount() != null) {
            if (request.getRequestedAmount().compareTo(new BigDecimal("10000000")) <= 0) {
                score += 30;
                details.append("Amount <= 10M: +30\n");
            } else if (request.getRequestedAmount().compareTo(new BigDecimal("25000000")) <= 0) {
                score += 20;
                details.append("Amount <= 25M: +20\n");
            } else {
                score += 10;
                details.append("Amount > 25M: +10\n");
            }
        }
        
        // Tenure scoring (0-20 points)
        if (request.getTenureMonths() != null) {
            if (request.getTenureMonths() <= 24) {
                score += 20;
                details.append("Tenure <= 24 months: +20\n");
            } else if (request.getTenureMonths() <= 36) {
                score += 15;
                details.append("Tenure <= 36 months: +15\n");
            } else {
                score += 10;
                details.append("Tenure > 36 months: +10\n");
            }
        }
        
        // Interest rate scoring (0-25 points)
        if (request.getInterestRate() != null) {
            if (request.getInterestRate().compareTo(new BigDecimal("6")) <= 0) {
                score += 25;
                details.append("Interest rate <= 6%: +25\n");
            } else if (request.getInterestRate().compareTo(new BigDecimal("9")) <= 0) {
                score += 20;
                details.append("Interest rate <= 9%: +20\n");
            } else {
                score += 15;
                details.append("Interest rate > 9%: +15\n");
            }
        }
        
        
        
        return new ScoringResult(score, "KUR_SCORING_V1", details.toString());
    }
    
    private ScoringResult calculatePersonalScore(LoanApplicationRequest request) {
        int score = 0;
        StringBuilder details = new StringBuilder();
        
        // Amount scoring (0-30 points)
        if (request.getRequestedAmount() != null) {
            if (request.getRequestedAmount().compareTo(new BigDecimal("25000000")) <= 0) {
                score += 30;
                details.append("Amount <= 25M: +30\n");
            } else if (request.getRequestedAmount().compareTo(new BigDecimal("50000000")) <= 0) {
                score += 20;
                details.append("Amount <= 50M: +20\n");
            } else {
                score += 10;
                details.append("Amount > 50M: +10\n");
            }
        }
        
        // Tenure scoring (0-25 points)
        if (request.getTenureMonths() != null) {
            if (request.getTenureMonths() <= 36) {
                score += 25;
                details.append("Tenure <= 36 months: +25\n");
            } else if (request.getTenureMonths() <= 60) {
                score += 20;
                details.append("Tenure <= 60 months: +20\n");
            } else {
                score += 15;
                details.append("Tenure > 60 months: +15\n");
            }
        }
        
        // Interest rate scoring (0-20 points)
        if (request.getInterestRate() != null) {
            if (request.getInterestRate().compareTo(new BigDecimal("12")) <= 0) {
                score += 20;
                details.append("Interest rate <= 12%: +20\n");
            } else if (request.getInterestRate().compareTo(new BigDecimal("18")) <= 0) {
                score += 15;
                details.append("Interest rate <= 18%: +15\n");
            } else {
                score += 10;
                details.append("Interest rate > 18%: +10\n");
            }
        }
        
        
        // Bureau scoring (0-10 points)
        if (false) {
            score += 10;
            details.append("Has bureau report: +10\n");
        }
        
        return new ScoringResult(score, "PERSONAL_SCORING_V1", details.toString());
    }
    
    private ScoringResult calculateWorkingCapitalScore(LoanApplicationRequest request) {
        int score = 0;
        StringBuilder details = new StringBuilder();
        
        // Amount scoring (0-35 points)
        if (request.getRequestedAmount() != null) {
            if (request.getRequestedAmount().compareTo(new BigDecimal("50000000")) <= 0) {
                score += 35;
                details.append("Amount <= 50M: +35\n");
            } else if (request.getRequestedAmount().compareTo(new BigDecimal("100000000")) <= 0) {
                score += 25;
                details.append("Amount <= 100M: +25\n");
            } else {
                score += 15;
                details.append("Amount > 100M: +15\n");
            }
        }
        
        // Tenure scoring (0-25 points)
        if (request.getTenureMonths() != null) {
            if (request.getTenureMonths() <= 12) {
                score += 25;
                details.append("Tenure <= 12 months: +25\n");
            } else if (request.getTenureMonths() <= 24) {
                score += 20;
                details.append("Tenure <= 24 months: +20\n");
            } else {
                score += 15;
                details.append("Tenure > 24 months: +15\n");
            }
        }
        
        // Interest rate scoring (0-20 points)
        if (request.getInterestRate() != null) {
            if (request.getInterestRate().compareTo(new BigDecimal("10")) <= 0) {
                score += 20;
                details.append("Interest rate <= 10%: +20\n");
            } else if (request.getInterestRate().compareTo(new BigDecimal("15")) <= 0) {
                score += 15;
                details.append("Interest rate <= 15%: +15\n");
            } else {
                score += 10;
                details.append("Interest rate > 15%: +10\n");
            }
        }
        
        
        return new ScoringResult(score, "WORKING_CAPITAL_SCORING_V1", details.toString());
    }
    
    private ScoringResult calculateMortgageScore(LoanApplicationRequest request) {
        int score = 0;
        StringBuilder details = new StringBuilder();
        
        // Amount scoring (0-40 points)
        if (request.getRequestedAmount() != null) {
            if (request.getRequestedAmount().compareTo(new BigDecimal("500000000")) <= 0) {
                score += 40;
                details.append("Amount <= 500M: +40\n");
            } else if (request.getRequestedAmount().compareTo(new BigDecimal("1000000000")) <= 0) {
                score += 30;
                details.append("Amount <= 1B: +30\n");
            } else {
                score += 20;
                details.append("Amount > 1B: +20\n");
            }
        }
        
        // Tenure scoring (0-30 points)
        if (request.getTenureMonths() != null) {
            if (request.getTenureMonths() <= 240) {
                score += 30;
                details.append("Tenure <= 20 years: +30\n");
            } else if (request.getTenureMonths() <= 300) {
                score += 25;
                details.append("Tenure <= 25 years: +25\n");
            } else {
                score += 20;
                details.append("Tenure > 25 years: +20\n");
            }
        }
        
        // Interest rate scoring (0-20 points)
        if (request.getInterestRate() != null) {
            if (request.getInterestRate().compareTo(new BigDecimal("8")) <= 0) {
                score += 20;
                details.append("Interest rate <= 8%: +20\n");
            } else if (request.getInterestRate().compareTo(new BigDecimal("12")) <= 0) {
                score += 15;
                details.append("Interest rate <= 12%: +15\n");
            } else {
                score += 10;
                details.append("Interest rate > 12%: +10\n");
            }
        }
        
        
        return new ScoringResult(score, "MORTGAGE_SCORING_V1", details.toString());
    }
    
    private ScoringResult calculateVehicleScore(LoanApplicationRequest request) {
        int score = 0;
        StringBuilder details = new StringBuilder();
        
        // Amount scoring (0-30 points)
        if (request.getRequestedAmount() != null) {
            if (request.getRequestedAmount().compareTo(new BigDecimal("100000000")) <= 0) {
                score += 30;
                details.append("Amount <= 100M: +30\n");
            } else if (request.getRequestedAmount().compareTo(new BigDecimal("250000000")) <= 0) {
                score += 25;
                details.append("Amount <= 250M: +25\n");
            } else {
                score += 20;
                details.append("Amount > 250M: +20\n");
            }
        }
        
        // Tenure scoring (0-25 points)
        if (request.getTenureMonths() != null) {
            if (request.getTenureMonths() <= 36) {
                score += 25;
                details.append("Tenure <= 36 months: +25\n");
            } else if (request.getTenureMonths() <= 60) {
                score += 20;
                details.append("Tenure <= 60 months: +20\n");
            } else {
                score += 15;
                details.append("Tenure > 60 months: +15\n");
            }
        }
        
        // Interest rate scoring (0-25 points)
        if (request.getInterestRate() != null) {
            if (request.getInterestRate().compareTo(new BigDecimal("10")) <= 0) {
                score += 25;
                details.append("Interest rate <= 10%: +25\n");
            } else if (request.getInterestRate().compareTo(new BigDecimal("15")) <= 0) {
                score += 20;
                details.append("Interest rate <= 15%: +20\n");
            } else {
                score += 15;
                details.append("Interest rate > 15%: +15\n");
            }
        }
        
        
        return new ScoringResult(score, "VEHICLE_SCORING_V1", details.toString());
    }
    
    private ScoringResult calculateDefaultScore(LoanApplicationRequest request) {
        int score = 50; // Default score
        StringBuilder details = new StringBuilder("Default scoring applied: 50 points\n");
        
        // Basic scoring for unknown product types
        if (request.getRequestedAmount() != null && request.getRequestedAmount().compareTo(new BigDecimal("10000000")) <= 0) {
            score += 20;
            details.append("Amount <= 10M: +20\n");
        }
        
        if (false) {
            score += 15;
            details.append("Has collateral: +15\n");
        }
        
        return new ScoringResult(score, "DEFAULT_SCORING_V1", details.toString());
    }
    
    private ScoringResult calculateInvestmentScore(LoanApplicationRequest request) {
        int score = 0;
        StringBuilder details = new StringBuilder();
        
        // Amount scoring (0-25 points)
        if (request.getRequestedAmount() != null) {
            if (request.getRequestedAmount().compareTo(new BigDecimal("50000000")) <= 0) {
                score += 25;
                details.append("Amount <= 50M: +25\n");
            } else if (request.getRequestedAmount().compareTo(new BigDecimal("100000000")) <= 0) {
                score += 20;
                details.append("Amount <= 100M: +20\n");
            } else {
                score += 15;
                details.append("Amount > 100M: +15\n");
            }
        }
        
        
        // Tenure scoring (0-20 points)
        if (request.getTenureMonths() != null) {
            if (request.getTenureMonths() <= 12) {
                score += 20;
                details.append("Tenure <= 12 months: +20\n");
            } else if (request.getTenureMonths() <= 24) {
                score += 15;
                details.append("Tenure <= 24 months: +15\n");
            } else {
                score += 10;
                details.append("Tenure > 24 months: +10\n");
            }
        }
        
        // Interest rate scoring (0-25 points)
        if (request.getInterestRate() != null) {
            if (request.getInterestRate().compareTo(new BigDecimal("12")) <= 0) {
                score += 25;
                details.append("Interest rate <= 12%: +25\n");
            } else if (request.getInterestRate().compareTo(new BigDecimal("15")) <= 0) {
                score += 20;
                details.append("Interest rate <= 15%: +20\n");
            } else {
                score += 15;
                details.append("Interest rate > 15%: +15\n");
            }
        }
        
        return new ScoringResult(score, "INVESTMENT_SCORING_V1", details.toString());
    }
    
    private ScoringResult calculateMicroScore(LoanApplicationRequest request) {
        int score = 0;
        StringBuilder details = new StringBuilder();
        
        // Amount scoring (0-30 points)
        if (request.getRequestedAmount() != null) {
            if (request.getRequestedAmount().compareTo(new BigDecimal("5000000")) <= 0) {
                score += 30;
                details.append("Amount <= 5M: +30\n");
            } else if (request.getRequestedAmount().compareTo(new BigDecimal("10000000")) <= 0) {
                score += 25;
                details.append("Amount <= 10M: +25\n");
            } else {
                score += 20;
                details.append("Amount > 10M: +20\n");
            }
        }
        
        
        // Tenure scoring (0-25 points)
        if (request.getTenureMonths() != null) {
            if (request.getTenureMonths() <= 6) {
                score += 25;
                details.append("Tenure <= 6 months: +25\n");
            } else if (request.getTenureMonths() <= 12) {
                score += 20;
                details.append("Tenure <= 12 months: +20\n");
            } else {
                score += 15;
                details.append("Tenure > 12 months: +15\n");
            }
        }
        
        // Interest rate scoring (0-20 points)
        if (request.getInterestRate() != null) {
            if (request.getInterestRate().compareTo(new BigDecimal("15")) <= 0) {
                score += 20;
                details.append("Interest rate <= 15%: +20\n");
            } else if (request.getInterestRate().compareTo(new BigDecimal("20")) <= 0) {
                score += 15;
                details.append("Interest rate <= 20%: +15\n");
            } else {
                score += 10;
                details.append("Interest rate > 20%: +10\n");
            }
        }
        
        return new ScoringResult(score, "MICRO_SCORING_V1", details.toString());
    }
    
    private ScoringResult calculateUltraMicroScore(LoanApplicationRequest request) {
        int score = 0;
        StringBuilder details = new StringBuilder();
        
        // Amount scoring (0-35 points)
        if (request.getRequestedAmount() != null) {
            if (request.getRequestedAmount().compareTo(new BigDecimal("2000000")) <= 0) {
                score += 35;
                details.append("Amount <= 2M: +35\n");
            } else if (request.getRequestedAmount().compareTo(new BigDecimal("5000000")) <= 0) {
                score += 30;
                details.append("Amount <= 5M: +30\n");
            } else {
                score += 25;
                details.append("Amount > 5M: +25\n");
            }
        }
        
        
        // Tenure scoring (0-30 points)
        if (request.getTenureMonths() != null) {
            if (request.getTenureMonths() <= 3) {
                score += 30;
                details.append("Tenure <= 3 months: +30\n");
            } else if (request.getTenureMonths() <= 6) {
                score += 25;
                details.append("Tenure <= 6 months: +25\n");
            } else {
                score += 20;
                details.append("Tenure > 6 months: +20\n");
            }
        }
        
        // Interest rate scoring (0-15 points)
        if (request.getInterestRate() != null) {
            if (request.getInterestRate().compareTo(new BigDecimal("20")) <= 0) {
                score += 15;
                details.append("Interest rate <= 20%: +15\n");
            } else if (request.getInterestRate().compareTo(new BigDecimal("25")) <= 0) {
                score += 10;
                details.append("Interest rate <= 25%: +10\n");
            } else {
                score += 5;
                details.append("Interest rate > 25%: +5\n");
            }
        }
        
        return new ScoringResult(score, "ULTRA_MICRO_SCORING_V1", details.toString());
    }
    
    private ScoringResult calculateKTAScore(LoanApplicationRequest request) {
        int score = 0;
        StringBuilder details = new StringBuilder();
        
        // Amount scoring (0-25 points)
        if (request.getRequestedAmount() != null) {
            if (request.getRequestedAmount().compareTo(new BigDecimal("20000000")) <= 0) {
                score += 25;
                details.append("Amount <= 20M: +25\n");
            } else if (request.getRequestedAmount().compareTo(new BigDecimal("50000000")) <= 0) {
                score += 20;
                details.append("Amount <= 50M: +20\n");
            } else {
                score += 15;
                details.append("Amount > 50M: +15\n");
            }
        }
        
        // Collateral scoring (0-35 points) - KTA doesn't require collateral but having one is a plus
        if (false) {
            score += 35;
            details.append("Has collateral (bonus): +35\n");
        }
        
        // Tenure scoring (0-25 points)
        if (request.getTenureMonths() != null) {
            if (request.getTenureMonths() <= 12) {
                score += 25;
                details.append("Tenure <= 12 months: +25\n");
            } else if (request.getTenureMonths() <= 24) {
                score += 20;
                details.append("Tenure <= 24 months: +20\n");
            } else {
                score += 15;
                details.append("Tenure > 24 months: +15\n");
            }
        }
        
        // Interest rate scoring (0-15 points)
        if (request.getInterestRate() != null) {
            if (request.getInterestRate().compareTo(new BigDecimal("18")) <= 0) {
                score += 15;
                details.append("Interest rate <= 18%: +15\n");
            } else if (request.getInterestRate().compareTo(new BigDecimal("22")) <= 0) {
                score += 10;
                details.append("Interest rate <= 22%: +10\n");
            } else {
                score += 5;
                details.append("Interest rate > 22%: +5\n");
            }
        }
        
        return new ScoringResult(score, "KTA_SCORING_V1", details.toString());
    }
}
