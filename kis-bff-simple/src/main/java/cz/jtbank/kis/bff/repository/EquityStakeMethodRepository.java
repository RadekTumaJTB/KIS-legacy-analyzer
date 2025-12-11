package cz.jtbank.kis.bff.repository;

import cz.jtbank.kis.bff.entity.EquityStakeMethodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquityStakeMethodRepository extends JpaRepository<EquityStakeMethodEntity, Long> {
}
