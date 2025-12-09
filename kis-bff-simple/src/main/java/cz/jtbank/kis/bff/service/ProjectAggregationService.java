package cz.jtbank.kis.bff.service;

import cz.jtbank.kis.bff.dto.project.*;
import cz.jtbank.kis.bff.entity.*;
import cz.jtbank.kis.bff.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Project Aggregation Service
 *
 * Aggregates project data from Oracle database
 * Provides unified project management operations
 */
@Service
public class ProjectAggregationService {

    private static final Logger logger = Logger.getLogger(ProjectAggregationService.class.getName());

    private final ProjectRepository projectRepository;
    private final ProjectCashFlowRepository projectCashFlowRepository;
    private final ProjectProposalRepository projectProposalRepository;
    private final ProjectStatusRepository projectStatusRepository;
    private final AppUserRepository appUserRepository;

    public ProjectAggregationService(
            ProjectRepository projectRepository,
            ProjectCashFlowRepository projectCashFlowRepository,
            ProjectProposalRepository projectProposalRepository,
            ProjectStatusRepository projectStatusRepository,
            AppUserRepository appUserRepository) {
        this.projectRepository = projectRepository;
        this.projectCashFlowRepository = projectCashFlowRepository;
        this.projectProposalRepository = projectProposalRepository;
        this.projectStatusRepository = projectStatusRepository;
        this.appUserRepository = appUserRepository;
    }

    /**
     * Get list of projects (summary view)
     */
    public List<ProjectSummaryDTO> getProjectList() {
        logger.info("Fetching project list from Oracle");

        List<ProjectEntity> projects = projectRepository.findAllByOrderByCreatedAtDesc();
        List<ProjectSummaryDTO> result = new ArrayList<>();

        for (ProjectEntity project : projects) {
            // Fetch related data
            ProjectStatusEntity status = project.getIdStatus() != null
                    ? projectStatusRepository.findById(project.getIdStatus()).orElse(null)
                    : null;

            // Fetch project manager (handle missing AppUser table gracefully)
            AppUserEntity projectManager = null;
            try {
                projectManager = project.getIdProjectManager() != null
                        ? appUserRepository.findById(project.getIdProjectManager()).orElse(null)
                        : null;
            } catch (Exception e) {
                logger.warning("Could not fetch project manager for project " + project.getId() + ": " + e.getMessage());
            }

            result.add(ProjectSummaryDTO.builder()
                    .id(project.getId())
                    .name(project.getName())
                    .projectNumber(project.getProjectNumber())
                    .status(status != null ? status.getName() : "N/A")
                    .statusCode(status != null ? status.getCode() : "UNKNOWN")
                    .projectManagerName(projectManager != null ? projectManager.getJmeno() : "N/A")
                    .managementSegmentName("N/A") // TODO: Join with management segment table
                    .startDate(project.getCreatedAt() != null ? project.getCreatedAt().toLocalDate() : null)
                    .valuationStartDate(project.getValuationStartDate())
                    .description(project.getDescription())
                    .build());
        }

        logger.info("Found " + result.size() + " projects");
        return result;
    }

