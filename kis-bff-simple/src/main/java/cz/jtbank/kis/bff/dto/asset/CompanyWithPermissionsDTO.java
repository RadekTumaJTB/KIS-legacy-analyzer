package cz.jtbank.kis.bff.dto.asset;

/**
 * Company with role-based permissions DTO
 * Used for company selection in Assets module
 */
public class CompanyWithPermissionsDTO {
    private Long id;
    private String name;
    private String ico;
    private Boolean canView;
    private Boolean canEdit;

    // Constructors
    public CompanyWithPermissionsDTO() {
    }

    public CompanyWithPermissionsDTO(Long id, String name, String ico, Boolean canView, Boolean canEdit) {
        this.id = id;
        this.name = name;
        this.ico = ico;
        this.canView = canView;
        this.canEdit = canEdit;
    }

    // Getters and Setters
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

    public Boolean getCanView() {
        return canView;
    }

    public void setCanView(Boolean canView) {
        this.canView = canView;
    }

    public Boolean getCanEdit() {
        return canEdit;
    }

    public void setCanEdit(Boolean canEdit) {
        this.canEdit = canEdit;
    }
}
