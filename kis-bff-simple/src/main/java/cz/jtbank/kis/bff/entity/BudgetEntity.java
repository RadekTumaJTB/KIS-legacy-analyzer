package cz.jtbank.kis.bff.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "KP_DAT_BUDGET", schema = "DB_JT")
public class BudgetEntity {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "ID_KTGODBOR", nullable = false)
    private Long idKtgOdbor;

    @Column(name = "DT_PLATNOSTOD", nullable = false)
    private LocalDate platnostOd;

    @Column(name = "DT_PLATNOSTDO", nullable = false)
    private LocalDate platnostDo;

    @Column(name = "ID_KTGPROJEKT")
    private Long idKtgProjekt;

    @Column(name = "DT_SCHVALITDO")
    private LocalDate schvalitDo;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIdKtgOdbor() { return idKtgOdbor; }
    public void setIdKtgOdbor(Long idKtgOdbor) { this.idKtgOdbor = idKtgOdbor; }

    public LocalDate getPlatnostOd() { return platnostOd; }
    public void setPlatnostOd(LocalDate platnostOd) { this.platnostOd = platnostOd; }

    public LocalDate getPlatnostDo() { return platnostDo; }
    public void setPlatnostDo(LocalDate platnostDo) { this.platnostDo = platnostDo; }

    public Long getIdKtgProjekt() { return idKtgProjekt; }
    public void setIdKtgProjekt(Long idKtgProjekt) { this.idKtgProjekt = idKtgProjekt; }

    public LocalDate getSchvalitDo() { return schvalitDo; }
    public void setSchvalitDo(LocalDate schvalitDo) { this.schvalitDo = schvalitDo; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
