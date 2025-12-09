package cz.jtbank.kis.bff.repository;

import cz.jtbank.kis.bff.entity.ProjectProposalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for ProjectProposalEntity (KP_KTG_PROJEKTNAVRH table)
 */
@Repository
public interface ProjectProposalRepository extends JpaRepository<ProjectProposalEntity, Long> {

    /**
     * Find all proposals for a specific project ordered by proposal date descending
     */
    List<ProjectProposalEntity> findByIdProjectOrderByProposalDateDesc(Long idProject);

    /**
     * Find proposals by status
     */
    List<ProjectProposalEntity> findByIdStatusOrderByProposalDateDesc(Long idStatus);

    /**
     * Find proposals by proposer
     */
    List<ProjectProposalEntity> findByIdProposerOrderByProposalDateDesc(Long idProposer);
}
