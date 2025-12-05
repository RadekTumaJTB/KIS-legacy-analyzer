package cz.jtbank.kis.bff.dto.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Document line item
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentLineItemDTO {

    private Long id;
    private String description;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal total;
}
