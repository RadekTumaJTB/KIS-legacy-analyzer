package cz.jtbank.kis.bff.dto.budget;

import java.math.BigDecimal;

/**
 * Request DTO for updating a budget
 */
public class BudgetUpdateRequestDTO {
    private String name;
    private BigDecimal plannedAmount;
    private String description;

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPlannedAmount() {
        return plannedAmount;
    }

    public void setPlannedAmount(BigDecimal plannedAmount) {
        this.plannedAmount = plannedAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
