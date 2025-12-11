package cz.jtbank.kis.bff.dto.emission;

import java.time.LocalDate;

/**
 * DTO for Financial Investment (Finanční Investice).
 * Represents the top-level investment metadata.
 */
public class FinancialInvestmentDTO {

    private Long id;
    private Long companyId;
    private String companyName;
    private String currency;
    private String isinCode;
    private LocalDate lastModified;
    private String modifiedByUser;

    // Constructors
    public FinancialInvestmentDTO() {
    }

    // Builder pattern
    public static class Builder {
        private FinancialInvestmentDTO dto = new FinancialInvestmentDTO();

        public Builder id(Long id) {
            dto.id = id;
            return this;
        }

        public Builder companyId(Long companyId) {
            dto.companyId = companyId;
            return this;
        }

        public Builder companyName(String companyName) {
            dto.companyName = companyName;
            return this;
        }

        public Builder currency(String currency) {
            dto.currency = currency;
            return this;
        }

        public Builder isinCode(String isinCode) {
            dto.isinCode = isinCode;
            return this;
        }

        public Builder lastModified(LocalDate lastModified) {
            dto.lastModified = lastModified;
            return this;
        }

        public Builder modifiedByUser(String modifiedByUser) {
            dto.modifiedByUser = modifiedByUser;
            return this;
        }

        public FinancialInvestmentDTO build() {
            return dto;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getIsinCode() {
        return isinCode;
    }

    public void setIsinCode(String isinCode) {
        this.isinCode = isinCode;
    }

    public LocalDate getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
    }

    public String getModifiedByUser() {
        return modifiedByUser;
    }

    public void setModifiedByUser(String modifiedByUser) {
        this.modifiedByUser = modifiedByUser;
    }
}
