package cz.jtbank.kis.bff.dto.asset;

import java.math.BigDecimal;

/**
 * DTO for Asset Overview with Calculations.
 * Provides snapshot view of equity stakes with ownership percentages and values.
 * Used in AssetOverviewPage for analytics and reporting.
 */
public class AssetOverviewDTO {

    private Long emissionId;
    private String companyName;
    private String isinCode;

    // Share calculations
    private BigDecimal totalEmissionShares;
    private BigDecimal sharesOwned;
    private BigDecimal ownershipPercentage;  // Calculated: (sharesOwned / totalEmissionShares) × 100

    // Value calculations
    private BigDecimal marketValue;
    private BigDecimal bookValue;
    private BigDecimal unrealizedGainLoss;  // Calculated: marketValue - bookValue

    // Additional context
    private String currency;
    private String accountingCompanyName;

    // Constructors
    public AssetOverviewDTO() {
    }

    // Builder pattern
    public static class Builder {
        private AssetOverviewDTO dto = new AssetOverviewDTO();

        public Builder emissionId(Long emissionId) {
            dto.emissionId = emissionId;
            return this;
        }

        public Builder companyName(String companyName) {
            dto.companyName = companyName;
            return this;
        }

        public Builder isinCode(String isinCode) {
            dto.isinCode = isinCode;
            return this;
        }

        public Builder totalEmissionShares(BigDecimal totalEmissionShares) {
            dto.totalEmissionShares = totalEmissionShares;
            return this;
        }

        public Builder sharesOwned(BigDecimal sharesOwned) {
            dto.sharesOwned = sharesOwned;
            return this;
        }

        public Builder ownershipPercentage(BigDecimal ownershipPercentage) {
            dto.ownershipPercentage = ownershipPercentage;
            return this;
        }

        public Builder marketValue(BigDecimal marketValue) {
            dto.marketValue = marketValue;
            return this;
        }

        public Builder bookValue(BigDecimal bookValue) {
            dto.bookValue = bookValue;
            return this;
        }

        public Builder unrealizedGainLoss(BigDecimal unrealizedGainLoss) {
            dto.unrealizedGainLoss = unrealizedGainLoss;
            return this;
        }

        public Builder currency(String currency) {
            dto.currency = currency;
            return this;
        }

        public Builder accountingCompanyName(String accountingCompanyName) {
            dto.accountingCompanyName = accountingCompanyName;
            return this;
        }

        public AssetOverviewDTO build() {
            // Auto-calculate ownership percentage if not set
            if (dto.ownershipPercentage == null && dto.sharesOwned != null && dto.totalEmissionShares != null
                    && dto.totalEmissionShares.compareTo(BigDecimal.ZERO) > 0) {
                dto.ownershipPercentage = dto.sharesOwned
                        .divide(dto.totalEmissionShares, 4, BigDecimal.ROUND_HALF_UP)
                        .multiply(BigDecimal.valueOf(100));
            }

            // Auto-calculate unrealized gain/loss if not set
            if (dto.unrealizedGainLoss == null && dto.marketValue != null && dto.bookValue != null) {
                dto.unrealizedGainLoss = dto.marketValue.subtract(dto.bookValue);
            }

            return dto;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Calculate ownership percentage
     */
    public void calculateOwnershipPercentage() {
        if (this.sharesOwned != null && this.totalEmissionShares != null
                && this.totalEmissionShares.compareTo(BigDecimal.ZERO) > 0) {
            this.ownershipPercentage = this.sharesOwned
                    .divide(this.totalEmissionShares, 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }
    }

    /**
     * Calculate unrealized gain/loss
     */
    public void calculateUnrealizedGainLoss() {
        if (this.marketValue != null && this.bookValue != null) {
            this.unrealizedGainLoss = this.marketValue.subtract(this.bookValue);
        }
    }

    // Getters and Setters
    public Long getEmissionId() {
        return emissionId;
    }

    public void setEmissionId(Long emissionId) {
        this.emissionId = emissionId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getIsinCode() {
        return isinCode;
    }

    public void setIsinCode(String isinCode) {
        this.isinCode = isinCode;
    }

    public BigDecimal getTotalEmissionShares() {
        return totalEmissionShares;
    }

    public void setTotalEmissionShares(BigDecimal totalEmissionShares) {
        this.totalEmissionShares = totalEmissionShares;
        calculateOwnershipPercentage();
    }

    public BigDecimal getSharesOwned() {
        return sharesOwned;
    }

    public void setSharesOwned(BigDecimal sharesOwned) {
        this.sharesOwned = sharesOwned;
        calculateOwnershipPercentage();
    }

    public BigDecimal getOwnershipPercentage() {
        return ownershipPercentage;
    }

    public void setOwnershipPercentage(BigDecimal ownershipPercentage) {
        this.ownershipPercentage = ownershipPercentage;
    }

    public BigDecimal getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(BigDecimal marketValue) {
        this.marketValue = marketValue;
        calculateUnrealizedGainLoss();
    }

    public BigDecimal getBookValue() {
        return bookValue;
    }

    public void setBookValue(BigDecimal bookValue) {
        this.bookValue = bookValue;
        calculateUnrealizedGainLoss();
    }

    public BigDecimal getUnrealizedGainLoss() {
        return unrealizedGainLoss;
    }

    public void setUnrealizedGainLoss(BigDecimal unrealizedGainLoss) {
        this.unrealizedGainLoss = unrealizedGainLoss;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAccountingCompanyName() {
        return accountingCompanyName;
    }

    public void setAccountingCompanyName(String accountingCompanyName) {
        this.accountingCompanyName = accountingCompanyName;
    }
}
