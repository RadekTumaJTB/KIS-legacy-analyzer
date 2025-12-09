package cz.jtbank.kis.bff.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "KP_KTG_ODBOR", schema = "DB_JT")
public class OdborEntity {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "S_KOD", unique = true, nullable = false, length = 20)
    private String kod;

    @Column(name = "S_NAZEV", nullable = false, length = 200)
    private String nazev;

    @Column(name = "ACTIVE", length = 1)
    private String active;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getKod() { return kod; }
    public void setKod(String kod) { this.kod = kod; }

    public String getNazev() { return nazev; }
    public void setNazev(String nazev) { this.nazev = nazev; }

    public String getActive() { return active; }
    public void setActive(String active) { this.active = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
