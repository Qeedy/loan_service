package com.projectlos.loan_service.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Collateral types")
public enum CollateralType {
    
    @Schema(description = "Real estate property")
    PROPERTY("Property", "Real estate property"),
    
    @Schema(description = "Motor vehicle")
    VEHICLE("Vehicle", "Motor vehicle"),
    
    @Schema(description = "Bank deposit or savings")
    DEPOSIT("Deposit", "Bank deposit or savings"),
    
    @Schema(description = "Investment instruments")
    INVESTMENT("Investment", "Investment instruments"),
    
    @Schema(description = "Precious metals")
    PRECIOUS_METALS("Precious Metals", "Precious metals"),
    
    @Schema(description = "Equipment or machinery")
    EQUIPMENT("Equipment", "Equipment or machinery"),
    
    @Schema(description = "Inventory or stock")
    INVENTORY("Inventory", "Inventory or stock"),
    
    @Schema(description = "Other types of collateral")
    OTHER("Other", "Other types of collateral");
    
    private final String displayName;
    private final String description;
    
    CollateralType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
}
