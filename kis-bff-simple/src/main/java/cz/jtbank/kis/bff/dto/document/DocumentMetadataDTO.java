package cz.jtbank.kis.bff.dto.document;

import java.time.LocalDateTime;

/**
 * Frontend metadata (permissions and actions)
 * Calculated based on current user and document state
 */
public class DocumentMetadataDTO {

    private Boolean canEdit;
    private Boolean canApprove;
    private Boolean canReject;
    private String pendingApproverName;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String modifiedBy;
    private Integer version;

    public DocumentMetadataDTO() {
    }

    public DocumentMetadataDTO(Boolean canEdit, Boolean canApprove, Boolean canReject, String pendingApproverName,
                               LocalDateTime createdAt, LocalDateTime modifiedAt, String modifiedBy, Integer version) {
        this.canEdit = canEdit;
        this.canApprove = canApprove;
        this.canReject = canReject;
        this.pendingApproverName = pendingApproverName;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.modifiedBy = modifiedBy;
        this.version = version;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Boolean getCanEdit() {
        return canEdit;
    }

    public void setCanEdit(Boolean canEdit) {
        this.canEdit = canEdit;
    }

    public Boolean getCanApprove() {
        return canApprove;
    }

    public void setCanApprove(Boolean canApprove) {
        this.canApprove = canApprove;
    }

    public Boolean getCanReject() {
        return canReject;
    }

    public void setCanReject(Boolean canReject) {
        this.canReject = canReject;
    }

    public String getPendingApproverName() {
        return pendingApproverName;
    }

    public void setPendingApproverName(String pendingApproverName) {
        this.pendingApproverName = pendingApproverName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public static class Builder {
        private Boolean canEdit;
        private Boolean canApprove;
        private Boolean canReject;
        private String pendingApproverName;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        private String modifiedBy;
        private Integer version;

        public Builder canEdit(Boolean canEdit) {
            this.canEdit = canEdit;
            return this;
        }

        public Builder canApprove(Boolean canApprove) {
            this.canApprove = canApprove;
            return this;
        }

        public Builder canReject(Boolean canReject) {
            this.canReject = canReject;
            return this;
        }

        public Builder pendingApproverName(String pendingApproverName) {
            this.pendingApproverName = pendingApproverName;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder modifiedAt(LocalDateTime modifiedAt) {
            this.modifiedAt = modifiedAt;
            return this;
        }

        public Builder modifiedBy(String modifiedBy) {
            this.modifiedBy = modifiedBy;
            return this;
        }

        public Builder version(Integer version) {
            this.version = version;
            return this;
        }

        public DocumentMetadataDTO build() {
            return new DocumentMetadataDTO(canEdit, canApprove, canReject, pendingApproverName,
                                          createdAt, modifiedAt, modifiedBy, version);
        }
    }
}
