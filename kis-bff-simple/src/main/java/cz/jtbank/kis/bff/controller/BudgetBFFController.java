package cz.jtbank.kis.bff.controller;

import cz.jtbank.kis.bff.dto.budget.BudgetCreateRequestDTO;
import cz.jtbank.kis.bff.dto.budget.BudgetUpdateRequestDTO;
import cz.jtbank.kis.bff.dto.budget.BudgetDetailDTO;
import cz.jtbank.kis.bff.dto.budget.BudgetKPIDTO;
import cz.jtbank.kis.bff.dto.budget.BudgetSummaryDTO;
import cz.jtbank.kis.bff.service.BudgetAggregationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

/**
 * Budget BFF Controller
 *
 * Provides aggregated budget endpoints for the frontend
 */
@RestController
@RequestMapping("/bff/budgets")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class BudgetBFFController {

    private static final Logger logger = Logger.getLogger(BudgetBFFController.class.getName());

    private final BudgetAggregationService budgetService;

    public BudgetBFFController(BudgetAggregationService budgetService) {
        this.budgetService = budgetService;
    }

    /**
     * GET /bff/budgets/kpis
     *
     * Get budget KPI metrics for dashboard
     */
    @GetMapping("/kpis")
    public ResponseEntity<BudgetKPIDTO> getBudgetKPIs() {
        logger.info("GET /bff/budgets/kpis");

        BudgetKPIDTO kpis = budgetService.getBudgetKPIs();
        return ResponseEntity.ok(kpis);
    }

    /**
     * GET /bff/budgets
     *
     * Get list of budgets (summary view)
     */
    @GetMapping
    public ResponseEntity<List<BudgetSummaryDTO>> getBudgetList() {
        logger.info("GET /bff/budgets");

        List<BudgetSummaryDTO> budgets = budgetService.getBudgetList();
        return ResponseEntity.ok(budgets);
    }

    /**
     * POST /bff/budgets
     *
     * Create new budget
     */
    @PostMapping
    public ResponseEntity<BudgetDetailDTO> createBudget(@RequestBody BudgetCreateRequestDTO request) {
        logger.info("POST /bff/budgets - Creating new budget: " + request.getCode());

        BudgetDetailDTO budget = budgetService.createBudget(request);
        return ResponseEntity.ok(budget);
    }

    /**
     * GET /bff/budgets/{id}
     *
     * Get complete budget detail with line items
     */
    @GetMapping("/{id}")
    public ResponseEntity<BudgetDetailDTO> getBudgetDetail(@PathVariable Long id) {
        logger.info("GET /bff/budgets/" + id);

        BudgetDetailDTO budget = budgetService.getBudgetDetail(id);
        return ResponseEntity.ok(budget);
    }

    /**
     * PUT /bff/budgets/{id}
     *
     * Update budget
     */
    @PutMapping("/{id}")
    public ResponseEntity<BudgetDetailDTO> updateBudget(
            @PathVariable Long id,
            @RequestBody BudgetUpdateRequestDTO request) {
        logger.info("PUT /bff/budgets/" + id);

        BudgetDetailDTO budget = budgetService.updateBudget(id, request);
        return ResponseEntity.ok(budget);
    }
}
