package cz.jtbank.kis.bff.repository;

import cz.jtbank.kis.bff.entity.CurrencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for CurrencyEntity (KP_CIS_MENA table)
 */
@Repository
public interface CurrencyRepository extends JpaRepository<CurrencyEntity, Long> {

    /**
     * Find currency by code
     */
    Optional<CurrencyEntity> findByCode(String code);
}
