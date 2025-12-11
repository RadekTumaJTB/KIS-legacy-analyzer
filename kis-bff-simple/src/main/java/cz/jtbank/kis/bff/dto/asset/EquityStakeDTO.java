package cz.jtbank.kis.bff.dto.asset;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for Equity Stake (Majetková Účast).
 * Represents ownership (shares) that one company holds in another company's emission.
 * Supports dual currency handling (transaction + accounting).
 */
public class EquityStakeDTO {

    private Long id;
    private Long emissionId;
    private Long accountingCompanyId;
    private String accountingCompanyName;
    private String accountNumber;

    // Temporal validity
    private LocalDate validFrom;
    private LocalDate validTo;

    // Transaction type and method
    private Long transactionTypeId;
    private String transactionTypeName;  // e.g., "Nákup", "Prodej", "Transfer"
    private Long methodId;
    private String methodName;  // e.g., "Přímá účast", "Nepřímá účast"

    private BigDecimal numberOfShares;

    // Transaction currency details
    private String transactionCurrency;
    private BigDecimal pricePerShareTransaction;
    private BigDecimal totalTransactionAmount;

    // Exchange rate
    private BigDecimal exchangeRate;

    // Accounting currency details
    private String accountingCurrency;
    private BigDecimal pricePerShareAccounting;
    private BigDecimal totalAccountingAmount;

    // Additional fields
    private Long purchasedFromCompanyId;
    private String purchasedFromCompanyName;
    private Boolean ignoreFlag;

    // Audit fields
    private LocalDate lastModified;
    private String modifiedByUser;

    // Constructors
    public EquityStakeDTO() {
        this.ignoreFlag = false;
    }

    // Builder pattern
    public static class Builder {
        private EquityStakeDTO dto = new EquityStakeDTO();

        public Builder id(Long id) {
            dto.id = id;
            return this;
        }

        public Builder emissionId(Long emissionId) {
            dto.emissionId = emissionId;
            return this;
        }

        public Builder accountingCompanyId(Long accountingCompanyId) {
            dto.accountingCompanyId = accountingCompanyId;
            return this;
        }

        public Builder accountingCompanyName(String accountingCompanyName) {
            dto.accountingCompanyName = accountingCompanyName;
            return this;
        }

