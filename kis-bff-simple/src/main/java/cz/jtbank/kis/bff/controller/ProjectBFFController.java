package cz.jtbank.kis.bff.controller;

import cz.jtbank.kis.bff.dto.project.*;
import cz.jtbank.kis.bff.service.ProjectAggregationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

/**
 * Project BFF Controller
 *
 * Provides aggregated project endpoints for the frontend
 */
@RestController
@RequestMapping("/bff/projects")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class ProjectBFFController {

    private static final Logger logger = Logger.getLogger(ProjectBFFController.class.getName());

    private final ProjectAggregationService projectService;

    public ProjectBFFController(ProjectAggregationService projectService) {
        this.projectService = projectService;
    }

    /**
     * GET /bff/projects
     *
     * Get list of projects (summary view)
     */
    @GetMapping
    public ResponseEntity<List<ProjectSummaryDTO>> getProjectList() {
        logger.info("GET /bff/projects");

        List<ProjectSummaryDTO> projects = projectService.getProjectList();
        return ResponseEntity.ok(projects);
    }

    /**
     * GET /bff/projects/{id}
     *
     * Get complete project detail with cash flow entries
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProjectDetailDTO> getProjectDetail(@PathVariable Long id) {
        logger.info("GET /bff/projects/" + id);

        ProjectDetailDTO project = projectService.getProjectDetail(id);
        return ResponseEntity.ok(project);
    }

    /**
     * POST /bff/projects
     *
     * Create new project
     */
    @PostMapping
    public ResponseEntity<ProjectDetailDTO> createProject(@RequestBody ProjectCreateRequestDTO request) {
        logger.info("POST /bff/projects - Creating new project: " + request.getName());

        ProjectDetailDTO project = projectService.createProject(request);
        return ResponseEntity.ok(project);
    }

    /**
     * PUT /bff/projects/{id}
     *
     * Update existing project
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProjectDetailDTO> updateProject(
            @PathVariable Long id,
            @RequestBody ProjectUpdateRequestDTO request) {
        logger.info("PUT /bff/projects/" + id);

        ProjectDetailDTO project = projectService.updateProject(id, request);
        return ResponseEntity.ok(project);
    }

    /**
     * GET /bff/projects/{id}/cash-flow
     *
     * Get project cash flow entries
     */
    @GetMapping("/{id}/cash-flow")
    public ResponseEntity<List<ProjectCashFlowDTO>> getProjectCashFlow(@PathVariable Long id) {
        logger.info("GET /bff/projects/" + id + "/cash-flow");

        List<ProjectCashFlowDTO> cashFlow = projectService.getProjectCashFlow(id);
        return ResponseEntity.ok(cashFlow);
    }
}
