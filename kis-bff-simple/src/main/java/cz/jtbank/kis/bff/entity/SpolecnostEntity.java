package cz.jtbank.kis.bff.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "KP_KTG_SPOLECNOST", schema = "DB_JT")
public class SpolecnostEntity {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "S_NAZEV", nullable = false, length = 200)
    private String nazev;

    @Column(name = "S_ICO", length = 20)
    private String ico;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNazev() { return nazev; }
    public void setNazev(String nazev) { this.nazev = nazev; }

    public String getIco() { return ico; }
    public void setIco(String ico) { this.ico = ico; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
