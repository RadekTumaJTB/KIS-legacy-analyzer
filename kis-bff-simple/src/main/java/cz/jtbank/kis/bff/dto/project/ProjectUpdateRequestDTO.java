package cz.jtbank.kis.bff.dto.project;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Request DTO for updating an existing project
 */
public class ProjectUpdateRequestDTO {

    private String name;
    private String projectNumber;
    private Long idStatus;
    private Long idProjectManager;
    private Long idManagementSegment;
    private Long idCurrency;
    private LocalDate valuationStartDate;
    private Long idFrequency;
    private String description;
    private BigDecimal approvalLevel1Amount;
    private BigDecimal approvalLevel2Amount;
    private BigDecimal approvalLevel3Amount;
    private String budgetTrackingFlag;
    private BigDecimal budgetIncreaseAmountPM;
    private BigDecimal budgetIncreaseAmountTop;
    private Long idBalanceType;
    private Long idBudgetType;
    private Long idCategory;

    public ProjectUpdateRequestDTO() {
    }

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(String projectNumber) {
        this.projectNumber = projectNumber;
    }

    public Long getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(Long idStatus) {
        this.idStatus = idStatus;
    }

    public Long getIdProjectManager() {
        return idProjectManager;
    }

    public void setIdProjectManager(Long idProjectManager) {
        this.idProjectManager = idProjectManager;
    }

    public Long getIdManagementSegment() {
        return idManagementSegment;
    }

    public void setIdManagementSegment(Long idManagementSegment) {
        this.idManagementSegment = idManagementSegment;
    }

    public Long getIdCurrency() {
        return idCurrency;
    }

    public void setIdCurrency(Long idCurrency) {
        this.idCurrency = idCurrency;
    }

    public LocalDate getValuationStartDate() {
        return valuationStartDate;
    }

    public void setValuationStartDate(LocalDate valuationStartDate) {
        this.valuationStartDate = valuationStartDate;
    }

    public Long getIdFrequency() {
        return idFrequency;
    }

    public void setIdFrequency(Long idFrequency) {
        this.idFrequency = idFrequency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getApprovalLevel1Amount() {
        return approvalLevel1Amount;
    }

    public void setApprovalLevel1Amount(BigDecimal approvalLevel1Amount) {
        this.approvalLevel1Amount = approvalLevel1Amount;
    }

    public BigDecimal getApprovalLevel2Amount() {
        return approvalLevel2Amount;
    }

    public void setApprovalLevel2Amount(BigDecimal approvalLevel2Amount) {
        this.approvalLevel2Amount = approvalLevel2Amount;
    }

    public BigDecimal getApprovalLevel3Amount() {
        return approvalLevel3Amount;
    }

    public void setApprovalLevel3Amount(BigDecimal approvalLevel3Amount) {
        this.approvalLevel3Amount = approvalLevel3Amount;
    }

    public String getBudgetTrackingFlag() {
        return budgetTrackingFlag;
    }

    public void setBudgetTrackingFlag(String budgetTrackingFlag) {
        this.budgetTrackingFlag = budgetTrackingFlag;
    }

    public BigDecimal getBudgetIncreaseAmountPM() {
        return budgetIncreaseAmountPM;
    }

    public void setBudgetIncreaseAmountPM(BigDecimal budgetIncreaseAmountPM) {
        this.budgetIncreaseAmountPM = budgetIncreaseAmountPM;
    }

    public BigDecimal getBudgetIncreaseAmountTop() {
        return budgetIncreaseAmountTop;
    }

    public void setBudgetIncreaseAmountTop(BigDecimal budgetIncreaseAmountTop) {
        this.budgetIncreaseAmountTop = budgetIncreaseAmountTop;
    }

    public Long getIdBalanceType() {
        return idBalanceType;
    }

    public void setIdBalanceType(Long idBalanceType) {
        this.idBalanceType = idBalanceType;
    }

    public Long getIdBudgetType() {
        return idBudgetType;
    }

    public void setIdBudgetType(Long idBudgetType) {
        this.idBudgetType = idBudgetType;
    }

    public Long getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(Long idCategory) {
        this.idCategory = idCategory;
    }
}
