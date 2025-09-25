package com.projectlos.loan_service.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Loan product types")
public enum ProductType {
    
    @Schema(description = "Kredit Usaha Rakyat - Government-backed micro loan for small businesses")
    KUR("Kredit Usaha Rakyat", "Government-backed micro loan for small businesses"),
    
    @Schema(description = "Kredit Tanpa Agunan - Unsecured personal loan")
    KTA("Kredit Tanpa Agunan", "Unsecured personal loan"),
    
    @Schema(description = "Kredit Pemilikan Rumah - Home ownership loan")
    KPR("Kredit Pemilikan Rumah", "Home ownership loan"),
    
    @Schema(description = "Kredit Pemilikan Kendaraan Bermotor - Vehicle ownership loan")
    KKB("Kredit Pemilikan Kendaraan Bermotor", "Vehicle ownership loan"),
    
    @Schema(description = "Kredit Investasi - Investment loan for business expansion")
    INVESTMENT("Kredit Investasi", "Investment loan for business expansion"),
    
    @Schema(description = "Kredit Modal Kerja - Working capital loan")
    WORKING_CAPITAL("Kredit Modal Kerja", "Working capital loan"),
    
    @Schema(description = "Kredit Konsumtif - Consumer loan for personal needs")
    CONSUMER("Kredit Konsumtif", "Consumer loan for personal needs"),
    
    @Schema(description = "Kredit Mikro - Micro loan for small businesses")
    MICRO("Kredit Mikro", "Micro loan for small businesses"),
    
    @Schema(description = "Kredit Ultra Mikro - Ultra micro loan for very small businesses")
    ULTRA_MICRO("Kredit Ultra Mikro", "Ultra micro loan for very small businesses"),
    
    @Schema(description = "Personal - Personal loan for personal needs")
    PERSONAL("Personal", "Personal loan for personal needs"),

    ALL("All","All");
    
    private final String displayName;
    private final String description;
    
    ProductType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}
