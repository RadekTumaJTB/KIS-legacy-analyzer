package cz.jtbank.kis.bff.service;

import cz.jtbank.kis.bff.dto.project.ProjectDetailDTO;
import cz.jtbank.kis.bff.dto.project.ProjectFormData;
import cz.jtbank.kis.bff.dto.project.ProjectSummaryDTO;
import cz.jtbank.kis.bff.repository.ProjectRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Types;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Project Service - Oracle Stored Procedure Implementation
 *
 * Uses Oracle PL/SQL procedures from KAP_PROJEKT package instead of JPA
 * This ensures business logic, validation, audit logging, and email notifications
 * are handled exactly as in the legacy application
 *
 * Main Procedures:
 * - KAP_PROJEKT.p_KpProjekt() - CRUD operations for projects
 * - KAP_PROJEKT.p_KpProjektCashFlow() - CRUD for cash flow
 * - KAP_PROJEKT.p_KpRelProjektUcSpol() - Project-Company relationships
 */
@Service
@Transactional
public class ProjectService {

    private static final Logger logger = Logger.getLogger(ProjectService.class.getName());

    private final JdbcTemplate jdbcTemplate;
    private final ProjectRepository projectRepository;
    private final ProjectAggregationService aggregationService;

    public ProjectService(
            JdbcTemplate jdbcTemplate,
            ProjectRepository projectRepository,
            ProjectAggregationService aggregationService) {
        this.jdbcTemplate = jdbcTemplate;
        this.projectRepository = projectRepository;
        this.aggregationService = aggregationService;
    }

    /**
     * Create new project using Oracle procedure KAP_PROJEKT.p_KpProjekt
     *
     * @param data Project form data
     * @return Created project with generated ID
     */
    public ProjectDetailDTO createProject(ProjectFormData data) {
        logger.info("Creating project via Oracle procedure: " + data.getName());

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withSchemaName("DB_JT")
                .withCatalogName("KAP_PROJEKT")
                .withProcedureName("p_KpProjekt")
                .declareParameters(
                        new SqlParameter("p_id", Types.NUMERIC),
                        new SqlOutParameter("p_id", Types.NUMERIC),
                        new SqlParameter("p_cisloOld", Types.VARCHAR),
                        new SqlParameter("p_nazev", Types.VARCHAR),
                        new SqlParameter("p_popis", Types.VARCHAR),
                        new SqlParameter("p_status", Types.NUMERIC),
                        new SqlParameter("p_kategorie", Types.NUMERIC),
                        new SqlParameter("p_mngsegment", Types.NUMERIC),
                        new SqlParameter("p_pmanager", Types.NUMERIC),
                        new SqlParameter("p_valuationStartDate", Types.DATE),
                        new SqlParameter("p_valuationEndDate", Types.DATE),
                        new SqlParameter("p_typBilance", Types.NUMERIC),
                        new SqlParameter("p_typBudgetu", Types.NUMERIC),
                        new SqlParameter("p_sledujeB budget", Types.CHAR),
                        new SqlParameter("p_action", Types.VARCHAR)
                );

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("p_id", null)  // NULL for INSERT
                .addValue("p_cisloOld", data.getOldNumber())
                .addValue("p_nazev", data.getName())
                .addValue("p_popis", data.getDescription())
                .addValue("p_status", data.getStatusId())
                .addValue("p_kategorie", data.getCategoryId())
                .addValue("p_mngsegment", data.getManagementSegmentId())
                .addValue("p_pmanager", data.getProjectManagerId())
                .addValue("p_valuationStartDate", data.getValuationStartDate())
                .addValue("p_valuationEndDate", data.getValuationEndDate())
                .addValue("p_typBilance", data.getBalanceTypeId())
                .addValue("p_typBudgetu", data.getBudgetTypeId())
                .addValue("p_sledujebudget", data.getTracksBudget() ? "1" : "0")
                .addValue("p_action", "I");  // I = Insert

        Map<String, Object> result = call.execute(params);

        // Oracle procedure returns generated ID in OUT parameter
        Number newId = (Number) result.get("p_id");
        if (newId == null) {
            throw new RuntimeException("Failed to create project - no ID returned from Oracle procedure");
        }

        logger.info("Project created with ID: " + newId.longValue());

        // The procedure also:
        // - Validates all business rules
        // - Creates audit log entry in KP_LOG_PROJEKT
        // - Sends email notification to project manager
        // - Sets default values for calculated fields

        // Fetch and return the created project
        return aggregationService.getProjectDetail(newId.longValue());
    }