    /**
     * Get complete project detail with cash flow entries
     */
    public ProjectDetailDTO getProjectDetail(Long id) {
        logger.info("Fetching project detail for ID: " + id + " from Oracle");

        ProjectEntity project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found: " + id));

        // Fetch related data
        ProjectStatusEntity status = project.getIdStatus() != null
                ? projectStatusRepository.findById(project.getIdStatus()).orElse(null)
                : null;

        // Fetch project manager (handle missing AppUser table gracefully)
        AppUserEntity projectManager = null;
        try {
            projectManager = project.getIdProjectManager() != null
                    ? appUserRepository.findById(project.getIdProjectManager()).orElse(null)
                    : null;
        } catch (Exception e) {
            logger.warning("Could not fetch project manager for project " + project.getId() + ": " + e.getMessage());
        }

        // Fetch cash flow entries
        List<ProjectCashFlowEntity> cashFlowEntities = projectCashFlowRepository.findByIdProjectOrderByDateDesc(id);
        List<ProjectCashFlowDTO> cashFlowList = new ArrayList<>();

        for (ProjectCashFlowEntity cashFlow : cashFlowEntities) {
            cashFlowList.add(ProjectCashFlowDTO.builder()
                    .id(cashFlow.getId())
                    .idProject(cashFlow.getIdProject())
                    .cashFlowTypeName("N/A") // TODO: Join with cash flow type table
                    .date(cashFlow.getDate())
                    .amount(cashFlow.getAmount())
                    .currencyCode("N/A") // TODO: Join with currency table
                    .inOutTypeName("N/A") // TODO: Join with in/out type table
                    .positionTypeName("N/A") // TODO: Join with position type table
                    .notes(cashFlow.getNotes())
                    .build());
        }

        return ProjectDetailDTO.builder()
                .id(project.getId())
                .name(project.getName())
                .projectNumber(project.getProjectNumber())
                .status(status != null ? status.getName() : "N/A")
                .statusCode(status != null ? status.getCode() : "UNKNOWN")
                .projectManagerName(projectManager != null ? projectManager.getJmeno() : "N/A")
                .idProjectManager(project.getIdProjectManager())
                .managementSegmentName("N/A") // TODO: Join with management segment table
                .idManagementSegment(project.getIdManagementSegment())
                .currencyCode("N/A") // TODO: Join with currency table
                .idCurrency(project.getIdCurrency())
                .valuationStartDate(project.getValuationStartDate())
                .frequencyName("N/A") // TODO: Join with frequency table
                .idFrequency(project.getIdFrequency())
                .description(project.getDescription())
                .approvalLevel1Amount(project.getApprovalLevel1Amount())
                .approvalLevel2Amount(project.getApprovalLevel2Amount())
                .approvalLevel3Amount(project.getApprovalLevel3Amount())
                .budgetTrackingFlag(project.getBudgetTrackingFlag())
                .budgetIncreaseAmountPM(project.getBudgetIncreaseAmountPM())
                .budgetIncreaseAmountTop(project.getBudgetIncreaseAmountTop())
                .balanceTypeName("N/A") // TODO: Join with balance type table
                .idBalanceType(project.getIdBalanceType())
                .budgetTypeName("N/A") // TODO: Join with budget type table
                .idBudgetType(project.getIdBudgetType())
                .categoryName("N/A") // TODO: Join with category table
                .idCategory(project.getIdCategory())
                .createdAt(project.getCreatedAt())
                .createdBy(project.getCreatedBy())
                .updatedAt(project.getUpdatedAt())
                .cashFlowList(cashFlowList)
                .build();
    }

    /**
     * Create new project
     */
    @Transactional
    public ProjectDetailDTO createProject(ProjectCreateRequestDTO request) {
        logger.info("Creating new project: " + request.getName());

        ProjectEntity project = new ProjectEntity();
        project.setName(request.getName());
        project.setProjectNumber(request.getProjectNumber());
        project.setIdStatus(request.getIdStatus());
        project.setIdProjectManager(request.getIdProjectManager());
        project.setIdManagementSegment(request.getIdManagementSegment());
        project.setIdCurrency(request.getIdCurrency());
        project.setValuationStartDate(request.getValuationStartDate());
        project.setIdFrequency(request.getIdFrequency());
        project.setDescription(request.getDescription());
        project.setApprovalLevel1Amount(request.getApprovalLevel1Amount());
        project.setApprovalLevel2Amount(request.getApprovalLevel2Amount());
        project.setApprovalLevel3Amount(request.getApprovalLevel3Amount());
        project.setBudgetTrackingFlag(request.getBudgetTrackingFlag());
        project.setBudgetIncreaseAmountPM(request.getBudgetIncreaseAmountPM());
        project.setBudgetIncreaseAmountTop(request.getBudgetIncreaseAmountTop());
        project.setIdBalanceType(request.getIdBalanceType());
        project.setIdBudgetType(request.getIdBudgetType());
        project.setIdCategory(request.getIdCategory());
        project.setCreatedAt(LocalDateTime.now());
        project.setCreatedBy("SYSTEM"); // TODO: Get from security context
        project.setUpdatedAt(LocalDateTime.now());

        ProjectEntity savedProject = projectRepository.save(project);

        logger.info("Created project with ID: " + savedProject.getId());

        return getProjectDetail(savedProject.getId());
    }

