package cz.jtbank.kis.bff.repository;

import cz.jtbank.kis.bff.entity.EquityStakeTransactionTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquityStakeTransactionTypeRepository extends JpaRepository<EquityStakeTransactionTypeEntity, Long> {
}