    /**
     * Update existing project using Oracle procedure
     *
     * @param id   Project ID
     * @param data Updated project data
     * @return Updated project
     */
    public ProjectDetailDTO updateProject(Long id, ProjectFormData data) {
        logger.info("Updating project ID " + id + " via Oracle procedure");

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withSchemaName("DB_JT")
                .withCatalogName("KAP_PROJEKT")
                .withProcedureName("p_KpProjekt")
                .declareParameters(
                        new SqlParameter("p_id", Types.NUMERIC),
                        new SqlParameter("p_cisloOld", Types.VARCHAR),
                        new SqlParameter("p_nazev", Types.VARCHAR),
                        new SqlParameter("p_popis", Types.VARCHAR),
                        new SqlParameter("p_status", Types.NUMERIC),
                        new SqlParameter("p_kategorie", Types.NUMERIC),
                        new SqlParameter("p_mngsegment", Types.NUMERIC),
                        new SqlParameter("p_pmanager", Types.NUMERIC),
                        new SqlParameter("p_valuationStartDate", Types.DATE),
                        new SqlParameter("p_valuationEndDate", Types.DATE),
                        new SqlParameter("p_typBilance", Types.NUMERIC),
                        new SqlParameter("p_typBudgetu", Types.NUMERIC),
                        new SqlParameter("p_sledujebudget", Types.CHAR),
                        new SqlParameter("p_action", Types.VARCHAR)
                );

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("p_id", id)
                .addValue("p_cisloOld", data.getOldNumber())
                .addValue("p_nazev", data.getName())
                .addValue("p_popis", data.getDescription())
                .addValue("p_status", data.getStatusId())
                .addValue("p_kategorie", data.getCategoryId())
                .addValue("p_mngsegment", data.getManagementSegmentId())
                .addValue("p_pmanager", data.getProjectManagerId())
                .addValue("p_valuationStartDate", data.getValuationStartDate())
                .addValue("p_valuationEndDate", data.getValuationEndDate())
                .addValue("p_typBilance", data.getBalanceTypeId())
                .addValue("p_typBudgetu", data.getBudgetTypeId())
                .addValue("p_sledujebudget", data.getTracksBudget() ? "1" : "0")
                .addValue("p_action", "U");  // U = Update

        call.execute(params);

        logger.info("Project ID " + id + " updated successfully");

        // The procedure also:
        // - Validates changes against business rules
        // - Creates audit log entry (before/after values)
        // - Sends email if project manager changed
        // - Recalculates dependent fields

        return aggregationService.getProjectDetail(id);
    }

    /**
     * Delete project using Oracle procedure
     *
     * @param id Project ID to delete
     */
    public void deleteProject(Long id) {
        logger.info("Deleting project ID " + id + " via Oracle procedure");

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withSchemaName("DB_JT")
                .withCatalogName("KAP_PROJEKT")
                .withProcedureName("p_KpProjekt")
                .declareParameters(
                        new SqlParameter("p_id", Types.NUMERIC),
                        new SqlParameter("p_action", Types.VARCHAR)
                );

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("p_id", id)
                .addValue("p_action", "D");  // D = Delete

        call.execute(params);

        logger.info("Project ID " + id + " deleted successfully");

        // The procedure also:
        // - Checks if project can be deleted (no dependent records)
        // - Creates audit log entry
        // - Sends notification email
        // - Cascades deletion if configured
    }

    /**
     * Get list of all projects
     * Uses aggregation service which queries views/tables directly
     *
     * Note: For role-based filtering, aggregation service should use
     * VwKtgProjektuserpravaOverviewView1 instead of VwKtgProjektOverviewView1
     *
     * @return List of project summaries
     */
    public List<ProjectSummaryDTO> getAllProjects() {
        return aggregationService.getProjectList();
    }

    /**
     * Get project detail by ID
     *
     * @param id Project ID
     * @return Project detail with cash flow
     */
    public ProjectDetailDTO getProjectById(Long id) {
        return aggregationService.getProjectDetail(id);
    }
}
