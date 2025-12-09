package cz.jtbank.kis.bff.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * JPA Entity for KP_KTG_PROJEKTNAVRH table
 * Represents project proposals in the KIS banking consolidation system
 */
@Entity
@Table(name = "KP_KTG_PROJEKTNAVRH", schema = "DB_JT")
public class ProjectProposalEntity {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "ID_KTGPROJEKT", nullable = false)
    private Long idProject;

    @Column(name = "S_NAZEV", length = 200)
    private String name;

    @Column(name = "S_POPIS", length = 4000)
    private String description;

    @Column(name = "DT_DATUM")
    private LocalDate proposalDate;

    @Column(name = "ND_CASTKA")
    private BigDecimal amount;

    @Column(name = "ID_STATUS")
    private Long idStatus;

    @Column(name = "ID_NAVRHOVATEL")
    private Long idProposer;

    @Column(name = "ID_SCHVALOVATEL")
    private Long idApprover;

    @Column(name = "DT_SCHVALENO")
    private LocalDate approvedDate;

    @Column(name = "S_POZNAMKA", length = 4000)
    private String notes;

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

    public Long getIdProject() {
        return idProject;
    }

    public void setIdProject(Long idProject) {
        this.idProject = idProject;
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

    public LocalDate getProposalDate() {
        return proposalDate;
    }

    public void setProposalDate(LocalDate proposalDate) {
        this.proposalDate = proposalDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(Long idStatus) {
        this.idStatus = idStatus;
    }

    public Long getIdProposer() {
        return idProposer;
    }

    public void setIdProposer(Long idProposer) {
        this.idProposer = idProposer;
    }

    public Long getIdApprover() {
        return idApprover;
    }

    public void setIdApprover(Long idApprover) {
        this.idApprover = idApprover;
    }

    public LocalDate getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(LocalDate approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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