    /**
     * Update existing project
     */
    @Transactional
    public ProjectDetailDTO updateProject(Long id, ProjectUpdateRequestDTO request) {
        logger.info("Updating project: " + id);

        ProjectEntity project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found: " + id));

        // Update fields if provided
        if (request.getName() != null) {
            project.setName(request.getName());
        }
        if (request.getProjectNumber() != null) {
            project.setProjectNumber(request.getProjectNumber());
        }
        if (request.getIdStatus() != null) {
            project.setIdStatus(request.getIdStatus());
        }
        if (request.getIdProjectManager() != null) {
            project.setIdProjectManager(request.getIdProjectManager());
        }
        if (request.getIdManagementSegment() != null) {
            project.setIdManagementSegment(request.getIdManagementSegment());
        }
        if (request.getIdCurrency() != null) {
            project.setIdCurrency(request.getIdCurrency());
        }
        if (request.getValuationStartDate() != null) {
            project.setValuationStartDate(request.getValuationStartDate());
        }
        if (request.getIdFrequency() != null) {
            project.setIdFrequency(request.getIdFrequency());
        }
        if (request.getDescription() != null) {
            project.setDescription(request.getDescription());
        }
        if (request.getApprovalLevel1Amount() != null) {
            project.setApprovalLevel1Amount(request.getApprovalLevel1Amount());
        }
        if (request.getApprovalLevel2Amount() != null) {
            project.setApprovalLevel2Amount(request.getApprovalLevel2Amount());
        }
        if (request.getApprovalLevel3Amount() != null) {
            project.setApprovalLevel3Amount(request.getApprovalLevel3Amount());
        }
        if (request.getBudgetTrackingFlag() != null) {
            project.setBudgetTrackingFlag(request.getBudgetTrackingFlag());
        }
        if (request.getBudgetIncreaseAmountPM() != null) {
            project.setBudgetIncreaseAmountPM(request.getBudgetIncreaseAmountPM());
        }
        if (request.getBudgetIncreaseAmountTop() != null) {
            project.setBudgetIncreaseAmountTop(request.getBudgetIncreaseAmountTop());
        }
        if (request.getIdBalanceType() != null) {
            project.setIdBalanceType(request.getIdBalanceType());
        }
        if (request.getIdBudgetType() != null) {
            project.setIdBudgetType(request.getIdBudgetType());
        }
        if (request.getIdCategory() != null) {
            project.setIdCategory(request.getIdCategory());
        }

        project.setUpdatedAt(LocalDateTime.now());

        projectRepository.save(project);

        logger.info("Updated project: " + id);

        return getProjectDetail(id);
    }

    /**
     * Get project cash flow entries
     */
    public List<ProjectCashFlowDTO> getProjectCashFlow(Long projectId) {
        logger.info("Fetching cash flow for project: " + projectId);

        // Verify project exists
        projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        List<ProjectCashFlowEntity> cashFlowEntities = projectCashFlowRepository.findByIdProjectOrderByDateDesc(projectId);
        List<ProjectCashFlowDTO> result = new ArrayList<>();

        for (ProjectCashFlowEntity cashFlow : cashFlowEntities) {
            result.add(ProjectCashFlowDTO.builder()
                    .id(cashFlow.getId())
                    .idProject(cashFlow.getIdProject())
                    .cashFlowTypeName("N/A") // TODO: Join with cash flow type table
                    .date(cashFlow.getDate())
                    .amount(cashFlow.getAmount())
                    .currencyCode("N/A") // TODO: Join with currency table
                    .inOutTypeName("N/A") // TODO: Join with in/out type table
                    .positionTypeName("N/A") // TODO: Join with position type table
                    .notes(cashFlow.getNotes())
                    .build());
        }

        logger.info("Found " + result.size() + " cash flow entries");
        return result;
    }

    // ========== MOCK DATA METHODS ==========

    /**
     * Create mock project list for testing
     */
    private List<ProjectSummaryDTO> createMockProjectList() {
        List<ProjectSummaryDTO> projects = new ArrayList<>();

        // Project 1: CRM Implementation - Active
        projects.add(ProjectSummaryDTO.builder()
                .id(1L)
                .name("Implementace CRM systému")
                .projectNumber("PRJ-2025-001")
                .status("Aktivní")
                .statusCode("ACTIVE")
                .projectManagerName("Jan Novák")
                .managementSegmentName("IT oddělení")
                .startDate(LocalDate.of(2025, 1, 15))
                .valuationStartDate(LocalDate.of(2025, 1, 15))
                .description("Implementace nového CRM systému pro zlepšení vztahů se zákazníky")
                .build());

        // Project 2: IT Infrastructure Modernization - Active
        projects.add(ProjectSummaryDTO.builder()
                .id(2L)
                .name("Modernizace IT infrastruktury")
                .projectNumber("PRJ-2025-002")
                .status("Aktivní")
                .statusCode("ACTIVE")
                .projectManagerName("Eva Černá")
                .managementSegmentName("IT oddělení")
                .startDate(LocalDate.of(2025, 2, 1))
                .valuationStartDate(LocalDate.of(2025, 2, 1))
                .description("Upgrade serverů a síťové infrastruktury")
                .build());

        // Project 3: Process Digitalization - In Planning
        projects.add(ProjectSummaryDTO.builder()
                .id(3L)
                .name("Digitalizace procesů")
                .projectNumber("PRJ-2025-003")
                .status("V přípravě")
                .statusCode("IN_PLANNING")
                .projectManagerName("Martin Dvořák")
                .managementSegmentName("Business Development")
                .startDate(LocalDate.of(2025, 3, 1))
                .valuationStartDate(LocalDate.of(2025, 3, 1))
                .description("Digitalizace klíčových obchodních procesů")
                .build());

        return projects;
    }

