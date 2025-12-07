package cz.jtbank.kis.bff.dto.document;

import java.math.BigDecimal;

/**
 * DTO for document update request
 * Used when user edits document fields inline
 */
public class DocumentUpdateRequestDTO {
    private String type;
    private BigDecimal amount;
    private String dueDate;
    private String companyName;

    public DocumentUpdateRequestDTO() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final DocumentUpdateRequestDTO dto = new DocumentUpdateRequestDTO();

        public Builder type(String type) {
            dto.type = type;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            dto.amount = amount;
            return this;
        }

        public Builder dueDate(String dueDate) {
            dto.dueDate = dueDate;
            return this;
        }

        public Builder companyName(String companyName) {
            dto.companyName = companyName;
            return this;
        }

        public DocumentUpdateRequestDTO build() {
            return dto;
        }
    }
}
