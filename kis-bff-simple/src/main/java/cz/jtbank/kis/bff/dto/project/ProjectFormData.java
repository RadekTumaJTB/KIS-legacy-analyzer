package cz.jtbank.kis.bff.dto.project;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for creating/updating projects via forms
 * Maps to parameters of Oracle procedure KAP_PROJEKT.p_KpProjekt
 */
public class ProjectFormData {
    private String oldNumber;  // p_cisloOld
    private String name;  // p_nazev
    private String description;  // p_popis
    private Long statusId;  // p_status
    private Long categoryId;  // p_kategorie
    private Long managementSegmentId;  // p_mngsegment
    private Long projectManagerId;  // p_pmanager
    private LocalDate valuationStartDate;  // p_valuationStartDate
    private LocalDate valuationEndDate;  // p_valuationEndDate
    private Long balanceTypeId;  // p_typBilance
    private Long budgetTypeId;  // p_typBudgetu
    private Boolean tracksBudget;  // p_sledujebudget (1/0)

    // Getters and Setters

    public String getOldNumber() {
        return oldNumber;
    }

    public void setOldNumber(String oldNumber) {
        this.oldNumber = oldNumber;
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

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getManagementSegmentId() {
        return managementSegmentId;
    }

    public void setManagementSegmentId(Long managementSegmentId) {
        this.managementSegmentId = managementSegmentId;
    }

    public Long getProjectManagerId() {
        return projectManagerId;
    }

    public void setProjectManagerId(Long projectManagerId) {
        this.projectManagerId = projectManagerId;
    }

    public LocalDate getValuationStartDate() {
        return valuationStartDate;
    }

    public void setValuationStartDate(LocalDate valuationStartDate) {
        this.valuationStartDate = valuationStartDate;
    }

    public LocalDate getValuationEndDate() {
        return valuationEndDate;
    }

    public void setValuationEndDate(LocalDate valuationEndDate) {
        this.valuationEndDate = valuationEndDate;
    }

    public Long getBalanceTypeId() {
        return balanceTypeId;
    }

    public void setBalanceTypeId(Long balanceTypeId) {
        this.balanceTypeId = balanceTypeId;
    }

    public Long getBudgetTypeId() {
        return budgetTypeId;
    }

    public void setBudgetTypeId(Long budgetTypeId) {
        this.budgetTypeId = budgetTypeId;
    }

    public Boolean getTracksBudget() {
        return tracksBudget;
    }

    public void setTracksBudget(Boolean tracksBudget) {
        this.tracksBudget = tracksBudget;
    }
}
