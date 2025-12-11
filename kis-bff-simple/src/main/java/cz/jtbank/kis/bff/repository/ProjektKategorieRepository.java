package cz.jtbank.kis.bff.repository;

import cz.jtbank.kis.bff.entity.ProjektKategorieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for ProjektKategorieEntity (KP_CIS_PROJEKTKATEGORIE table)
 */
@Repository
public interface ProjektKategorieRepository extends JpaRepository<ProjektKategorieEntity, Long> {

    /**
     * Find project category by code
     */
    Optional<ProjektKategorieEntity> findByCode(String code);
}
