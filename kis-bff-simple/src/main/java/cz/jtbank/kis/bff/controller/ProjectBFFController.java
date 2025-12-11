package cz.jtbank.kis.bff.controller;

import cz.jtbank.kis.bff.dto.project.*;
import cz.jtbank.kis.bff.service.ProjectService;
import cz.jtbank.kis.bff.service.ProjectAggregationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

/**
 * Project BFF Controller
 *
 * Provides aggregated project endpoints for the frontend.
 * Uses Oracle stored procedures for CUD operations (ProjectService)
 * and views for read operations (ProjectAggregationService)
 */
@RestController
@RequestMapping("/bff/projects")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class ProjectBFFController {

    private static final Logger logger = Logger.getLogger(ProjectBFFController.class.getName());

    private final ProjectService projectService;

    public ProjectBFFController(ProjectService projectService) {
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

        List<ProjectSummaryDTO> projects = projectService.getAllProjects();
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

        ProjectDetailDTO project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }

    /**
     * POST /bff/projects
     *
     * Create new project via Oracle procedure KAP_PROJEKT.p_KpProjekt
     */
    @PostMapping
    public ResponseEntity<ProjectDetailDTO> createProject(@RequestBody ProjectFormData request) {
        logger.info("POST /bff/projects - Creating new project: " + request.getName());

        ProjectDetailDTO project = projectService.createProject(request);
        return ResponseEntity.ok(project);
    }

    /**
     * PUT /bff/projects/{id}
     *
     * Update existing project via Oracle procedure KAP_PROJEKT.p_KpProjekt
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProjectDetailDTO> updateProject(
            @PathVariable Long id,
            @RequestBody ProjectFormData request) {
        logger.info("PUT /bff/projects/" + id);

        ProjectDetailDTO project = projectService.updateProject(id, request);
        return ResponseEntity.ok(project);
    }

    /**
     * DELETE /bff/projects/{id}
     *
     * Delete project via Oracle procedure KAP_PROJEKT.p_KpProjekt
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        logger.info("DELETE /bff/projects/" + id);

        projectService.deleteProject(id);
        return ResponseEntity.ok().build();
    }
}
