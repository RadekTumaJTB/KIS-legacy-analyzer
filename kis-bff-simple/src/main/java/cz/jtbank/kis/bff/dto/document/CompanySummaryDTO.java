package cz.jtbank.kis.bff.dto.document;

/**
 * Company summary (minimal information for display)
 */
public class CompanySummaryDTO {

    private Long id;
    private String name;
    private String registrationNumber; // Changed from ico
    private String address;

    public CompanySummaryDTO() {
    }

    public CompanySummaryDTO(Long id, String name, String registrationNumber, String address) {
        this.id = id;
        this.name = name;
        this.registrationNumber = registrationNumber;
        this.address = address;
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

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static class Builder {
        private Long id;
        private String name;
        private String registrationNumber;
        private String address;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder registrationNumber(String registrationNumber) {
            this.registrationNumber = registrationNumber;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        // Keep ico() for backward compatibility
        public Builder ico(String ico) {
            this.registrationNumber = ico;
            return this;
        }

        public CompanySummaryDTO build() {
            return new CompanySummaryDTO(id, name, registrationNumber, address);
        }
    }
}
