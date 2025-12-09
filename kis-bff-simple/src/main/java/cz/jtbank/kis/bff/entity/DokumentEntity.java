package cz.jtbank.kis.bff.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "KP_DAT_DOKUMENT", schema = "DB_JT")
public class DokumentEntity {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "ID_CISDOKUMENT")
    private Long idCisDokument;

    @Column(name = "S_CISLODOKLADU", length = 20)
    private String cisloDokl;

    @Column(name = "ID_SPOLECNOST")
    private Long idSpolecnost;

    @Column(name = "ID_BUDGET")
    private Long idBudget;

    @Column(name = "ID_ZADAVATEL")
    private Long idZadavatel;

    @Column(name = "ID_CISSTATUS")
    private Long idCisStatus;

    @Column(name = "CASTKA", precision = 27, scale = 2)
    private BigDecimal castka;

    @Column(name = "S_MENA", length = 3)
    private String mena;

    @Column(name = "DT_DATUMZADANI")
    private LocalDate datumZadani;

    @Column(name = "DT_DATUMSPLATNOSTI")
    private LocalDate datumSplatnosti;

    @Column(name = "S_POPIS", length = 500)
    private String popis;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIdCisDokument() { return idCisDokument; }
    public void setIdCisDokument(Long idCisDokument) { this.idCisDokument = idCisDokument; }

    public String getCisloDokl() { return cisloDokl; }
    public void setCisloDokl(String cisloDokl) { this.cisloDokl = cisloDokl; }

    public Long getIdSpolecnost() { return idSpolecnost; }
    public void setIdSpolecnost(Long idSpolecnost) { this.idSpolecnost = idSpolecnost; }

    public Long getIdBudget() { return idBudget; }
    public void setIdBudget(Long idBudget) { this.idBudget = idBudget; }

    public Long getIdZadavatel() { return idZadavatel; }
    public void setIdZadavatel(Long idZadavatel) { this.idZadavatel = idZadavatel; }

    public Long getIdCisStatus() { return idCisStatus; }
    public void setIdCisStatus(Long idCisStatus) { this.idCisStatus = idCisStatus; }

    public BigDecimal getCastka() { return castka; }
    public void setCastka(BigDecimal castka) { this.castka = castka; }

    public String getMena() { return mena; }
    public void setMena(String mena) { this.mena = mena; }

    public LocalDate getDatumZadani() { return datumZadani; }
    public void setDatumZadani(LocalDate datumZadani) { this.datumZadani = datumZadani; }

    public LocalDate getDatumSplatnosti() { return datumSplatnosti; }
    public void setDatumSplatnosti(LocalDate datumSplatnosti) { this.datumSplatnosti = datumSplatnosti; }

    public String getPopis() { return popis; }
    public void setPopis(String popis) { this.popis = popis; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
