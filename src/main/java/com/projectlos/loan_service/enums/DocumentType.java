package com.projectlos.loan_service.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Identity document types")
public enum DocumentType {
    
    @Schema(description = "Kartu Tanda Penduduk (Indonesian ID Card)")
    KTP("KTP", "Kartu Tanda Penduduk"),
    
    @Schema(description = "Surat Izin Mengemudi (Driver's License)")
    SIM("SIM", "Surat Izin Mengemudi"),
    
    @Schema(description = "Passport")
    PASSPORT("Passport", "Passport"),
    
    @Schema(description = "Nomor Pokok Wajib Pajak (Tax ID)")
    NPWP("NPWP", "Nomor Pokok Wajib Pajak"),
    
    @Schema(description = "Kartu Keluarga (Family Card)")
    KK("KK", "Kartu Keluarga"),
    
    @Schema(description = "Birth Certificate")
    BIRTH_CERTIFICATE("Birth Certificate", "Birth Certificate"),
    
    @Schema(description = "Marriage Certificate")
    MARRIAGE_CERTIFICATE("Marriage Certificate", "Marriage Certificate"),
    
    @Schema(description = "Divorce Certificate")
    DIVORCE_CERTIFICATE("Divorce Certificate", "Divorce Certificate"),
    
    @Schema(description = "Other identity documents")
    OTHER("Other", "Other identity documents");
    
    private final String displayName;
    private final String description;
    
    DocumentType(String displayName, String description) {
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
