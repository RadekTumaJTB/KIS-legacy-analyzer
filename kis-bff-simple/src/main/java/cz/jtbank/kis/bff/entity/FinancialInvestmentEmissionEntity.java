package cz.jtbank.kis.bff.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entity representing an Emission of a Financial Investment (Emise Finanční Investice).
 * Maps to Oracle table: KP_KTG_FININVESTICEEMISE
 *
 * An emission represents a specific issuance of shares/bonds with time validity.
 * Multiple emissions can exist for one financial investment (historical data).
 */
@Entity
@Table(name = "KP_KTG_FININVESTICEEMISE")
public class FinancialInvestmentEmissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_fininvesticeemise")
    @SequenceGenerator(name = "seq_fininvesticeemise", sequenceName = "SQ_KP_KTG_FININVESTICEEMISE", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "ID_KTGFINANCNIINVESTICE")
    private Long financialInvestmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_KTGFINANCNIINVESTICE", insertable = false, updatable = false)
    private FinancialInvestmentEntity financialInvestment;

    @Column(name = "ID_PARENT")
    private Long parentId; // Previous version in history chain

    @Column(name = "DT_PLATNOSTOD")
    private LocalDate validFrom;

    @Column(name = "DT_PLATNOSTDO")
    private LocalDate validTo;

    @Column(name = "NL_POCETKUSU", precision = 10, scale = 0)
    private Long numberOfShares; // Number of shares/units

    @Column(name = "NL_NOMINAL", precision = 17, scale = 4)
    private BigDecimal nominalValue; // Nominal/face value

    @Column(name = "ND_ZAKLADNIJMENI", precision = 15, scale = 2)
    private BigDecimal registeredCapital; // Základní jmění

    @Column(name = "DT_ZMENA")
    private LocalDate lastModified;

    @Column(name = "S_UZIVATEL", length = 20)
    private String modifiedByUser;

    @Column(name = "C_INVESTICE", length = 1)
    private String investmentFlag; // 'F' or 'T'

    @Column(name = "C_NENULOVY", length = 1)
    private String nonZeroFlag; // '0' or '1'

    // Constructors
    public FinancialInvestmentEmissionEntity() {
        this.lastModified = LocalDate.now();
        this.investmentFlag = "F";
        this.nonZeroFlag = "1";
    }

    // Builder pattern
    public static class Builder {
        private FinancialInvestmentEmissionEntity entity = new FinancialInvestmentEmissionEntity();

        public Builder id(Long id) {
            entity.id = id;
            return this;
        }

        public Builder financialInvestmentId(Long financialInvestmentId) {
            entity.financialInvestmentId = financialInvestmentId;
            return this;
        }

        public Builder parentId(Long parentId) {
            entity.parentId = parentId;
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

        public Builder numberOfShares(Long numberOfShares) {
            entity.numberOfShares = numberOfShares;
            return this;
        }

        public Builder nominalValue(BigDecimal nominalValue) {
            entity.nominalValue = nominalValue;
            return this;
        }

        public Builder registeredCapital(BigDecimal registeredCapital) {
            entity.registeredCapital = registeredCapital;
            return this;
        }

        public Builder investmentFlag(String investmentFlag) {
            entity.investmentFlag = investmentFlag;
            return this;
        }

        public Builder nonZeroFlag(String nonZeroFlag) {
            entity.nonZeroFlag = nonZeroFlag;
            return this;
        }

        public Builder modifiedByUser(String user) {
            entity.modifiedByUser = user;
            return this;
        }

        public FinancialInvestmentEmissionEntity build() {
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

    public Long getFinancialInvestmentId() {
        return financialInvestmentId;
    }

    public void setFinancialInvestmentId(Long financialInvestmentId) {
        this.financialInvestmentId = financialInvestmentId;
    }

    public FinancialInvestmentEntity getFinancialInvestment() {
        return financialInvestment;
    }

    public void setFinancialInvestment(FinancialInvestmentEntity financialInvestment) {
        this.financialInvestment = financialInvestment;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
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

    public Long getNumberOfShares() {
        return numberOfShares;
    }

    public void setNumberOfShares(Long numberOfShares) {
        this.numberOfShares = numberOfShares;
    }

    public BigDecimal getNominalValue() {
        return nominalValue;
    }

    public void setNominalValue(BigDecimal nominalValue) {
        this.nominalValue = nominalValue;
    }

    public BigDecimal getRegisteredCapital() {
        return registeredCapital;
    }

    public void setRegisteredCapital(BigDecimal registeredCapital) {
        this.registeredCapital = registeredCapital;
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

    public String getInvestmentFlag() {
        return investmentFlag;
    }

    public void setInvestmentFlag(String investmentFlag) {
        this.investmentFlag = investmentFlag;
    }

    public String getNonZeroFlag() {
        return nonZeroFlag;
    }

    public void setNonZeroFlag(String nonZeroFlag) {
        this.nonZeroFlag = nonZeroFlag;
    }
}
