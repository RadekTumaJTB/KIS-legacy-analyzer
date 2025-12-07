package cz.jtbank.kis.bff.service;

import cz.jtbank.kis.bff.dto.budget.*;
import cz.jtbank.kis.bff.dto.document.UserSummaryDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Budget Aggregation Service
 *
 * Aggregates budget data from multiple backend services
 *
 * TODO: Replace mock data with actual Feign client calls
 * when core backend endpoints are available
 */
@Service
public class BudgetAggregationService {

    private static final Logger logger = Logger.getLogger(BudgetAggregationService.class.getName());

    private static final String[] MONTH_NAMES_CZ = {
            "Leden", "Únor", "Březen", "Duben", "Květen", "Červen",
            "Červenec", "Srpen", "Září", "Říjen", "Listopad", "Prosinec"
    };

    /**
     * Get budget KPIs for dashboard
     */
    public BudgetKPIDTO getBudgetKPIs() {
        logger.info("Fetching budget KPIs");

        // TODO: Replace with actual backend call
        return createMockKPIs();
    }

    /**
     * Get list of budgets (summary view)
     */
    public List<BudgetSummaryDTO> getBudgetList() {
        logger.info("Fetching budget list");

        // TODO: Replace with actual backend call
        return createMockBudgetList();
    }

    /**
     * Get complete budget detail with line items
     */
    public BudgetDetailDTO getBudgetDetail(Long id) {
        logger.info("Fetching budget detail for ID: " + id);

        // TODO: Replace with actual backend call
        return createMockBudgetDetail(id);
    }

