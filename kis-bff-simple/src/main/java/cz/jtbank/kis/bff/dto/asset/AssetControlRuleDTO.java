package cz.jtbank.kis.bff.dto.asset;

/**
 * DTO for Asset Control Rule (Pravidlo kontroly majetku).
 * Used for validating account numbers against equity stake types.
 * Implements business rules for asset accounting validation.
 */
public class AssetControlRuleDTO {

    private Long id;
    private String accountPattern;  // e.g., "061*", "062*"
    private Long equityStakeTypeId;
    private String equityStakeTypeName;
    private Boolean isActive;
    private String validationMessage;  // Custom message for validation failures
    private String description;  // Rule description

    // Constructors
    public AssetControlRuleDTO() {
        this.isActive = true;
    }

    // Builder pattern
    public static class Builder {
        private AssetControlRuleDTO dto = new AssetControlRuleDTO();

        public Builder id(Long id) {
            dto.id = id;
            return this;
        }

        public Builder accountPattern(String accountPattern) {
            dto.accountPattern = accountPattern;
            return this;
        }

        public Builder equityStakeTypeId(Long equityStakeTypeId) {
            dto.equityStakeTypeId = equityStakeTypeId;
            return this;
        }

        public Builder equityStakeTypeName(String equityStakeTypeName) {
            dto.equityStakeTypeName = equityStakeTypeName;
            return this;
        }

        public Builder isActive(Boolean isActive) {
            dto.isActive = isActive;
            return this;
        }

        public Builder validationMessage(String validationMessage) {
            dto.validationMessage = validationMessage;
            return this;
        }

        public Builder description(String description) {
            dto.description = description;
            return this;
        }

        public AssetControlRuleDTO build() {
            return dto;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Check if account number matches this rule's pattern
     * @param accountNumber The account number to validate
     * @return true if matches pattern, false otherwise
     */
    public boolean matches(String accountNumber) {
        if (accountNumber == null || accountPattern == null) {
            return false;
        }

        // Convert pattern to regex (e.g., "061*" → "^061.*")
        String regex = "^" + accountPattern.replace("*", ".*");
        return accountNumber.matches(regex);
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountPattern() {
        return accountPattern;
    }

    public void setAccountPattern(String accountPattern) {
        this.accountPattern = accountPattern;
    }

    public Long getEquityStakeTypeId() {
        return equityStakeTypeId;
    }

    public void setEquityStakeTypeId(Long equityStakeTypeId) {
        this.equityStakeTypeId = equityStakeTypeId;
    }

    public String getEquityStakeTypeName() {
        return equityStakeTypeName;
    }

    public void setEquityStakeTypeName(String equityStakeTypeName) {
        this.equityStakeTypeName = equityStakeTypeName;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getValidationMessage() {
        return validationMessage;
    }

    public void setValidationMessage(String validationMessage) {
        this.validationMessage = validationMessage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
