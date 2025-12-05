package cz.jtbank.kis.bff.dto.document;

/**
 * Frontend metadata (permissions and actions)
 * Calculated based on current user and document state
 */
public class DocumentMetadataDTO {

    private Boolean canEdit;
    private Boolean canApprove;
    private Boolean canReject;
    private String pendingApproverName;

    public DocumentMetadataDTO() {
    }

    public DocumentMetadataDTO(Boolean canEdit, Boolean canApprove, Boolean canReject, String pendingApproverName) {
        this.canEdit = canEdit;
        this.canApprove = canApprove;
        this.canReject = canReject;
        this.pendingApproverName = pendingApproverName;
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

    public static class Builder {
        private Boolean canEdit;
        private Boolean canApprove;
        private Boolean canReject;
        private String pendingApproverName;

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

        public DocumentMetadataDTO build() {
            return new DocumentMetadataDTO(canEdit, canApprove, canReject, pendingApproverName);
        }
    }
}
