package cz.jtbank.kis.bff.dto.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Document entity DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
}
