package cz.jtbank.kis.bff.service;

import cz.jtbank.kis.bff.dto.project.ProjectSummaryDTO;
import cz.jtbank.kis.bff.repository.oraclepackage.ProjectPackageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Service using Oracle Package calls instead of JPA.
 *
 * This service mirrors the original Oracle ADF/BC4J architecture
 * by calling stored procedures in KAP_PROJEKT package.
 *
 * Pattern: Service → ProjectPackageRepository → CallableStatement → Oracle Package
 *
 * Original: ProjektModuleImpl.kpProjekt() → db_jt.kap_projekt.p_KpProjekt()
 * New:      ProjectOraclePackageService → ProjectPackageRepository.callKpProjekt()
 */
@Service
@Transactional
public class ProjectOraclePackageService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectOraclePackageService.class);

    private final ProjectPackageRepository packageRepository;
    private final JdbcTemplate jdbcTemplate;

    public ProjectOraclePackageService(
            ProjectPackageRepository packageRepository,
            JdbcTemplate jdbcTemplate) {
        this.packageRepository = packageRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Create a new project by calling Oracle package procedure.
     * Demonstration method - parameters would come from ProjectFormData in real implementation.
     */
    public Integer createProjectDemo(String projectName, String description, String currentUser) throws SQLException {
        logger.info("Creating project: {} by user: {}", projectName, currentUser);

        // Generate new project ID using sequence
        Integer newProjectId = jdbcTemplate.queryForObject(
                "SELECT DB_JT.SEQ_KP_PROJEKT.NEXTVAL FROM DUAL",
                Integer.class
        );

        Date startDate = Date.valueOf(LocalDate.now());

        // Call Oracle package procedure with action 'I' (Insert)
        // Using minimal required parameters for demonstration
        packageRepository.callKpProjekt(
                "I",                        // akce: Insert
                newProjectId,               // idProjekt
                projectName,                // nazev
                null,                       // cisloOld
                1,                          // idStatus (default: Aktivní)
                1,                          // idNavrhuje (default user)
                1,                          // idPManager (default manager)
                null,                       // idMngSeg (nullable)
                "CZK",                      // menaNaklady
                startDate,                  // startOceneni
                1,                          // idFrekvence (default)
                description != null ? description : "", // popis
                null,                       // idNavrh
                null,                       // dtMDalsi
                0,                          // mMesicu
                currentUser,                // uzivatel
                null,                       // idTypBilance
                "A",                        // sledujeBudget (A = Yes)
                null,                       // idTypBudgetu
                null                        // kategorie
        );

        logger.info("Successfully created project with ID: {}", newProjectId);
        return newProjectId;
    }

    /**
     * Update existing project by calling Oracle package procedure.
     * Demonstration method.
     */
    public void updateProjectDemo(Integer projectId, String projectName, String description, String currentUser) throws SQLException {
        logger.info("Updating project ID: {} by user: {}", projectId, currentUser);

        Date startDate = Date.valueOf(LocalDate.now());

        // Call Oracle package procedure with action 'U' (Update)
        packageRepository.callKpProjekt(
                "U",                        // akce: Update
                projectId,                  // idProjekt
                projectName,                // nazev
                null,                       // cisloOld
                1,                          // idStatus
                1,                          // idNavrhuje
                1,                          // idPManager
                null,                       // idMngSeg
                "CZK",                      // menaNaklady
                startDate,                  // startOceneni
                1,                          // idFrekvence
                description != null ? description : "", // popis
                null,                       // idNavrh
                null,                       // dtMDalsi
                0,                          // mMesicu
                currentUser,                // uzivatel
                null,                       // idTypBilance
                "A",                        // sledujeBudget
                null,                       // idTypBudgetu
                null                        // kategorie
        );

        logger.info("Successfully updated project ID: {}", projectId);
    }

    /**
     * Delete project by calling Oracle package procedure.
     */
    public void deleteProject(Integer projectId, String currentUser) throws SQLException {
        logger.info("Deleting project ID: {} by user: {}", projectId, currentUser);

        // Call Oracle package procedure with action 'D' (Delete)
        // Only projectId, action, and username are required for delete
        packageRepository.callKpProjekt(
                "D",                        // akce: Delete
                projectId,                  // idProjekt
                null,                       // nazev
                null,                       // cisloOld
                null,                       // idStatus
                null,                       // idNavrhuje
                null,                       // idPManager
                null,                       // idMngSeg
                null,                       // menaNaklady
                null,                       // startOceneni
                null,                       // idFrekvence
                null,                       // popis
                null,                       // idNavrh
                null,                       // dtMDalsi
                null,                       // mMesicu
                currentUser,                // uzivatel
                null,                       // idTypBilance
                null,                       // sledujeBudget
                null,                       // idTypBudgetu
                null                        // kategorie
        );

        logger.info("Successfully deleted project ID: {}", projectId);
    }

    /**
     * Get project list by querying Oracle views directly.
     * Uses SELECT instead of JPA for better compatibility with Oracle packages.
     *
     * @return List of projects
     */
    public List<ProjectSummaryDTO> getProjectList() {
        logger.info("Fetching project list using direct SQL query");

        String sql = """
                SELECT
                    p.ID as id,
                    p.S_NAZEV as name,
                    p.S_CISLOOLD as projectNumber,
                    ps.S_POPIS as statusName,
                    p.S_POPIS as description,
                    p.DT_PLATNOSTOD as startDate,
                    p.DT_PLATNOSTDO as endDate
                FROM DB_JT.KP_KTG_PROJEKT p
                LEFT JOIN DB_JT.KP_CIS_PROJEKTSTATUS ps ON p.ID_STATUS = ps.ID
                ORDER BY p.ID DESC
                """;

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

        return rows.stream()
                .map(row -> ProjectSummaryDTO.builder()
                        .id(((Number) row.get("id")).longValue())
                        .name((String) row.get("name"))
                        .projectNumber((String) row.get("projectNumber"))
                        .status((String) row.get("statusName"))
                        .description((String) row.get("description"))
                        .startDate(row.get("startDate") != null
                                ? ((java.sql.Timestamp) row.get("startDate")).toLocalDateTime().toLocalDate()
                                : null)
                        .build())
                .toList();
    }

    /**
     * Get project by ID using direct SQL query.
     *
     * @param projectId Project ID
     * @return Project details or null if not found
     */
    public ProjectSummaryDTO getProjectById(Long projectId) {
        logger.info("Fetching project by ID: {}", projectId);

        String sql = """
                SELECT
                    p.ID as id,
                    p.S_NAZEV as name,
                    p.S_CISLOOLD as projectNumber,
                    p.ID_STATUS as statusId,
                    ps.S_POPIS as statusName,
                    p.S_POPIS as description,
                    p.DT_PLATNOSTOD as startDate,
                    p.DT_PLATNOSTDO as endDate,
                    p.ID_PMANAGER as managerId,
                    p.ID_MNGSEGMENT as managementSegmentId,
                    p.S_MENA as currency
                FROM DB_JT.KP_KTG_PROJEKT p
                LEFT JOIN DB_JT.KP_CIS_PROJEKTSTATUS ps ON p.ID_STATUS = ps.ID
                WHERE p.ID = ?
                """;

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, projectId);

        if (rows.isEmpty()) {
            return null;
        }

        Map<String, Object> row = rows.get(0);

        return ProjectSummaryDTO.builder()
                .id(((Number) row.get("id")).longValue())
                .name((String) row.get("name"))
                .projectNumber((String) row.get("projectNumber"))
                .status((String) row.get("statusName"))
                .description((String) row.get("description"))
                .startDate(row.get("startDate") != null
                        ? ((java.sql.Timestamp) row.get("startDate")).toLocalDateTime().toLocalDate()
                        : null)
                .build();
    }
}
