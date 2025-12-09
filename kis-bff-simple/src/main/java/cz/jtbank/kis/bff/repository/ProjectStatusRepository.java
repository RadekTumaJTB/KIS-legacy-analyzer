package cz.jtbank.kis.bff.repository;

import cz.jtbank.kis.bff.entity.ProjectStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for ProjectStatusEntity (KP_CIS_PROJEKTSTATUS table)
 */
@Repository
public interface ProjectStatusRepository extends JpaRepository<ProjectStatusEntity, Long> {

    /**
     * Find status by code
     */
    Optional<ProjectStatusEntity> findByCode(String code);
}
