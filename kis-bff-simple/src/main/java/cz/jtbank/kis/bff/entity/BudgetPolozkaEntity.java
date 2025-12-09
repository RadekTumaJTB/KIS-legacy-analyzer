package cz.jtbank.kis.bff.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "KP_DAT_BUDGETPOLOZKA", schema = "DB_JT")
public class BudgetPolozkaEntity {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "ID_BUDGET", nullable = false)
    private Long idBudget;

    @Column(name = "MESIC")
    private Integer mesic;

    @Column(name = "PLAN_AMOUNT", precision = 15, scale = 2)
    private BigDecimal planAmount;

    @Column(name = "ACTUAL_AMOUNT", precision = 15, scale = 2)
    private BigDecimal actualAmount;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIdBudget() { return idBudget; }
    public void setIdBudget(Long idBudget) { this.idBudget = idBudget; }

    public Integer getMesic() { return mesic; }
    public void setMesic(Integer mesic) { this.mesic = mesic; }

    public BigDecimal getPlanAmount() { return planAmount; }
    public void setPlanAmount(BigDecimal planAmount) { this.planAmount = planAmount; }

    public BigDecimal getActualAmount() { return actualAmount; }
    public void setActualAmount(BigDecimal actualAmount) { this.actualAmount = actualAmount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
