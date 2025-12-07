package cz.jtbank.kis.bff.dto.budget;

import java.math.BigDecimal;

/**
 * Budget line item (monthly breakdown)
 */
public class BudgetLineItemDTO {

    private Long id;
    private Integer month; // 1-12
    private String monthName; // "Leden", "Únor", etc.
    private BigDecimal plannedAmount;
    private BigDecimal actualAmount;
    private BigDecimal variance;
    private Double utilizationPercent;
    private String status; // ON_TRACK, OVER_BUDGET, UNDER_BUDGET, WARNING
    private String notes;

    public BudgetLineItemDTO() {
    }

    public BudgetLineItemDTO(Long id, Integer month, String monthName, BigDecimal plannedAmount,
                            BigDecimal actualAmount, BigDecimal variance, Double utilizationPercent,
                            String status, String notes) {
        this.id = id;
        this.month = month;
        this.monthName = monthName;
        this.plannedAmount = plannedAmount;
        this.actualAmount = actualAmount;
        this.variance = variance;
        this.utilizationPercent = utilizationPercent;
        this.status = status;
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

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public BigDecimal getPlannedAmount() {
        return plannedAmount;
    }

    public void setPlannedAmount(BigDecimal plannedAmount) {
        this.plannedAmount = plannedAmount;
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public BigDecimal getVariance() {
        return variance;
    }

    public void setVariance(BigDecimal variance) {
        this.variance = variance;
    }

    public Double getUtilizationPercent() {
        return utilizationPercent;
    }

    public void setUtilizationPercent(Double utilizationPercent) {
        this.utilizationPercent = utilizationPercent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        private Integer month;
        private String monthName;
        private BigDecimal plannedAmount;
        private BigDecimal actualAmount;
        private BigDecimal variance;
        private Double utilizationPercent;
        private String status;
        private String notes;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder month(Integer month) {
            this.month = month;
            return this;
        }

        public Builder monthName(String monthName) {
            this.monthName = monthName;
            return this;
        }

        public Builder plannedAmount(BigDecimal plannedAmount) {
            this.plannedAmount = plannedAmount;
            return this;
        }

        public Builder actualAmount(BigDecimal actualAmount) {
            this.actualAmount = actualAmount;
            return this;
        }

        public Builder variance(BigDecimal variance) {
            this.variance = variance;
            return this;
        }

        public Builder utilizationPercent(Double utilizationPercent) {
            this.utilizationPercent = utilizationPercent;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder notes(String notes) {
            this.notes = notes;
            return this;
        }

        public BudgetLineItemDTO build() {
            return new BudgetLineItemDTO(id, month, monthName, plannedAmount, actualAmount,
                    variance, utilizationPercent, status, notes);
        }
    }
}
