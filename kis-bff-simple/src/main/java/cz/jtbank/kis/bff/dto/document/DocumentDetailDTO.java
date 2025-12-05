package cz.jtbank.kis.bff.dto.document;

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

    public DocumentDetailDTO() {
    }

    public DocumentDetailDTO(DocumentDTO document, List<ApprovalChainItemDTO> approvalChain,
                             List<RelatedTransactionDTO> relatedTransactions,
                             List<DocumentLineItemDTO> lineItems, DocumentMetadataDTO metadata) {
        this.document = document;
        this.approvalChain = approvalChain;
        this.relatedTransactions = relatedTransactions;
        this.lineItems = lineItems;
        this.metadata = metadata;
    }

    public static Builder builder() {
        return new Builder();
    }

    public DocumentDTO getDocument() {
        return document;
    }

    public void setDocument(DocumentDTO document) {
        this.document = document;
    }

    public List<ApprovalChainItemDTO> getApprovalChain() {
        return approvalChain;
    }

    public void setApprovalChain(List<ApprovalChainItemDTO> approvalChain) {
        this.approvalChain = approvalChain;
    }

    public List<RelatedTransactionDTO> getRelatedTransactions() {
        return relatedTransactions;
    }

    public void setRelatedTransactions(List<RelatedTransactionDTO> relatedTransactions) {
        this.relatedTransactions = relatedTransactions;
    }

    public List<DocumentLineItemDTO> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<DocumentLineItemDTO> lineItems) {
        this.lineItems = lineItems;
    }

    public DocumentMetadataDTO getMetadata() {
        return metadata;
    }

    public void setMetadata(DocumentMetadataDTO metadata) {
        this.metadata = metadata;
    }

    public static class Builder {
        private DocumentDTO document;
        private List<ApprovalChainItemDTO> approvalChain;
        private List<RelatedTransactionDTO> relatedTransactions;
        private List<DocumentLineItemDTO> lineItems;
        private DocumentMetadataDTO metadata;

        public Builder document(DocumentDTO document) {
            this.document = document;
            return this;
        }

        public Builder approvalChain(List<ApprovalChainItemDTO> approvalChain) {
            this.approvalChain = approvalChain;
            return this;
        }

        public Builder relatedTransactions(List<RelatedTransactionDTO> relatedTransactions) {
            this.relatedTransactions = relatedTransactions;
            return this;
        }

        public Builder lineItems(List<DocumentLineItemDTO> lineItems) {
            this.lineItems = lineItems;
            return this;
        }

        public Builder metadata(DocumentMetadataDTO metadata) {
            this.metadata = metadata;
            return this;
        }

        public DocumentDetailDTO build() {
            return new DocumentDetailDTO(document, approvalChain, relatedTransactions, lineItems, metadata);
        }
    }
}
