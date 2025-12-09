package cz.jtbank.kis.bff.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * JPA Entity for KP_CIS_PROJEKTSTATUS table
 * Represents project status lookup table in the KIS banking consolidation system
 */
@Entity
@Table(name = "KP_CIS_PROJEKTSTATUS", schema = "DB_JT")
public class ProjectStatusEntity {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "S_NAZEV", nullable = false, length = 100)
    private String name;

    @Column(name = "S_KOD", length = 50)
    private String code;

    @Column(name = "S_POPIS", length = 1000)
    private String description;

    @Column(name = "DT_CREATED")
    private LocalDateTime createdAt;

    @Column(name = "S_UZIVATEL", length = 50)
    private String createdBy;

    @Column(name = "DT_UPDATED")
    private LocalDateTime updatedAt;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
