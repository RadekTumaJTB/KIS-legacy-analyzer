package cz.jtbank.kis.bff.repository;

import cz.jtbank.kis.bff.entity.BudgetPolozkaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BudgetPolozkaRepository extends JpaRepository<BudgetPolozkaEntity, Long> {

    List<BudgetPolozkaEntity> findByIdBudgetOrderByMesic(Long budgetId);
}
