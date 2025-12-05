package cz.jtbank.kis.bff.dto.document;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Document summary for list views
 */
public class DocumentSummaryDTO {

    private Long id;
    private String number;
    private String type;
    private BigDecimal amount;
    private String currency;
    private LocalDate dueDate;
    private String status;
    private String companyName;
    private String createdByName;

    public DocumentSummaryDTO() {
    }

    public DocumentSummaryDTO(Long id, String number, String type, BigDecimal amount,
                              String currency, LocalDate dueDate, String status,
                              String companyName, String createdByName) {
        this.id = id;
        this.number = number;
        this.type = type;
        this.amount = amount;
        this.currency = currency;
        this.dueDate = dueDate;
        this.status = status;
        this.companyName = companyName;
        this.createdByName = createdByName;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public static class Builder {
        private Long id;
        private String number;
        private String type;
        private BigDecimal amount;
        private String currency;
        private LocalDate dueDate;
        private String status;
        private String companyName;
        private String createdByName;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder number(String number) {
            this.number = number;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public Builder dueDate(LocalDate dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder companyName(String companyName) {
            this.companyName = companyName;
            return this;
        }

        public Builder createdByName(String createdByName) {
            this.createdByName = createdByName;
            return this;
        }

        public DocumentSummaryDTO build() {
            return new DocumentSummaryDTO(id, number, type, amount, currency,
                    dueDate, status, companyName, createdByName);
        }
    }
}
