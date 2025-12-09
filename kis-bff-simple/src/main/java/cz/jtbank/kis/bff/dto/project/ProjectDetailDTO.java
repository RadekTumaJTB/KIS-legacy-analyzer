package cz.jtbank.kis.bff.dto.project;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Complete project detail with cash flow entries
 */
public class ProjectDetailDTO {

    private Long id;
    private String name;
    private String projectNumber;
    private String status;
    private String statusCode;
    private String projectManagerName;
    private Long idProjectManager;
    private String managementSegmentName;
    private Long idManagementSegment;
    private String currencyCode;
    private Long idCurrency;
    private LocalDate valuationStartDate;
    private String frequencyName;
    private Long idFrequency;
    private String description;
    private BigDecimal approvalLevel1Amount;
    private BigDecimal approvalLevel2Amount;
    private BigDecimal approvalLevel3Amount;
    private String budgetTrackingFlag;
    private BigDecimal budgetIncreaseAmountPM;
    private BigDecimal budgetIncreaseAmountTop;
    private String balanceTypeName;
    private Long idBalanceType;
    private String budgetTypeName;
    private Long idBudgetType;
    private String categoryName;
    private Long idCategory;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private List<ProjectCashFlowDTO> cashFlowList;

    public ProjectDetailDTO() {
    }

