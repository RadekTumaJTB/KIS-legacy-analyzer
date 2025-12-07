package cz.jtbank.kis.bff.dto.budget;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Budget summary for list view
 */
public class BudgetSummaryDTO {

    private Long id;
    private String code;
    private String name;
    private String type; // REVENUE, EXPENSE, CAPEX
    private Integer year;
    private String status; // DRAFT, ACTIVE, LOCKED, ARCHIVED
    private BigDecimal plannedAmount;
    private BigDecimal actualAmount;
    private BigDecimal variance;
    private Double utilizationPercent;
    private String departmentName;
    private String ownerName;
    private LocalDate validFrom;
    private LocalDate validTo;

    public BudgetSummaryDTO() {
    }

    public BudgetSummaryDTO(Long id, String code, String name, String type, Integer year, String status,
                           BigDecimal plannedAmount, BigDecimal actualAmount, BigDecimal variance,
                           Double utilizationPercent, String departmentName, String ownerName,
                           LocalDate validFrom, LocalDate validTo) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.type = type;
        this.year = year;
        this.status = status;
        this.plannedAmount = plannedAmount;
        this.actualAmount = actualAmount;
        this.variance = variance;
        this.utilizationPercent = utilizationPercent;
        this.departmentName = departmentName;
        this.ownerName = ownerName;
        this.validFrom = validFrom;
        this.validTo = validTo;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
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

    // Builder class

    public static class Builder {
        private Long id;
        private String code;
        private String name;
        private String type;
        private Integer year;
        private String status;
        private BigDecimal plannedAmount;
        private BigDecimal actualAmount;
        private BigDecimal variance;
        private Double utilizationPercent;
        private String departmentName;
        private String ownerName;
        private LocalDate validFrom;
        private LocalDate validTo;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder year(Integer year) {
            this.year = year;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
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

        public Builder departmentName(String departmentName) {
            this.departmentName = departmentName;
            return this;
        }

        public Builder ownerName(String ownerName) {
            this.ownerName = ownerName;
            return this;
        }

        public Builder validFrom(LocalDate validFrom) {
            this.validFrom = validFrom;
            return this;
        }

        public Builder validTo(LocalDate validTo) {
            this.validTo = validTo;
            return this;
        }

        public BudgetSummaryDTO build() {
            return new BudgetSummaryDTO(id, code, name, type, year, status, plannedAmount, actualAmount,
                    variance, utilizationPercent, departmentName, ownerName, validFrom, validTo);
        }
    }
}
