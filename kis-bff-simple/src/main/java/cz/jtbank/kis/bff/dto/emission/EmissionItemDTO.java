package cz.jtbank.kis.bff.dto.emission;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for Emission Item with inline editing support.
 * Represents a single emission entry within a financial investment.
 * Supports batch update operations with I/U/D (Insert/Update/Delete) action tracking.
 */
public class EmissionItemDTO {

    private Long id;
    private Long financialInvestmentId;

    // Action tracking for batch updates: 'I' = Insert, 'U' = Update, 'D' = Delete
    private String action;

    // Temporal validity
    private LocalDate validFrom;
    private LocalDate validTo;

    // Emission details
    private BigDecimal numberOfShares;
    private BigDecimal nominalValue;
    private BigDecimal registeredCapital;
    private BigDecimal volume;  // Auto-calculated: numberOfShares × nominalValue

    // Flags
    private Boolean investmentFlag;

    // UI state (not persisted to database)
    private Boolean isExpanded;  // For collapsible rows in UI
    private Boolean isDirty;     // Track unsaved changes in UI

    // Constructors
    public EmissionItemDTO() {
        this.action = "U";  // Default to Update
        this.isExpanded = false;
        this.isDirty = false;
        this.investmentFlag = false;
    }

    // Builder pattern
    public static class Builder {
        private EmissionItemDTO dto = new EmissionItemDTO();

        public Builder id(Long id) {
            dto.id = id;
            return this;
        }

        public Builder financialInvestmentId(Long financialInvestmentId) {
            dto.financialInvestmentId = financialInvestmentId;
            return this;
        }

        public Builder action(String action) {
            dto.action = action;
            return this;
        }

        public Builder validFrom(LocalDate validFrom) {
            dto.validFrom = validFrom;
            return this;
        }

        public Builder validTo(LocalDate validTo) {
            dto.validTo = validTo;
            return this;
        }

        public Builder numberOfShares(BigDecimal numberOfShares) {
            dto.numberOfShares = numberOfShares;
            return this;
        }

        public Builder nominalValue(BigDecimal nominalValue) {
            dto.nominalValue = nominalValue;
            return this;
        }

        public Builder registeredCapital(BigDecimal registeredCapital) {
            dto.registeredCapital = registeredCapital;
            return this;
        }

        public Builder volume(BigDecimal volume) {
            dto.volume = volume;
            return this;
        }

        public Builder investmentFlag(Boolean investmentFlag) {
            dto.investmentFlag = investmentFlag;
            return this;
        }

        public Builder isExpanded(Boolean isExpanded) {
            dto.isExpanded = isExpanded;
            return this;
        }

        public Builder isDirty(Boolean isDirty) {
            dto.isDirty = isDirty;
            return this;
        }

        public EmissionItemDTO build() {
            // Auto-calculate volume if not set
            if (dto.volume == null && dto.numberOfShares != null && dto.nominalValue != null) {
                dto.volume = dto.numberOfShares.multiply(dto.nominalValue);
            }
            return dto;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Calculate volume from shares and nominal value
     */
    public void calculateVolume() {
        if (this.numberOfShares != null && this.nominalValue != null) {
            this.volume = this.numberOfShares.multiply(this.nominalValue);
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFinancialInvestmentId() {
        return financialInvestmentId;
    }

    public void setFinancialInvestmentId(Long financialInvestmentId) {
        this.financialInvestmentId = financialInvestmentId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDate getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDate validTo) {
        this.validTo = validTo;
    }

    public BigDecimal getNumberOfShares() {
        return numberOfShares;
    }

    public void setNumberOfShares(BigDecimal numberOfShares) {
        this.numberOfShares = numberOfShares;
        calculateVolume();
    }

    public BigDecimal getNominalValue() {
        return nominalValue;
    }

    public void setNominalValue(BigDecimal nominalValue) {
        this.nominalValue = nominalValue;
        calculateVolume();
    }

    public BigDecimal getRegisteredCapital() {
        return registeredCapital;
    }

    public void setRegisteredCapital(BigDecimal registeredCapital) {
        this.registeredCapital = registeredCapital;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public Boolean getInvestmentFlag() {
        return investmentFlag;
    }

    public void setInvestmentFlag(Boolean investmentFlag) {
        this.investmentFlag = investmentFlag;
    }

    public Boolean getIsExpanded() {
        return isExpanded;
    }

    public void setIsExpanded(Boolean isExpanded) {
        this.isExpanded = isExpanded;
    }

    public Boolean getIsDirty() {
        return isDirty;
    }

    public void setIsDirty(Boolean isDirty) {
        this.isDirty = isDirty;
    }
}