        public Builder accountNumber(String accountNumber) {
            dto.accountNumber = accountNumber;
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

        public Builder transactionTypeId(Long transactionTypeId) {
            dto.transactionTypeId = transactionTypeId;
            return this;
        }

        public Builder transactionTypeName(String transactionTypeName) {
            dto.transactionTypeName = transactionTypeName;
            return this;
        }

        public Builder methodId(Long methodId) {
            dto.methodId = methodId;
            return this;
        }

        public Builder methodName(String methodName) {
            dto.methodName = methodName;
            return this;
        }

        public Builder numberOfShares(BigDecimal numberOfShares) {
            dto.numberOfShares = numberOfShares;
            return this;
        }

        public Builder transactionCurrency(String transactionCurrency) {
            dto.transactionCurrency = transactionCurrency;
            return this;
        }

        public Builder pricePerShareTransaction(BigDecimal pricePerShareTransaction) {
            dto.pricePerShareTransaction = pricePerShareTransaction;
            return this;
        }

        public Builder totalTransactionAmount(BigDecimal totalTransactionAmount) {
            dto.totalTransactionAmount = totalTransactionAmount;
            return this;
        }

        public Builder exchangeRate(BigDecimal exchangeRate) {
            dto.exchangeRate = exchangeRate;
            return this;
        }

        public Builder accountingCurrency(String accountingCurrency) {
            dto.accountingCurrency = accountingCurrency;
            return this;
        }

        public Builder pricePerShareAccounting(BigDecimal pricePerShareAccounting) {
            dto.pricePerShareAccounting = pricePerShareAccounting;
            return this;
        }

        public Builder totalAccountingAmount(BigDecimal totalAccountingAmount) {
            dto.totalAccountingAmount = totalAccountingAmount;
            return this;
        }

        public Builder purchasedFromCompanyId(Long purchasedFromCompanyId) {
            dto.purchasedFromCompanyId = purchasedFromCompanyId;
            return this;
        }

        public Builder purchasedFromCompanyName(String purchasedFromCompanyName) {
            dto.purchasedFromCompanyName = purchasedFromCompanyName;
            return this;
        }

        public Builder ignoreFlag(Boolean ignoreFlag) {
            dto.ignoreFlag = ignoreFlag;
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

        public EquityStakeDTO build() {
            return dto;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Calculate accounting amounts from transaction amounts using exchange rate
     */
    public void calculateAccountingAmounts() {
        if (this.pricePerShareTransaction != null && this.exchangeRate != null) {
            this.pricePerShareAccounting = this.pricePerShareTransaction.multiply(this.exchangeRate);
        }
        if (this.totalTransactionAmount != null && this.exchangeRate != null) {
            this.totalAccountingAmount = this.totalTransactionAmount.multiply(this.exchangeRate);
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmissionId() {
        return emissionId;
    }

    public void setEmissionId(Long emissionId) {
        this.emissionId = emissionId;
    }

    public Long getAccountingCompanyId() {
        return accountingCompanyId;
    }

    public void setAccountingCompanyId(Long accountingCompanyId) {
        this.accountingCompanyId = accountingCompanyId;
    }

    public String getAccountingCompanyName() {
        return accountingCompanyName;
    }

    public void setAccountingCompanyName(String accountingCompanyName) {
        this.accountingCompanyName = accountingCompanyName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
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

    public Long getTransactionTypeId() {
        return transactionTypeId;
    }

    public void setTransactionTypeId(Long transactionTypeId) {
        this.transactionTypeId = transactionTypeId;
    }

    public String getTransactionTypeName() {
        return transactionTypeName;
    }

    public void setTransactionTypeName(String transactionTypeName) {
        this.transactionTypeName = transactionTypeName;
    }

    public Long getMethodId() {
        return methodId;
    }

    public void setMethodId(Long methodId) {
        this.methodId = methodId;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public BigDecimal getNumberOfShares() {
        return numberOfShares;
    }

    public void setNumberOfShares(BigDecimal numberOfShares) {
        this.numberOfShares = numberOfShares;
    }

    public String getTransactionCurrency() {
        return transactionCurrency;
    }

    public void setTransactionCurrency(String transactionCurrency) {
        this.transactionCurrency = transactionCurrency;
    }

    public BigDecimal getPricePerShareTransaction() {
        return pricePerShareTransaction;
    }

    public void setPricePerShareTransaction(BigDecimal pricePerShareTransaction) {
        this.pricePerShareTransaction = pricePerShareTransaction;
        calculateAccountingAmounts();
    }

    public BigDecimal getTotalTransactionAmount() {
        return totalTransactionAmount;
    }

    public void setTotalTransactionAmount(BigDecimal totalTransactionAmount) {
        this.totalTransactionAmount = totalTransactionAmount;
        calculateAccountingAmounts();
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
        calculateAccountingAmounts();
    }

    public String getAccountingCurrency() {
        return accountingCurrency;
    }

    public void setAccountingCurrency(String accountingCurrency) {
        this.accountingCurrency = accountingCurrency;
    }

    public BigDecimal getPricePerShareAccounting() {
        return pricePerShareAccounting;
    }

    public void setPricePerShareAccounting(BigDecimal pricePerShareAccounting) {
        this.pricePerShareAccounting = pricePerShareAccounting;
    }

    public BigDecimal getTotalAccountingAmount() {
        return totalAccountingAmount;
    }

    public void setTotalAccountingAmount(BigDecimal totalAccountingAmount) {
        this.totalAccountingAmount = totalAccountingAmount;
    }

    public Long getPurchasedFromCompanyId() {
        return purchasedFromCompanyId;
    }

    public void setPurchasedFromCompanyId(Long purchasedFromCompanyId) {
        this.purchasedFromCompanyId = purchasedFromCompanyId;
    }

    public String getPurchasedFromCompanyName() {
        return purchasedFromCompanyName;
    }

    public void setPurchasedFromCompanyName(String purchasedFromCompanyName) {
        this.purchasedFromCompanyName = purchasedFromCompanyName;
    }

    public Boolean getIgnoreFlag() {
        return ignoreFlag;
    }

    public void setIgnoreFlag(Boolean ignoreFlag) {
        this.ignoreFlag = ignoreFlag;
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
