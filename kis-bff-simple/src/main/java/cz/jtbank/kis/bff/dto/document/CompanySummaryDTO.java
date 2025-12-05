package cz.jtbank.kis.bff.dto.document;

/**
 * Company summary (minimal information for display)
 */
public class CompanySummaryDTO {

    private Long id;
    private String name;
    private String ico;

    public CompanySummaryDTO() {
    }

    public CompanySummaryDTO(Long id, String name, String ico) {
        this.id = id;
        this.name = name;
        this.ico = ico;
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

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public static class Builder {
        private Long id;
        private String name;
        private String ico;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder ico(String ico) {
            this.ico = ico;
            return this;
        }

        public CompanySummaryDTO build() {
            return new CompanySummaryDTO(id, name, ico);
        }
    }
}
