package cz.jtbank.kis.bff.dto.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Single approval step in workflow
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalChainItemDTO {

    private Integer level;
    private UserSummaryDTO approver;
    private String status; // PENDING, APPROVED, REJECTED
    private LocalDateTime approvedAt;
    private String comment;
}
