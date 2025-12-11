package cz.jtbank.kis.bff.repository;

import cz.jtbank.kis.bff.entity.TypProjektoveBilanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for TypProjektoveBilanceEntity (KP_CIS_TYPPROJEKTOVEBILANCE table)
 */
@Repository
public interface TypProjektoveBilanceRepository extends JpaRepository<TypProjektoveBilanceEntity, Long> {

    /**
     * Find project balance type by code
     */
    Optional<TypProjektoveBilanceEntity> findByCode(String code);
}
