package cz.jtbank.kis.bff.dto;

/**
 * Generic DTO for reference data (lookups, dropdowns)
 * Used for statuses, categories, segments, types, etc.
 */
public class ReferenceDataDTO {
    private Long id;
    private String code;
    private String name;
    private String description;

    public ReferenceDataDTO() {}

    public ReferenceDataDTO(Long id, String code, String name, String description) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
    }

    // Builder pattern methods for easy construction
    public static ReferenceDataDTO of(Long id, String name) {
        return new ReferenceDataDTO(id, null, name, null);
    }

    public static ReferenceDataDTO of(Long id, String code, String name) {
        return new ReferenceDataDTO(id, code, name, null);
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
