package cz.jtbank.kis.bff.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * JPA Entity for KP_KTG_PROJEKT table
 * Represents a project in the KIS banking consolidation system
 */
@Entity
@Table(name = "KP_KTG_PROJEKT", schema = "DB_JT")
public class ProjectEntity {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "S_NAZEV", nullable = false, length = 200)
    private String name;

    @Column(name = "S_CISLO", length = 50)
    private String projectNumber;

    @Column(name = "ID_STATUS")
    private Long idStatus;

    @Column(name = "ID_PMANAGER")
    private Long idProjectManager;

    @Column(name = "ID_MNGSEGMENT")
    private Long idManagementSegment;

    @Column(name = "ID_MENA")
    private Long idCurrency;

    @Column(name = "DT_HODNOCENI_OD")
    private LocalDate valuationStartDate;

    @Column(name = "ID_FREKVENCE")
    private Long idFrequency;

    @Column(name = "S_POPIS", length = 4000)
    private String description;

    @Column(name = "ND_DOCUROVEN1")
    private BigDecimal approvalLevel1Amount;

    @Column(name = "ND_DOCUROVEN2")
    private BigDecimal approvalLevel2Amount;

    @Column(name = "ND_DOCUROVEN3")
    private BigDecimal approvalLevel3Amount;

    @Column(name = "C_SLEDUJEBUDGET")
    private String budgetTrackingFlag;

    @Column(name = "ND_BUDGETNAVYSENI_PM")
    private BigDecimal budgetIncreaseAmountPM;

    @Column(name = "ND_BUDGETNAVYSENI_TOP")
    private BigDecimal budgetIncreaseAmountTop;

    @Column(name = "ID_TYPBILANCE")
    private Long idBalanceType;

    @Column(name = "ID_TYPBUDGETU")
    private Long idBudgetType;

    @Column(name = "ID_KATEGORIE")
    private Long idCategory;

    @Column(name = "DT_CREATED")
    private LocalDateTime createdAt;

    @Column(name = "S_UZIVATEL", length = 50)
    private String createdBy;

    @Column(name = "DT_UPDATED")
    private LocalDateTime updatedAt;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
