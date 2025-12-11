package cz.jtbank.kis.bff.repository;

import cz.jtbank.kis.bff.entity.ProjektCashFlowTypEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for ProjektCashFlowTypEntity (KP_CIS_PROJEKTCASHFLOWTYP table)
 */
@Repository
public interface ProjektCashFlowTypRepository extends JpaRepository<ProjektCashFlowTypEntity, Long> {

    /**
     * Find project cash flow type by code
     */
    Optional<ProjektCashFlowTypEntity> findByCode(String code);
}
