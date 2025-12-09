package cz.jtbank.kis.bff.repository;

import cz.jtbank.kis.bff.entity.BudgetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BudgetRepository extends JpaRepository<BudgetEntity, Long> {

    List<BudgetEntity> findByIdKtgOdbor(Long odborId);
}
