package cz.jtbank.kis.bff.repository;

import cz.jtbank.kis.bff.entity.TypBudgetuProjektuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for TypBudgetuProjektuEntity (KP_CIS_TYPBUDGETUPROJEKTU table)
 */
@Repository
public interface TypBudgetuProjektuRepository extends JpaRepository<TypBudgetuProjektuEntity, Long> {

    /**
     * Find project budget type by code
     */
    Optional<TypBudgetuProjektuEntity> findByCode(String code);
}
