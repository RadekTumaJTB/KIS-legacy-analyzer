package cz.jtbank.kis.bff.dto.budget;

import java.math.BigDecimal;

/**
 * Request DTO for updating a budget
 * UI sends total amount, backend distributes it across months
 * and calls updateBudgetPolozka() for each month (like original)
 */
public class BudgetUpdateRequestDTO {
    private BigDecimal plannedAmount;  // Celková plánovaná částka (UI posílá total)
    private String description;
    private String notes;

    // Getters and Setters

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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