    /**
     * Create mock project detail for testing
     */
    private ProjectDetailDTO createMockProjectDetail(Long id) {
        // Create mock cash flow entries
        List<ProjectCashFlowDTO> cashFlowList = createMockCashFlowList(id);

        // Return different project based on ID
        if (id == 1L) {
            return ProjectDetailDTO.builder()
                    .id(1L)
                    .name("Implementace CRM systému")
                    .projectNumber("PRJ-2025-001")
                    .status("Aktivní")
                    .statusCode("ACTIVE")
                    .projectManagerName("Jan Novák")
                    .idProjectManager(101L)
                    .managementSegmentName("IT oddělení")
                    .idManagementSegment(1L)
                    .currencyCode("CZK")
                    .idCurrency(1L)
                    .valuationStartDate(LocalDate.of(2025, 1, 15))
                    .frequencyName("Měsíční")
                    .idFrequency(1L)
                    .description("Implementace nového CRM systému pro zlepšení vztahů se zákazníky. Projekt zahrnuje analýzu požadavků, výběr dodavatele, implementaci a školení uživatelů.")
                    .approvalLevel1Amount(new BigDecimal("500000"))
                    .approvalLevel2Amount(new BigDecimal("1000000"))
                    .approvalLevel3Amount(new BigDecimal("2000000"))
                    .budgetTrackingFlag("A")
                    .budgetIncreaseAmountPM(new BigDecimal("100000"))
                    .budgetIncreaseAmountTop(new BigDecimal("200000"))
                    .balanceTypeName("Aktivní")
                    .idBalanceType(1L)
                    .budgetTypeName("CAPEX")
                    .idBudgetType(1L)
                    .categoryName("IT Projekty")
                    .idCategory(1L)
                    .createdAt(LocalDateTime.of(2024, 12, 1, 10, 0))
                    .createdBy("admin")
                    .updatedAt(LocalDateTime.of(2025, 12, 5, 15, 30))
                    .cashFlowList(cashFlowList)
                    .build();
        } else if (id == 2L) {
            return ProjectDetailDTO.builder()
                    .id(2L)
                    .name("Modernizace IT infrastruktury")
                    .projectNumber("PRJ-2025-002")
                    .status("Aktivní")
                    .statusCode("ACTIVE")
                    .projectManagerName("Eva Černá")
                    .idProjectManager(102L)
                    .managementSegmentName("IT oddělení")
                    .idManagementSegment(1L)
                    .currencyCode("CZK")
                    .idCurrency(1L)
                    .valuationStartDate(LocalDate.of(2025, 2, 1))
                    .frequencyName("Měsíční")
                    .idFrequency(1L)
                    .description("Upgrade serverů, síťové infrastruktury a migrace do cloudu pro zvýšení výkonu a dostupnosti systémů.")
                    .approvalLevel1Amount(new BigDecimal("750000"))
                    .approvalLevel2Amount(new BigDecimal("1500000"))
                    .approvalLevel3Amount(new BigDecimal("3000000"))
                    .budgetTrackingFlag("A")
                    .budgetIncreaseAmountPM(new BigDecimal("150000"))
                    .budgetIncreaseAmountTop(new BigDecimal("300000"))
                    .balanceTypeName("Aktivní")
                    .idBalanceType(1L)
                    .budgetTypeName("CAPEX")
                    .idBudgetType(1L)
                    .categoryName("IT Projekty")
                    .idCategory(1L)
                    .createdAt(LocalDateTime.of(2024, 12, 10, 11, 0))
                    .createdBy("admin")
                    .updatedAt(LocalDateTime.of(2025, 12, 4, 14, 20))
                    .cashFlowList(cashFlowList)
                    .build();
        } else if (id == 3L) {
            return ProjectDetailDTO.builder()
                    .id(3L)
                    .name("Digitalizace procesů")
                    .projectNumber("PRJ-2025-003")
                    .status("V přípravě")
                    .statusCode("IN_PLANNING")
                    .projectManagerName("Martin Dvořák")
                    .idProjectManager(103L)
                    .managementSegmentName("Business Development")
                    .idManagementSegment(2L)
                    .currencyCode("CZK")
                    .idCurrency(1L)
                    .valuationStartDate(LocalDate.of(2025, 3, 1))
                    .frequencyName("Měsíční")
                    .idFrequency(1L)
                    .description("Digitalizace klíčových obchodních procesů včetně automatizace workflow a integrace systémů.")
                    .approvalLevel1Amount(new BigDecimal("300000"))
                    .approvalLevel2Amount(new BigDecimal("600000"))
                    .approvalLevel3Amount(new BigDecimal("1200000"))
                    .budgetTrackingFlag("A")
                    .budgetIncreaseAmountPM(new BigDecimal("50000"))
                    .budgetIncreaseAmountTop(new BigDecimal("100000"))
                    .balanceTypeName("Aktivní")
                    .idBalanceType(1L)
                    .budgetTypeName("OPEX")
                    .idBudgetType(2L)
                    .categoryName("Business Transformation")
                    .idCategory(2L)
                    .createdAt(LocalDateTime.of(2024, 12, 15, 9, 0))
                    .createdBy("admin")
                    .updatedAt(LocalDateTime.of(2025, 12, 3, 16, 45))
                    .cashFlowList(cashFlowList)
                    .build();
        } else {
            // Default project for other IDs
            return ProjectDetailDTO.builder()
                    .id(id)
                    .name("Testovací projekt " + id)
                    .projectNumber("PRJ-2025-" + String.format("%03d", id))
                    .status("Aktivní")
                    .statusCode("ACTIVE")
                    .projectManagerName("Test Manager")
                    .idProjectManager(100L)
                    .managementSegmentName("Test Department")
                    .idManagementSegment(99L)
                    .currencyCode("CZK")
                    .idCurrency(1L)
                    .valuationStartDate(LocalDate.now())
                    .frequencyName("Měsíční")
                    .idFrequency(1L)
                    .description("Testovací projekt pro ID " + id)
                    .approvalLevel1Amount(new BigDecimal("100000"))
                    .approvalLevel2Amount(new BigDecimal("200000"))
                    .approvalLevel3Amount(new BigDecimal("500000"))
                    .budgetTrackingFlag("A")
                    .budgetIncreaseAmountPM(new BigDecimal("50000"))
                    .budgetIncreaseAmountTop(new BigDecimal("100000"))
                    .balanceTypeName("Aktivní")
                    .idBalanceType(1L)
                    .budgetTypeName("OPEX")
                    .idBudgetType(2L)
                    .categoryName("Různé")
                    .idCategory(99L)
                    .createdAt(LocalDateTime.now().minusDays(30))
                    .createdBy("system")
                    .updatedAt(LocalDateTime.now())
                    .cashFlowList(cashFlowList)
                    .build();
        }
    }

