package cz.jtbank.kis.bff.repository;

import cz.jtbank.kis.bff.entity.ProjektInOutTypEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for ProjektInOutTypEntity (KP_CIS_PROJEKTINOOUTTYP table)
 */
@Repository
public interface ProjektInOutTypRepository extends JpaRepository<ProjektInOutTypEntity, Long> {

    /**
     * Find project in/out type by code
     */
    Optional<ProjektInOutTypEntity> findByCode(String code);
}
