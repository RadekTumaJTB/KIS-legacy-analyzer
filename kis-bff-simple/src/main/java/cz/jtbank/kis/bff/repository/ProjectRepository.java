package cz.jtbank.kis.bff.repository;

import cz.jtbank.kis.bff.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for ProjectEntity (KP_KTG_PROJEKT table)
 */
@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    /**
     * Find all projects ordered by creation date descending
     */
    List<ProjectEntity> findAllByOrderByCreatedAtDesc();

    /**
     * Find projects by status
     */
    List<ProjectEntity> findByIdStatusOrderByCreatedAtDesc(Long idStatus);

    /**
     * Find projects by project manager
     */
    List<ProjectEntity> findByIdProjectManagerOrderByCreatedAtDesc(Long idProjectManager);

    /**
     * Find projects by management segment
     */
    List<ProjectEntity> findByIdManagementSegmentOrderByCreatedAtDesc(Long idManagementSegment);
}
