package cz.jtbank.kis.bff.repository;

import cz.jtbank.kis.bff.entity.ProjectCashFlowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for ProjectCashFlowEntity (KP_DAT_PROJEKTCASHFLOW table)
 */
@Repository
public interface ProjectCashFlowRepository extends JpaRepository<ProjectCashFlowEntity, Long> {

    /**
     * Find all cash flow entries for a specific project ordered by date
     */
    List<ProjectCashFlowEntity> findByIdProjectOrderByDateDesc(Long idProject);

    /**
     * Find cash flow entries by project and type
     */
    List<ProjectCashFlowEntity> findByIdProjectAndIdCashFlowTypeOrderByDateDesc(Long idProject, Long idCashFlowType);

    /**
     * Find cash flow entries by project and in/out type
     */
    List<ProjectCashFlowEntity> findByIdProjectAndIdInOutTypeOrderByDateDesc(Long idProject, Long idInOutType);
}
