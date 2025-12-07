package cz.jtbank.kis.bff.dto.budget;

import cz.jtbank.kis.bff.dto.document.UserSummaryDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Complete budget detail with line items
 */
public class BudgetDetailDTO {

    private Long id;
    private String code;
    private String name;
    private String description;
    private String type; // REVENUE, EXPENSE, CAPEX
    private Integer year;
    private String status; // DRAFT, ACTIVE, LOCKED, ARCHIVED
    private BigDecimal totalPlanned;
    private BigDecimal totalActual;
    private BigDecimal totalVariance;
    private Double utilizationPercent;
    private String departmentName;
    private UserSummaryDTO owner;
    private LocalDate validFrom;
    private LocalDate validTo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<BudgetLineItemDTO> lineItems;
    private String notes;

    public BudgetDetailDTO() {
    }

    public BudgetDetailDTO(Long id, String code, String name, String description, String type,
                          Integer year, String status, BigDecimal totalPlanned, BigDecimal totalActual,
                          BigDecimal totalVariance, Double utilizationPercent, String departmentName,
                          UserSummaryDTO owner, LocalDate validFrom, LocalDate validTo,
                          LocalDateTime createdAt, LocalDateTime updatedAt,
                          List<BudgetLineItemDTO> lineItems, String notes) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.type = type;
        this.year = year;
        this.status = status;
        this.totalPlanned = totalPlanned;
        this.totalActual = totalActual;
        this.totalVariance = totalVariance;
        this.utilizationPercent = utilizationPercent;
        this.departmentName = departmentName;
        this.owner = owner;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lineItems = lineItems;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public UserSummaryDTO getOwner() {
        return owner;
    }

    public void setOwner(UserSummaryDTO owner) {
        this.owner = owner;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<BudgetLineItemDTO> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<BudgetLineItemDTO> lineItems) {
        this.lineItems = lineItems;
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
        private String code;
        private String name;
        private String description;
        private String type;
        private Integer year;
        private String status;
        private BigDecimal totalPlanned;
        private BigDecimal totalActual;
        private BigDecimal totalVariance;
        private Double utilizationPercent;
        private String departmentName;
        private UserSummaryDTO owner;
        private LocalDate validFrom;
        private LocalDate validTo;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private List<BudgetLineItemDTO> lineItems;
        private String notes;

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

        public Builder description(String description) {
            this.description = description;
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

        public Builder utilizationPercent(Double utilizationPercent) {
            this.utilizationPercent = utilizationPercent;
            return this;
        }

        public Builder departmentName(String departmentName) {
            this.departmentName = departmentName;
            return this;
        }

        public Builder owner(UserSummaryDTO owner) {
            this.owner = owner;
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

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder lineItems(List<BudgetLineItemDTO> lineItems) {
            this.lineItems = lineItems;
            return this;
        }

        public Builder notes(String notes) {
            this.notes = notes;
            return this;
        }

        public BudgetDetailDTO build() {
            return new BudgetDetailDTO(id, code, name, description, type, year, status,
                    totalPlanned, totalActual, totalVariance, utilizationPercent,
                    departmentName, owner, validFrom, validTo, createdAt, updatedAt,
                    lineItems, notes);
        }
    }
}
