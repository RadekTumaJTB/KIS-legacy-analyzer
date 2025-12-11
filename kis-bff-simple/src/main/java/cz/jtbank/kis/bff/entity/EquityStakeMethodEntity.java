package cz.jtbank.kis.bff.entity;

import jakarta.persistence.*;

/**
 * Entity representing Equity Stake Method (Způsob majetkové účasti).
 * Maps to Oracle table: KP_CIS_MAJETKOVAUCASTZPUSOB
 *
 * Examples: Přímá účast (Direct participation), Nepřímá účast (Indirect participation), etc.
 */
@Entity
@Table(name = "KP_CIS_MAJETKOVAUCASTZPUSOB")
public class EquityStakeMethodEntity {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "S_POPIS", length = 100)
    private String description;

    @Column(name = "S_POPIS_EN", length = 100)
    private String descriptionEn; // English description (if exists)

    @Column(name = "C_AKTIVNI", length = 1)
    private String activeFlag; // '1' = active, '0' = inactive

    // Constructors
    public EquityStakeMethodEntity() {
        this.activeFlag = "1";
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionEn() {
        return descriptionEn;
    }

    public void setDescriptionEn(String descriptionEn) {
        this.descriptionEn = descriptionEn;
    }

    public String getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(String activeFlag) {
        this.activeFlag = activeFlag;
    }
}
