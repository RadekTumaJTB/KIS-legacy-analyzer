package cz.jtbank.kis.bff.dto.project;

import java.time.LocalDate;

/**
 * Project summary for list view
 */
public class ProjectSummaryDTO {

    private Long id;
    private String name;
    private String projectNumber;
    private String status;
    private String statusCode;
    private String projectManagerName;
    private String managementSegmentName;
    private LocalDate startDate;
    private LocalDate valuationStartDate;
    private String description;

    public ProjectSummaryDTO() {
    }

    public ProjectSummaryDTO(Long id, String name, String projectNumber, String status, String statusCode,
                           String projectManagerName, String managementSegmentName, LocalDate startDate,
                           LocalDate valuationStartDate, String description) {
        this.id = id;
        this.name = name;
        this.projectNumber = projectNumber;
        this.status = status;
        this.statusCode = statusCode;
        this.projectManagerName = projectManagerName;
        this.managementSegmentName = managementSegmentName;
        this.startDate = startDate;
        this.valuationStartDate = valuationStartDate;
        this.description = description;
    }

    public static Builder builder() {
        return new Builder();
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

    public String getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(String projectNumber) {
        this.projectNumber = projectNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getProjectManagerName() {
        return projectManagerName;
    }

    public void setProjectManagerName(String projectManagerName) {
        this.projectManagerName = projectManagerName;
    }

    public String getManagementSegmentName() {
        return managementSegmentName;
    }

    public void setManagementSegmentName(String managementSegmentName) {
        this.managementSegmentName = managementSegmentName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getValuationStartDate() {
        return valuationStartDate;
    }

    public void setValuationStartDate(LocalDate valuationStartDate) {
        this.valuationStartDate = valuationStartDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Builder class

    public static class Builder {
        private Long id;
        private String name;
        private String projectNumber;
        private String status;
        private String statusCode;
        private String projectManagerName;
        private String managementSegmentName;
        private LocalDate startDate;
        private LocalDate valuationStartDate;
        private String description;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder projectNumber(String projectNumber) {
            this.projectNumber = projectNumber;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder statusCode(String statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder projectManagerName(String projectManagerName) {
            this.projectManagerName = projectManagerName;
            return this;
        }

        public Builder managementSegmentName(String managementSegmentName) {
            this.managementSegmentName = managementSegmentName;
            return this;
        }

        public Builder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder valuationStartDate(LocalDate valuationStartDate) {
            this.valuationStartDate = valuationStartDate;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public ProjectSummaryDTO build() {
            return new ProjectSummaryDTO(id, name, projectNumber, status, statusCode, projectManagerName,
                    managementSegmentName, startDate, valuationStartDate, description);
        }
    }
}
