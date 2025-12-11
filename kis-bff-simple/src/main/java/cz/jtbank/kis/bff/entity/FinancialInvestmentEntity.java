package cz.jtbank.kis.bff.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entity representing a Financial Investment (Finanční Investice).
 * Maps to Oracle table: KP_KTG_FINANCNIINVESTICE
 *
 * A financial investment represents a security (stock, bond) that the company
 * invests in. It contains basic information like ISIN code and currency.
 */
@Entity
@Table(name = "KP_KTG_FINANCNIINVESTICE")
public class FinancialInvestmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_fininvestice")
    @SequenceGenerator(name = "seq_fininvestice", sequenceName = "SQ_KP_KTG_FININVESTICE", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "ID_KTGSPOLECNOST")
    private Long companyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_KTGSPOLECNOST", insertable = false, updatable = false)
    private SpolecnostEntity company;

    @Column(name = "S_MENA", length = 3)
    private String currencyCode;

    @Column(name = "DT_ZMENA")
    private LocalDate lastModified;

    @Column(name = "S_UZIVATEL", length = 20)
    private String modifiedByUser;

    @Column(name = "S_ISIN", length = 13)
    private String isinCode; // International Securities Identification Number

    // Constructors
    public FinancialInvestmentEntity() {
        this.lastModified = LocalDate.now();
    }

    // Builder pattern (manual - no Lombok)
    public static class Builder {
        private FinancialInvestmentEntity entity = new FinancialInvestmentEntity();

        public Builder id(Long id) {
            entity.id = id;
            return this;
        }

        public Builder companyId(Long companyId) {
            entity.companyId = companyId;
            return this;
        }

        public Builder currencyCode(String currencyCode) {
            entity.currencyCode = currencyCode;
            return this;
        }

        public Builder isinCode(String isinCode) {
            entity.isinCode = isinCode;
            return this;
        }

        public Builder modifiedByUser(String user) {
            entity.modifiedByUser = user;
            return this;
        }

        public FinancialInvestmentEntity build() {
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

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public SpolecnostEntity getCompany() {
        return company;
    }

    public void setCompany(SpolecnostEntity company) {
        this.company = company;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
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

    public String getIsinCode() {
        return isinCode;
    }

    public void setIsinCode(String isinCode) {
        this.isinCode = isinCode;
    }
}
