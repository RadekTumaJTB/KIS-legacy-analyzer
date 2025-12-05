package cz.jtbank.kis.bff.dto.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Related transaction (payment, etc.)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelatedTransactionDTO {

    private Long id;
    private String type;
    private BigDecimal amount;
    private LocalDate date;
}