    /**
     * Create mock cash flow list for testing
     */
    private List<ProjectCashFlowDTO> createMockCashFlowList(Long projectId) {
        List<ProjectCashFlowDTO> cashFlows = new ArrayList<>();

        // Add some sample cash flow entries
        cashFlows.add(ProjectCashFlowDTO.builder()
                .id(1L)
                .idProject(projectId)
                .cashFlowTypeName("Investice")
                .date(LocalDate.of(2025, 1, 15))
                .amount(new BigDecimal("500000"))
                .currencyCode("CZK")
                .inOutTypeName("Výdaj")
                .positionTypeName("CAPEX")
                .notes("Počáteční investice do projektu")
                .build());

        cashFlows.add(ProjectCashFlowDTO.builder()
                .id(2L)
                .idProject(projectId)
                .cashFlowTypeName("Provozní náklady")
                .date(LocalDate.of(2025, 2, 15))
                .amount(new BigDecimal("150000"))
                .currencyCode("CZK")
                .inOutTypeName("Výdaj")
                .positionTypeName("OPEX")
                .notes("Měsíční provozní náklady")
                .build());

        cashFlows.add(ProjectCashFlowDTO.builder()
                .id(3L)
                .idProject(projectId)
                .cashFlowTypeName("Licence")
                .date(LocalDate.of(2025, 3, 1))
                .amount(new BigDecimal("200000"))
                .currencyCode("CZK")
                .inOutTypeName("Výdaj")
                .positionTypeName("OPEX")
                .notes("Roční licence software")
                .build());

        return cashFlows;
    }
}
