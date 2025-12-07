package cz.jtbank.kis.bff.dto.budget;

import java.math.BigDecimal;

/**
 * Budget KPI metrics for dashboard
 */
public class BudgetKPIDTO {

    private BigDecimal totalPlanned;
    private BigDecimal totalActual;
    private BigDecimal totalVariance;
    private Double averageUtilization;
    private Integer totalBudgets;
    private Integer activeBudgets;
    private Integer overBudgetCount;
    private Integer underBudgetCount;
    private BigDecimal largestVariance;
    private String largestVarianceBudgetName;

    public BudgetKPIDTO() {
    }

    public BudgetKPIDTO(BigDecimal totalPlanned, BigDecimal totalActual, BigDecimal totalVariance,
                       Double averageUtilization, Integer totalBudgets, Integer activeBudgets,
                       Integer overBudgetCount, Integer underBudgetCount, BigDecimal largestVariance,
                       String largestVarianceBudgetName) {
        this.totalPlanned = totalPlanned;
        this.totalActual = totalActual;
        this.totalVariance = totalVariance;
        this.averageUtilization = averageUtilization;
        this.totalBudgets = totalBudgets;
        this.activeBudgets = activeBudgets;
        this.overBudgetCount = overBudgetCount;
        this.underBudgetCount = underBudgetCount;
        this.largestVariance = largestVariance;
        this.largestVarianceBudgetName = largestVarianceBudgetName;
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters and Setters

    public BigDecimal getTotalPlanned() {
        return totalPlanned;
    }

    public void setTotalPlanned(BigDecimal totalPlanned) {
        this.totalPlanned = totalPlanned;
    }

    public BigDecimal getTotalActual() {
        return totalActual;
    }

    public void setTotalActual(BigDecimal totalActual) {
        this.totalActual = totalActual;
    }

    public BigDecimal getTotalVariance() {
        return totalVariance;
    }

    public void setTotalVariance(BigDecimal totalVariance) {
        this.totalVariance = totalVariance;
    }

    public Double getAverageUtilization() {
        return averageUtilization;
    }

    public void setAverageUtilization(Double averageUtilization) {
        this.averageUtilization = averageUtilization;
    }

    public Integer getTotalBudgets() {
        return totalBudgets;
    }

    public void setTotalBudgets(Integer totalBudgets) {
        this.totalBudgets = totalBudgets;
    }

    public Integer getActiveBudgets() {
        return activeBudgets;
    }

    public void setActiveBudgets(Integer activeBudgets) {
        this.activeBudgets = activeBudgets;
    }

    public Integer getOverBudgetCount() {
        return overBudgetCount;
    }

    public void setOverBudgetCount(Integer overBudgetCount) {
        this.overBudgetCount = overBudgetCount;
    }

    public Integer getUnderBudgetCount() {
        return underBudgetCount;
    }

    public void setUnderBudgetCount(Integer underBudgetCount) {
        this.underBudgetCount = underBudgetCount;
    }

    public BigDecimal getLargestVariance() {
        return largestVariance;
    }

    public void setLargestVariance(BigDecimal largestVariance) {
        this.largestVariance = largestVariance;
    }

    public String getLargestVarianceBudgetName() {
        return largestVarianceBudgetName;
    }

    public void setLargestVarianceBudgetName(String largestVarianceBudgetName) {
        this.largestVarianceBudgetName = largestVarianceBudgetName;
    }

    // Builder class

    public static class Builder {
        private BigDecimal totalPlanned;
        private BigDecimal totalActual;
        private BigDecimal totalVariance;
        private Double averageUtilization;
        private Integer totalBudgets;
        private Integer activeBudgets;
        private Integer overBudgetCount;
        private Integer underBudgetCount;
        private BigDecimal largestVariance;
        private String largestVarianceBudgetName;

        public Builder totalPlanned(BigDecimal totalPlanned) {
            this.totalPlanned = totalPlanned;
            return this;
        }

        public Builder totalActual(BigDecimal totalActual) {
            this.totalActual = totalActual;
            return this;
        }

        public Builder totalVariance(BigDecimal totalVariance) {
            this.totalVariance = totalVariance;
            return this;
        }

        public Builder averageUtilization(Double averageUtilization) {
            this.averageUtilization = averageUtilization;
            return this;
        }

        public Builder totalBudgets(Integer totalBudgets) {
            this.totalBudgets = totalBudgets;
            return this;
        }

        public Builder activeBudgets(Integer activeBudgets) {
            this.activeBudgets = activeBudgets;
            return this;
        }

        public Builder overBudgetCount(Integer overBudgetCount) {
            this.overBudgetCount = overBudgetCount;
            return this;
        }

        public Builder underBudgetCount(Integer underBudgetCount) {
            this.underBudgetCount = underBudgetCount;
            return this;
        }

        public Builder largestVariance(BigDecimal largestVariance) {
            this.largestVariance = largestVariance;
            return this;
        }

        public Builder largestVarianceBudgetName(String largestVarianceBudgetName) {
            this.largestVarianceBudgetName = largestVarianceBudgetName;
            return this;
        }

        public BudgetKPIDTO build() {
            return new BudgetKPIDTO(totalPlanned, totalActual, totalVariance, averageUtilization,
                    totalBudgets, activeBudgets, overBudgetCount, underBudgetCount,
                    largestVariance, largestVarianceBudgetName);
        }
    }
}