    public ProjectDetailDTO(Long id, String name, String projectNumber, String status, String statusCode,
                          String projectManagerName, Long idProjectManager, String managementSegmentName,
                          Long idManagementSegment, String currencyCode, Long idCurrency,
                          LocalDate valuationStartDate, String frequencyName, Long idFrequency,
                          String description, BigDecimal approvalLevel1Amount, BigDecimal approvalLevel2Amount,
                          BigDecimal approvalLevel3Amount, String budgetTrackingFlag,
                          BigDecimal budgetIncreaseAmountPM, BigDecimal budgetIncreaseAmountTop,
                          String balanceTypeName, Long idBalanceType, String budgetTypeName,
                          Long idBudgetType, String categoryName, Long idCategory, LocalDateTime createdAt,
                          String createdBy, LocalDateTime updatedAt, List<ProjectCashFlowDTO> cashFlowList) {
        this.id = id;
        this.name = name;
        this.projectNumber = projectNumber;
        this.status = status;
        this.statusCode = statusCode;
        this.projectManagerName = projectManagerName;
        this.idProjectManager = idProjectManager;
        this.managementSegmentName = managementSegmentName;
        this.idManagementSegment = idManagementSegment;
        this.currencyCode = currencyCode;
        this.idCurrency = idCurrency;
        this.valuationStartDate = valuationStartDate;
        this.frequencyName = frequencyName;
        this.idFrequency = idFrequency;
        this.description = description;
        this.approvalLevel1Amount = approvalLevel1Amount;
        this.approvalLevel2Amount = approvalLevel2Amount;
        this.approvalLevel3Amount = approvalLevel3Amount;
        this.budgetTrackingFlag = budgetTrackingFlag;
        this.budgetIncreaseAmountPM = budgetIncreaseAmountPM;
        this.budgetIncreaseAmountTop = budgetIncreaseAmountTop;
        this.balanceTypeName = balanceTypeName;
        this.idBalanceType = idBalanceType;
        this.budgetTypeName = budgetTypeName;
        this.idBudgetType = idBudgetType;
        this.categoryName = categoryName;
        this.idCategory = idCategory;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.cashFlowList = cashFlowList;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getProjectManagerName() {
        return projectManagerName;
    }

    public void setProjectManagerName(String projectManagerName) {
        this.projectManagerName = projectManagerName;
    }

    public Long getIdProjectManager() {
        return idProjectManager;
    }

    public void setIdProjectManager(Long idProjectManager) {
        this.idProjectManager = idProjectManager;
    }

    public String getManagementSegmentName() {
        return managementSegmentName;
    }

    public void setManagementSegmentName(String managementSegmentName) {
        this.managementSegmentName = managementSegmentName;
    }

    public Long getIdManagementSegment() {
        return idManagementSegment;
    }

    public void setIdManagementSegment(Long idManagementSegment) {
        this.idManagementSegment = idManagementSegment;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
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

    public String getFrequencyName() {
        return frequencyName;
    }

    public void setFrequencyName(String frequencyName) {
        this.frequencyName = frequencyName;
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

    public String getBalanceTypeName() {
        return balanceTypeName;
    }

    public void setBalanceTypeName(String balanceTypeName) {
        this.balanceTypeName = balanceTypeName;
    }

    public Long getIdBalanceType() {
        return idBalanceType;
    }

    public void setIdBalanceType(Long idBalanceType) {
        this.idBalanceType = idBalanceType;
    }

    public String getBudgetTypeName() {
        return budgetTypeName;
    }

    public void setBudgetTypeName(String budgetTypeName) {
        this.budgetTypeName = budgetTypeName;
    }

    public Long getIdBudgetType() {
        return idBudgetType;
    }

    public void setIdBudgetType(Long idBudgetType) {
        this.idBudgetType = idBudgetType;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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

    public List<ProjectCashFlowDTO> getCashFlowList() {
        return cashFlowList;
    }

    public void setCashFlowList(List<ProjectCashFlowDTO> cashFlowList) {
        this.cashFlowList = cashFlowList;
    }

    // Builder class

    public static class Builder {
        private Long id;
        private String name;
        private String projectNumber;
        private String status;
        private String statusCode;
        private String projectManagerName;
        private Long idProjectManager;
        private String managementSegmentName;
        private Long idManagementSegment;
        private String currencyCode;
        private Long idCurrency;
        private LocalDate valuationStartDate;
        private String frequencyName;
        private Long idFrequency;
        private String description;
        private BigDecimal approvalLevel1Amount;
        private BigDecimal approvalLevel2Amount;
        private BigDecimal approvalLevel3Amount;
        private String budgetTrackingFlag;
        private BigDecimal budgetIncreaseAmountPM;
        private BigDecimal budgetIncreaseAmountTop;
        private String balanceTypeName;
        private Long idBalanceType;
        private String budgetTypeName;
        private Long idBudgetType;
        private String categoryName;
        private Long idCategory;
        private LocalDateTime createdAt;
        private String createdBy;
        private LocalDateTime updatedAt;
        private List<ProjectCashFlowDTO> cashFlowList;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder projectNumber(String projectNumber) {
            this.projectNumber = projectNumber;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder statusCode(String statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder projectManagerName(String projectManagerName) {
            this.projectManagerName = projectManagerName;
            return this;
        }

        public Builder idProjectManager(Long idProjectManager) {
            this.idProjectManager = idProjectManager;
            return this;
        }

        public Builder managementSegmentName(String managementSegmentName) {
            this.managementSegmentName = managementSegmentName;
            return this;
        }

        public Builder idManagementSegment(Long idManagementSegment) {
            this.idManagementSegment = idManagementSegment;
            return this;
        }

        public Builder currencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
            return this;
        }

        public Builder idCurrency(Long idCurrency) {
            this.idCurrency = idCurrency;
            return this;
        }

        public Builder valuationStartDate(LocalDate valuationStartDate) {
            this.valuationStartDate = valuationStartDate;
            return this;
        }

        public Builder frequencyName(String frequencyName) {
            this.frequencyName = frequencyName;
            return this;
        }

        public Builder idFrequency(Long idFrequency) {
            this.idFrequency = idFrequency;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder approvalLevel1Amount(BigDecimal approvalLevel1Amount) {
            this.approvalLevel1Amount = approvalLevel1Amount;
            return this;
        }

        public Builder approvalLevel2Amount(BigDecimal approvalLevel2Amount) {
            this.approvalLevel2Amount = approvalLevel2Amount;
            return this;
        }

        public Builder approvalLevel3Amount(BigDecimal approvalLevel3Amount) {
            this.approvalLevel3Amount = approvalLevel3Amount;
            return this;
        }

        public Builder budgetTrackingFlag(String budgetTrackingFlag) {
            this.budgetTrackingFlag = budgetTrackingFlag;
            return this;
        }

        public Builder budgetIncreaseAmountPM(BigDecimal budgetIncreaseAmountPM) {
            this.budgetIncreaseAmountPM = budgetIncreaseAmountPM;
            return this;
        }

        public Builder budgetIncreaseAmountTop(BigDecimal budgetIncreaseAmountTop) {
            this.budgetIncreaseAmountTop = budgetIncreaseAmountTop;
            return this;
        }

        public Builder balanceTypeName(String balanceTypeName) {
            this.balanceTypeName = balanceTypeName;
            return this;
        }

        public Builder idBalanceType(Long idBalanceType) {
            this.idBalanceType = idBalanceType;
            return this;
        }

        public Builder budgetTypeName(String budgetTypeName) {
            this.budgetTypeName = budgetTypeName;
            return this;
        }

        public Builder idBudgetType(Long idBudgetType) {
            this.idBudgetType = idBudgetType;
            return this;
        }

        public Builder categoryName(String categoryName) {
            this.categoryName = categoryName;
            return this;
        }

        public Builder idCategory(Long idCategory) {
            this.idCategory = idCategory;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder cashFlowList(List<ProjectCashFlowDTO> cashFlowList) {
            this.cashFlowList = cashFlowList;
            return this;
        }

        public ProjectDetailDTO build() {
            return new ProjectDetailDTO(id, name, projectNumber, status, statusCode, projectManagerName,
                    idProjectManager, managementSegmentName, idManagementSegment, currencyCode, idCurrency,
                    valuationStartDate, frequencyName, idFrequency, description, approvalLevel1Amount,
                    approvalLevel2Amount, approvalLevel3Amount, budgetTrackingFlag, budgetIncreaseAmountPM,
                    budgetIncreaseAmountTop, balanceTypeName, idBalanceType, budgetTypeName, idBudgetType,
                    categoryName, idCategory, createdAt, createdBy, updatedAt, cashFlowList);
        }
    }
}
