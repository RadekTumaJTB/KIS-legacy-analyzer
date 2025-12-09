package cz.jtbank.kis.bff.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * JPA Entity for KP_DAT_PROJEKTCASHFLOW table
 * Represents cash flow entries for projects (inflows/outflows)
 */
@Entity
@Table(name = "KP_DAT_PROJEKTCASHFLOW", schema = "DB_JT")
public class ProjectCashFlowEntity {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "ID_KTGPROJEKT", nullable = false)
    private Long idProject;

    @Column(name = "ID_CASHFLOWTYP")
    private Long idCashFlowType;

    @Column(name = "DT_DATUM")
    private LocalDate date;

    @Column(name = "ND_CASTKA")
    private BigDecimal amount;

    @Column(name = "ID_MENA")
    private Long idCurrency;

    @Column(name = "ID_INOUTTTYP")
    private Long idInOutType;

    @Column(name = "ID_POZICETYP")
    private Long idPositionType;

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

    public Long getIdCashFlowType() {
        return idCashFlowType;
    }

    public void setIdCashFlowType(Long idCashFlowType) {
        this.idCashFlowType = idCashFlowType;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getIdCurrency() {
        return idCurrency;
    }

    public void setIdCurrency(Long idCurrency) {
        this.idCurrency = idCurrency;
    }

    public Long getIdInOutType() {
        return idInOutType;
    }

    public void setIdInOutType(Long idInOutType) {
        this.idInOutType = idInOutType;
    }

    public Long getIdPositionType() {
        return idPositionType;
    }

    public void setIdPositionType(Long idPositionType) {
        this.idPositionType = idPositionType;
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
