package cz.jtbank.kis.bff.dto.document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Document entity DTO
 */
public class DocumentDTO {

    private Long id;
    private String number;
    private String type;
    private BigDecimal amount;
    private String currency;
    private LocalDate dueDate;
    private String status;

    /**
     * Creator information (enriched from User service)
     */
    private UserSummaryDTO createdBy;

    /**
     * Company information (enriched from Company service)
     */
    private CompanySummaryDTO company;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public DocumentDTO() {
    }

    public DocumentDTO(Long id, String number, String type, BigDecimal amount, String currency,
                       LocalDate dueDate, String status, UserSummaryDTO createdBy,
                       CompanySummaryDTO company, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.number = number;
        this.type = type;
        this.amount = amount;
        this.currency = currency;
        this.dueDate = dueDate;
        this.status = status;
        this.createdBy = createdBy;
        this.company = company;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public UserSummaryDTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserSummaryDTO createdBy) {
        this.createdBy = createdBy;
    }

    public CompanySummaryDTO getCompany() {
        return company;
    }

    public void setCompany(CompanySummaryDTO company) {
        this.company = company;
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

    public static class Builder {
        private Long id;
        private String number;
        private String type;
        private BigDecimal amount;
        private String currency;
        private LocalDate dueDate;
        private String status;
        private UserSummaryDTO createdBy;
        private CompanySummaryDTO company;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

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

        public Builder createdBy(UserSummaryDTO createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public Builder company(CompanySummaryDTO company) {
            this.company = company;
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

        public DocumentDTO build() {
            return new DocumentDTO(id, number, type, amount, currency, dueDate, status,
                    createdBy, company, createdAt, updatedAt);
        }
    }
}
