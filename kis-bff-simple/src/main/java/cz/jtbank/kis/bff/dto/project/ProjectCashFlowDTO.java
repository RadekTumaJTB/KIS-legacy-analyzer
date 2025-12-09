package cz.jtbank.kis.bff.dto.project;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Project cash flow entry DTO
 */
public class ProjectCashFlowDTO {

    private Long id;
    private Long idProject;
    private String cashFlowTypeName;
    private LocalDate date;
    private BigDecimal amount;
    private String currencyCode;
    private String inOutTypeName;
    private String positionTypeName;
    private String notes;

    public ProjectCashFlowDTO() {
    }

    public ProjectCashFlowDTO(Long id, Long idProject, String cashFlowTypeName, LocalDate date,
                            BigDecimal amount, String currencyCode, String inOutTypeName,
                            String positionTypeName, String notes) {
        this.id = id;
        this.idProject = idProject;
        this.cashFlowTypeName = cashFlowTypeName;
        this.date = date;
        this.amount = amount;
        this.currencyCode = currencyCode;
        this.inOutTypeName = inOutTypeName;
        this.positionTypeName = positionTypeName;
        this.notes = notes;
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

    public Long getIdProject() {
        return idProject;
    }

    public void setIdProject(Long idProject) {
        this.idProject = idProject;
    }

    public String getCashFlowTypeName() {
        return cashFlowTypeName;
    }

    public void setCashFlowTypeName(String cashFlowTypeName) {
        this.cashFlowTypeName = cashFlowTypeName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getInOutTypeName() {
        return inOutTypeName;
    }

    public void setInOutTypeName(String inOutTypeName) {
        this.inOutTypeName = inOutTypeName;
    }

    public String getPositionTypeName() {
        return positionTypeName;
    }

    public void setPositionTypeName(String positionTypeName) {
        this.positionTypeName = positionTypeName;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // Builder class

    public static class Builder {
        private Long id;
        private Long idProject;
        private String cashFlowTypeName;
        private LocalDate date;
        private BigDecimal amount;
        private String currencyCode;
        private String inOutTypeName;
        private String positionTypeName;
        private String notes;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder idProject(Long idProject) {
            this.idProject = idProject;
            return this;
        }

        public Builder cashFlowTypeName(String cashFlowTypeName) {
            this.cashFlowTypeName = cashFlowTypeName;
            return this;
        }

        public Builder date(LocalDate date) {
            this.date = date;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder currencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
            return this;
        }

        public Builder inOutTypeName(String inOutTypeName) {
            this.inOutTypeName = inOutTypeName;
            return this;
        }

        public Builder positionTypeName(String positionTypeName) {
            this.positionTypeName = positionTypeName;
            return this;
        }

        public Builder notes(String notes) {
            this.notes = notes;
            return this;
        }

        public ProjectCashFlowDTO build() {
            return new ProjectCashFlowDTO(id, idProject, cashFlowTypeName, date, amount,
                    currencyCode, inOutTypeName, positionTypeName, notes);
        }
    }
}
