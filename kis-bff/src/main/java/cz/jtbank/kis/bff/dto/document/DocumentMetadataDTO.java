package cz.jtbank.kis.bff.dto.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Frontend metadata (permissions and actions)
 * Calculated based on current user and document state
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentMetadataDTO {

    private Boolean canEdit;
    private Boolean canApprove;
    private Boolean canReject;
    private String pendingApproverName;
}
