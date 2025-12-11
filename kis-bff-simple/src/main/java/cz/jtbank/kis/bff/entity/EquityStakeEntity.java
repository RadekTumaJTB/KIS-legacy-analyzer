package cz.jtbank.kis.bff.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entity representing an Equity Stake (Majetková Účast).
 * Maps to Oracle table: KP_DAT_MAJETKOVAUCAST
 *
 * An equity stake represents actual ownership (shares) that one company
 * holds in another company's financial investment emission.
 */
@Entity
@Table(name = "KP_DAT_MAJETKOVAUCAST")
public class EquityStakeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_majetkovaucast")
    @SequenceGenerator(name = "seq_majetkovaucast", sequenceName = "SQ_KP_DAT_MAJETKOVAUCAST", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "ID_PARENT")
    private Long parentId; // Previous version in history

    @Column(name = "ID_KTGUCETNISPOLECNOST")
    private Long accountingCompanyId; // Owner of the stake

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_KTGUCETNISPOLECNOST", insertable = false, updatable = false)
    private SpolecnostEntity accountingCompany;

    @Column(name = "ID_KTGFININVESTICEEMISE")
    private Long emissionId; // Which emission this stake belongs to

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_KTGFININVESTICEEMISE", insertable = false, updatable = false)
    private FinancialInvestmentEmissionEntity emission;

    @Column(name = "S_UCET", length = 12)
    private String accountNumber;

    @Column(name = "DT_PLATNOSTOD")
    private LocalDate validFrom;

    @Column(name = "DT_PLATNOSTDO")
    private LocalDate validTo;

    @Column(name = "ID_CISMUTYPTRANSAKCE")
    private Long transactionTypeId;

    @Column(name = "ID_CISMUZPUSOB")
    private Long methodId;

    @Column(name = "NL_POCETKUSU", precision = 17, scale = 4)
    private BigDecimal numberOfShares;

    // Transaction currency details
    @Column(name = "S_MENATRANSAKCE", length = 3)
    private String transactionCurrency;

    @Column(name = "ND_CENATRANSAKCEKUS", precision = 15, scale = 2)
    private BigDecimal pricePerShareTransaction;

    @Column(name = "ND_OBJEMTRANSAKCE", precision = 15, scale = 2)
    private BigDecimal totalTransactionAmount;

    // Exchange rate
    @Column(name = "ND_KURZ", precision = 10, scale = 6)
    private BigDecimal exchangeRate;

    // Accounting currency details
    @Column(name = "S_MENAUCETNI", length = 3)
    private String accountingCurrency;

    @Column(name = "ND_CENAUCETNIKUS", precision = 15, scale = 2)
    private BigDecimal pricePerShareAccounting;

    @Column(name = "ND_OBJEMUCETNI", precision = 15, scale = 2)
    private BigDecimal totalAccountingAmount;

    // Audit fields
    @Column(name = "DT_ZMENA")
    private LocalDate lastModified;

    @Column(name = "S_UZIVATEL", length = 20)
    private String modifiedByUser;

    // Additional fields
    @Column(name = "ID_KTGUCETNISPOLECNOSTKOUPENO")
    private Long purchasedFromCompanyId; // Company we bought this from

    @Column(name = "C_IGNOROVAT", length = 1)
    private String ignoreFlag; // '0' or '1'

    // Constructors
    public EquityStakeEntity() {
        this.lastModified = LocalDate.now();
        this.ignoreFlag = "0";
    }

    // Builder pattern
    public static class Builder {
        private EquityStakeEntity entity = new EquityStakeEntity();

        public Builder id(Long id) {
            entity.id = id;
            return this;
        }

        public Builder parentId(Long parentId) {
            entity.parentId = parentId;
            return this;
        }

        public Builder accountingCompanyId(Long accountingCompanyId) {
            entity.accountingCompanyId = accountingCompanyId;
            return this;
        }

        public Builder emissionId(Long emissionId) {
            entity.emissionId = emissionId;
            return this;
        }

        public Builder accountNumber(String accountNumber) {
            entity.accountNumber = accountNumber;
            return this;
        }

        public Builder validFrom(LocalDate validFrom) {
            entity.validFrom = validFrom;
            return this;
        }

        public Builder validTo(LocalDate validTo) {
            entity.validTo = validTo;
            return this;
        }

        public Builder transactionTypeId(Long transactionTypeId) {
            entity.transactionTypeId = transactionTypeId;
            return this;
        }

        public Builder methodId(Long methodId) {
            entity.methodId = methodId;
            return this;
        }

        public Builder numberOfShares(BigDecimal numberOfShares) {
            entity.numberOfShares = numberOfShares;
            return this;
        }

        public Builder transactionCurrency(String transactionCurrency) {
            entity.transactionCurrency = transactionCurrency;
            return this;
        }

        public Builder pricePerShareTransaction(BigDecimal pricePerShareTransaction) {
            entity.pricePerShareTransaction = pricePerShareTransaction;
            return this;
        }

        public Builder totalTransactionAmount(BigDecimal totalTransactionAmount) {
            entity.totalTransactionAmount = totalTransactionAmount;
            return this;
        }

        public Builder exchangeRate(BigDecimal exchangeRate) {
            entity.exchangeRate = exchangeRate;
            return this;
        }

        public Builder accountingCurrency(String accountingCurrency) {
            entity.accountingCurrency = accountingCurrency;
            return this;
        }

        public Builder pricePerShareAccounting(BigDecimal pricePerShareAccounting) {
            entity.pricePerShareAccounting = pricePerShareAccounting;
            return this;
        }

        public Builder totalAccountingAmount(BigDecimal totalAccountingAmount) {
            entity.totalAccountingAmount = totalAccountingAmount;
            return this;
        }

        public Builder purchasedFromCompanyId(Long purchasedFromCompanyId) {
            entity.purchasedFromCompanyId = purchasedFromCompanyId;
            return this;
        }

        public Builder ignoreFlag(String ignoreFlag) {
            entity.ignoreFlag = ignoreFlag;
            return this;
        }

        public Builder modifiedByUser(String user) {
            entity.modifiedByUser = user;
            return this;
        }

        public EquityStakeEntity build() {
            return entity;
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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getAccountingCompanyId() {
        return accountingCompanyId;
    }

    public void setAccountingCompanyId(Long accountingCompanyId) {
        this.accountingCompanyId = accountingCompanyId;
    }

    public SpolecnostEntity getAccountingCompany() {
        return accountingCompany;
    }

    public void setAccountingCompany(SpolecnostEntity accountingCompany) {
        this.accountingCompany = accountingCompany;
    }

    public Long getEmissionId() {
        return emissionId;
    }

    public void setEmissionId(Long emissionId) {
        this.emissionId = emissionId;
    }

    public FinancialInvestmentEmissionEntity getEmission() {
        return emission;
    }

    public void setEmission(FinancialInvestmentEmissionEntity emission) {
        this.emission = emission;
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

    public Long getMethodId() {
        return methodId;
    }

    public void setMethodId(Long methodId) {
        this.methodId = methodId;
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
    }

    public BigDecimal getTotalTransactionAmount() {
        return totalTransactionAmount;
    }

    public void setTotalTransactionAmount(BigDecimal totalTransactionAmount) {
        this.totalTransactionAmount = totalTransactionAmount;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
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

    public Long getPurchasedFromCompanyId() {
        return purchasedFromCompanyId;
    }

    public void setPurchasedFromCompanyId(Long purchasedFromCompanyId) {
        this.purchasedFromCompanyId = purchasedFromCompanyId;
    }

    public String getIgnoreFlag() {
        return ignoreFlag;
    }

    public void setIgnoreFlag(String ignoreFlag) {
        this.ignoreFlag = ignoreFlag;
    }
}
