package cz.jtbank.kis.bff.repository;

import cz.jtbank.kis.bff.entity.ProjektFrekvenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for ProjektFrekvenceEntity (KP_CIS_PROJEKTFREKVENCE table)
 */
@Repository
public interface ProjektFrekvenceRepository extends JpaRepository<ProjektFrekvenceEntity, Long> {

    /**
     * Find project frequency by code
     */
    Optional<ProjektFrekvenceEntity> findByCode(String code);
}
