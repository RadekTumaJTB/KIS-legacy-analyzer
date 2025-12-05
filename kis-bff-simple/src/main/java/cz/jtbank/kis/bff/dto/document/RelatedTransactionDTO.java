package cz.jtbank.kis.bff.dto.document;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Related transaction (payment, etc.)
 */
public class RelatedTransactionDTO {

    private Long id;
    private String type;
    private BigDecimal amount;
    private LocalDate date;

    public RelatedTransactionDTO() {
    }

    public RelatedTransactionDTO(Long id, String type, BigDecimal amount, LocalDate date) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.date = date;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public static class Builder {
        private Long id;
        private String type;
        private BigDecimal amount;
        private LocalDate date;

        public Builder id(Long id) {
            this.id = id;
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

        public Builder date(LocalDate date) {
            this.date = date;
            return this;
        }

        public RelatedTransactionDTO build() {
            return new RelatedTransactionDTO(id, type, amount, date);
        }
    }
}