    /**
     * Update budget
     */
    public BudgetDetailDTO updateBudget(Long id, BudgetUpdateRequestDTO request) {
        logger.info("Updating budget: " + id);

        // TODO: Call actual backend update endpoint
        // For now, get current budget and update fields

        BudgetDetailDTO current = getBudgetDetail(id);

        return BudgetDetailDTO.builder()
                .id(current.getId())
                .code(current.getCode())
                .name(request.getName() != null ? request.getName() : current.getName())
                .description(request.getDescription() != null ? request.getDescription() : current.getDescription())
                .type(current.getType())
                .year(current.getYear())
                .status(current.getStatus())
                .totalPlanned(request.getPlannedAmount() != null ? request.getPlannedAmount() : current.getTotalPlanned())
                .totalActual(current.getTotalActual())
                .totalVariance(current.getTotalVariance())
                .utilizationPercent(current.getUtilizationPercent())
                .departmentName(current.getDepartmentName())
                .owner(current.getOwner())
                .validFrom(current.getValidFrom())
                .validTo(current.getValidTo())
                .lineItems(current.getLineItems())
                .notes(request.getDescription())
                .createdAt(current.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * Create new budget
     */
    public BudgetDetailDTO createBudget(BudgetCreateRequestDTO request) {
        logger.info("Creating new budget: " + request.getCode());

        // TODO: Call actual backend creation endpoint
        // For now, generate mock budget with new ID

        Long newId = System.currentTimeMillis(); // Generate unique ID
        LocalDate validFrom = LocalDate.of(request.getYear(), 1, 1);
        LocalDate validTo = LocalDate.of(request.getYear(), 12, 31);

        UserSummaryDTO owner = UserSummaryDTO.builder()
                .id(1L)
                .name("Eva Černá")
                .email("eva.cerna@jtbank.cz")
                .position("CFO")
                .build();

        // Create empty line items for new budget
        List<BudgetLineItemDTO> lineItems = List.of();

        return BudgetDetailDTO.builder()
                .id(newId)
                .code(request.getCode())
                .name(request.getName())
                .description(request.getDescription() != null ? request.getDescription() : "")
                .type(request.getType())
                .year(request.getYear())
                .status("DRAFT")
                .totalPlanned(request.getPlannedAmount())
                .totalActual(BigDecimal.ZERO)
                .totalVariance(BigDecimal.ZERO)
                .utilizationPercent(0.0)
                .departmentName(request.getDepartmentName())
                .owner(owner)
                .validFrom(validFrom)
                .validTo(validTo)
                .lineItems(lineItems)
                .notes(request.getDescription())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    // ========== MOCK DATA METHODS ==========

    private BudgetKPIDTO createMockKPIs() {
        return BudgetKPIDTO.builder()
                .totalPlanned(new BigDecimal("12500000.00"))
                .totalActual(new BigDecimal("10850000.00"))
                .totalVariance(new BigDecimal("-1650000.00"))
                .averageUtilization(86.8)
                .totalBudgets(8)
                .activeBudgets(6)
                .overBudgetCount(2)
                .underBudgetCount(4)
                .largestVariance(new BigDecimal("-850000.00"))
                .largestVarianceBudgetName("Marketing 2025")
                .build();
    }

    private List<BudgetSummaryDTO> createMockBudgetList() {
        List<BudgetSummaryDTO> budgets = new ArrayList<>();

        // Budget 1: IT Department - Over budget
        budgets.add(BudgetSummaryDTO.builder()
                .id(1L)
                .code("BUD-2025-001")
                .name("IT Infrastruktura 2025")
                .type("EXPENSE")
                .year(2025)
                .status("ACTIVE")
                .plannedAmount(new BigDecimal("2500000.00"))
                .actualAmount(new BigDecimal("2750000.00"))
                .variance(new BigDecimal("250000.00"))
                .utilizationPercent(110.0)
                .departmentName("IT")
                .ownerName("Tomáš Novák")
                .validFrom(LocalDate.of(2025, 1, 1))
                .validTo(LocalDate.of(2025, 12, 31))
                .build());

        // Budget 2: Marketing - Under budget
        budgets.add(BudgetSummaryDTO.builder()
                .id(2L)
                .code("BUD-2025-002")
                .name("Marketing 2025")
                .type("EXPENSE")
                .year(2025)
                .status("ACTIVE")
                .plannedAmount(new BigDecimal("1800000.00"))
                .actualAmount(new BigDecimal("950000.00"))
                .variance(new BigDecimal("-850000.00"))
                .utilizationPercent(52.8)
                .departmentName("Marketing")
                .ownerName("Eva Černá")
                .validFrom(LocalDate.of(2025, 1, 1))
                .validTo(LocalDate.of(2025, 12, 31))
                .build());

        // Budget 3: Revenue
        budgets.add(BudgetSummaryDTO.builder()
                .id(3L)
                .code("BUD-2025-003")
                .name("Tržby - Retail Banking")
                .type("REVENUE")
                .year(2025)
                .status("ACTIVE")
                .plannedAmount(new BigDecimal("5000000.00"))
                .actualAmount(new BigDecimal("4850000.00"))
                .variance(new BigDecimal("-150000.00"))
                .utilizationPercent(97.0)
                .departmentName("Retail Banking")
                .ownerName("Martin Novák")
                .validFrom(LocalDate.of(2025, 1, 1))
                .validTo(LocalDate.of(2025, 12, 31))
                .build());

        // Budget 4: HR
        budgets.add(BudgetSummaryDTO.builder()
                .id(4L)
                .code("BUD-2025-004")
                .name("HR & Recruitment 2025")
                .type("EXPENSE")
                .year(2025)
                .status("ACTIVE")
                .plannedAmount(new BigDecimal("1200000.00"))
                .actualAmount(new BigDecimal("1050000.00"))
                .variance(new BigDecimal("-150000.00"))
                .utilizationPercent(87.5)
                .departmentName("HR")
                .ownerName("Petra Svobodová")
                .validFrom(LocalDate.of(2025, 1, 1))
                .validTo(LocalDate.of(2025, 12, 31))
                .build());

        // Budget 5: CAPEX - Office equipment
        budgets.add(BudgetSummaryDTO.builder()
                .id(5L)
                .code("BUD-2025-005")
                .name("Kancelářské vybavení")
                .type("CAPEX")
                .year(2025)
                .status("ACTIVE")
                .plannedAmount(new BigDecimal("800000.00"))
                .actualAmount(new BigDecimal("650000.00"))
                .variance(new BigDecimal("-150000.00"))
                .utilizationPercent(81.3)
                .departmentName("Facilities")
                .ownerName("Jan Dvořák")
                .validFrom(LocalDate.of(2025, 1, 1))
                .validTo(LocalDate.of(2025, 12, 31))
                .build());

        // Budget 6: Draft budget
        budgets.add(BudgetSummaryDTO.builder()
                .id(6L)
                .code("BUD-2026-001")
                .name("IT Infrastruktura 2026")
                .type("EXPENSE")
                .year(2026)
                .status("DRAFT")
                .plannedAmount(new BigDecimal("3000000.00"))
                .actualAmount(BigDecimal.ZERO)
                .variance(BigDecimal.ZERO)
                .utilizationPercent(0.0)
                .departmentName("IT")
                .ownerName("Tomáš Novák")
                .validFrom(LocalDate.of(2026, 1, 1))
                .validTo(LocalDate.of(2026, 12, 31))
                .build());

        return budgets;
    }

    private BudgetDetailDTO createMockBudgetDetail(Long id) {
        // Create monthly line items
        List<BudgetLineItemDTO> lineItems = createMockLineItems(id);

        // Calculate totals
        BigDecimal totalPlanned = lineItems.stream()
                .map(BudgetLineItemDTO::getPlannedAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalActual = lineItems.stream()
                .map(BudgetLineItemDTO::getActualAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalVariance = totalActual.subtract(totalPlanned);

        double utilization = totalPlanned.compareTo(BigDecimal.ZERO) > 0
                ? totalActual.divide(totalPlanned, 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100"))
                        .doubleValue()
                : 0.0;

        return BudgetDetailDTO.builder()
                .id(id)
                .code("BUD-2025-" + String.format("%03d", id))
                .name("IT Infrastruktura 2025")
                .description("Roční rozpočet pro IT infrastrukturu včetně hardware, software a cloudových služeb")
                .type("EXPENSE")
                .year(2025)
                .status("ACTIVE")
                .totalPlanned(totalPlanned)
                .totalActual(totalActual)
                .totalVariance(totalVariance)
                .utilizationPercent(utilization)
                .departmentName("IT")
                .owner(UserSummaryDTO.builder()
                        .id(3L)
                        .name("Tomáš Novák")
                        .email("tomas.novak@jtbank.cz")
                        .position("IT Manager")
                        .build())
                .validFrom(LocalDate.of(2025, 1, 1))
                .validTo(LocalDate.of(2025, 12, 31))
                .createdAt(LocalDateTime.of(2024, 11, 1, 10, 0))
                .updatedAt(LocalDateTime.of(2025, 12, 1, 15, 30))
                .lineItems(lineItems)
                .notes("Budget schválen CFO dne 2024-11-15. Očekává se zvýšená potřeba v Q4 kvůli upgradu serverů.")
                .build();
    }

    private List<BudgetLineItemDTO> createMockLineItems(Long budgetId) {
        List<BudgetLineItemDTO> items = new ArrayList<>();

        // Mock data: IT budget with seasonal variations
        BigDecimal[] plannedAmounts = {
                new BigDecimal("200000"), new BigDecimal("200000"), new BigDecimal("200000"),
                new BigDecimal("200000"), new BigDecimal("210000"), new BigDecimal("210000"),
                new BigDecimal("220000"), new BigDecimal("220000"), new BigDecimal("230000"),
                new BigDecimal("240000"), new BigDecimal("270000"), new BigDecimal("300000")
        };

        BigDecimal[] actualAmounts = {
                new BigDecimal("195000"), new BigDecimal("205000"), new BigDecimal("198000"),
                new BigDecimal("210000"), new BigDecimal("215000"), new BigDecimal("220000"),
                new BigDecimal("225000"), new BigDecimal("230000"), new BigDecimal("245000"),
                new BigDecimal("260000"), new BigDecimal("290000"), new BigDecimal("0") // December not yet spent
        };

        for (int month = 1; month <= 12; month++) {
            BigDecimal planned = plannedAmounts[month - 1];
            BigDecimal actual = actualAmounts[month - 1];
            BigDecimal variance = actual.subtract(planned);

            double utilization = planned.compareTo(BigDecimal.ZERO) > 0
                    ? actual.divide(planned, 4, RoundingMode.HALF_UP)
                            .multiply(new BigDecimal("100"))
                            .doubleValue()
                    : 0.0;

            String status;
            if (month == 12) {
                status = "PENDING"; // December not yet reached
            } else if (utilization > 105) {
                status = "OVER_BUDGET";
            } else if (utilization < 90) {
                status = "UNDER_BUDGET";
            } else if (utilization > 95 && utilization <= 105) {
                status = "ON_TRACK";
            } else {
                status = "WARNING";
            }

            items.add(BudgetLineItemDTO.builder()
                    .id((long) month)
                    .month(month)
                    .monthName(MONTH_NAMES_CZ[month - 1])
                    .plannedAmount(planned)
                    .actualAmount(actual)
                    .variance(variance)
                    .utilizationPercent(utilization)
                    .status(status)
                    .notes(month == 11 ? "Zvýšené náklady kvůli Black Friday kampaním" : "")
                    .build());
        }

        return items;
    }
}
