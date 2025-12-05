package cz.jtbank.kis.bff.dto.document;

/**
 * User summary (minimal information for display)
 */
public class UserSummaryDTO {

    private Long id;
    private String name;
    private String email;
    private String position;

    public UserSummaryDTO() {
    }

    public UserSummaryDTO(Long id, String name, String email, String position) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.position = position;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public static class Builder {
        private Long id;
        private String name;
        private String email;
        private String position;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder position(String position) {
            this.position = position;
            return this;
        }

        public UserSummaryDTO build() {
            return new UserSummaryDTO(id, name, email, position);
        }
    }
}
