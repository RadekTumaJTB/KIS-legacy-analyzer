package cz.jtbank.kis.bff.dto.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Frontend-optimized DTO for Document Detail view
 *
 * Aggregates data from multiple services:
 * - Document entity (core service)
 * - Approval chain (approval service)
 * - Related transactions (transaction service)
 * - Line items (document service)
 * - Metadata (calculated permissions)
 *
 * Performance: 1 API call instead of 5
 * Expected load time: ~400ms (vs 2000ms without BFF)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDetailDTO {

    /**
     * Core document information
     */
    private DocumentDTO document;

    /**
     * Approval workflow with user details
     */
    private List<ApprovalChainItemDTO> approvalChain;

    /**
     * Related transactions (payments, etc.)
     */
    private List<RelatedTransactionDTO> relatedTransactions;

    /**
     * Document line items
     */
    private List<DocumentLineItemDTO> lineItems;

    /**
     * Frontend metadata (permissions, actions)
     */
    private DocumentMetadataDTO metadata;
}
