package cz.jtbank.kis.bff.dto.document;

import java.util.List;

/**
 * DTO for approval/rejection actions
 * Supports both single and bulk operations
 */
public class ApprovalActionRequestDTO {
    private String action; // "approve" or "reject"
    private String comment;
    private List<Long> documentIds; // For bulk operations

    public ApprovalActionRequestDTO() {
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<Long> getDocumentIds() {
        return documentIds;
    }

    public void setDocumentIds(List<Long> documentIds) {
        this.documentIds = documentIds;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final ApprovalActionRequestDTO dto = new ApprovalActionRequestDTO();

        public Builder action(String action) {
            dto.action = action;
            return this;
        }

        public Builder comment(String comment) {
            dto.comment = comment;
            return this;
        }

        public Builder documentIds(List<Long> documentIds) {
            dto.documentIds = documentIds;
            return this;
        }

        public ApprovalActionRequestDTO build() {
            return dto;
        }
    }
}
