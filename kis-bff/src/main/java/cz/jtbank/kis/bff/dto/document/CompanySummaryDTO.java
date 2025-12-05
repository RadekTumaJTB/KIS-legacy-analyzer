package cz.jtbank.kis.bff.dto.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Company summary (minimal information for display)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanySummaryDTO {

    private Long id;
    private String name;
    private String ico;
}
