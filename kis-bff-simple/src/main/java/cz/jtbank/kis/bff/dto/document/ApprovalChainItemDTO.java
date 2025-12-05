package cz.jtbank.kis.bff.dto.document;

import java.time.LocalDateTime;

/**
 * Single approval step in workflow
 */
public class ApprovalChainItemDTO {

    private Integer level;
    private UserSummaryDTO approver;
    private String status; // PENDING, APPROVED, REJECTED
    private LocalDateTime approvedAt;
    private String comment;

    public ApprovalChainItemDTO() {
    }

    public ApprovalChainItemDTO(Integer level, UserSummaryDTO approver, String status, LocalDateTime approvedAt, String comment) {
        this.level = level;
        this.approver = approver;
        this.status = status;
        this.approvedAt = approvedAt;
        this.comment = comment;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public UserSummaryDTO getApprover() {
        return approver;
    }

    public void setApprover(UserSummaryDTO approver) {
        this.approver = approver;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public static class Builder {
        private Integer level;
        private UserSummaryDTO approver;
        private String status;
        private LocalDateTime approvedAt;
        private String comment;

        public Builder level(Integer level) {
            this.level = level;
            return this;
        }

        public Builder approver(UserSummaryDTO approver) {
            this.approver = approver;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder approvedAt(LocalDateTime approvedAt) {
            this.approvedAt = approvedAt;
            return this;
        }

        public Builder comment(String comment) {
            this.comment = comment;
            return this;
        }

        public ApprovalChainItemDTO build() {
            return new ApprovalChainItemDTO(level, approver, status, approvedAt, comment);
        }
    }
}
