package cz.jtbank.kis.bff.repository;

import cz.jtbank.kis.bff.entity.ManagementSegmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for ManagementSegmentEntity (KP_CIS_MNGSEGMENT table)
 */
@Repository
public interface ManagementSegmentRepository extends JpaRepository<ManagementSegmentEntity, Long> {

    /**
     * Find management segment by code
     */
    Optional<ManagementSegmentEntity> findByCode(String code);
}
