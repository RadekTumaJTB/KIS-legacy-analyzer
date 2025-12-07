package cz.jtbank.kis.bff.dto.document;

/**
 * DTO for adding a comment to a document
 */
public class CommentRequestDTO {
    private String text;
    private String type; // "comment", "approval", "rejection"

    public CommentRequestDTO() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final CommentRequestDTO dto = new CommentRequestDTO();

        public Builder text(String text) {
            dto.text = text;
            return this;
        }

        public Builder type(String type) {
            dto.type = type;
            return this;
        }

        public CommentRequestDTO build() {
            return dto;
        }
    }
}
