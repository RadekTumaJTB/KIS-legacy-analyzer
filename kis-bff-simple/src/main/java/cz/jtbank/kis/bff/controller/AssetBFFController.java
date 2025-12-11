package cz.jtbank.kis.bff.controller;

import cz.jtbank.kis.bff.dto.asset.*;
import cz.jtbank.kis.bff.service.AssetAggregationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * BFF Controller for Assets Module (Majetkové Účasti)
 *
 * Endpoints:
 * - GET /bff/assets/companies - Get companies with role-based filtering
 * - GET /bff/assets/companies/{companyId}/participations - Get equity stakes for company
 * - GET /bff/assets/participations/{id} - Get specific equity stake
 * - POST /bff/assets/participations - Create new equity stake
 * - PUT /bff/assets/participations/{id} - Update equity stake
 * - DELETE /bff/assets/participations/{id} - Delete equity stake
 * - GET /bff/assets/overview - Get asset overview with calculations
 * - GET /bff/assets/controls - Get control rules
 * - POST /bff/assets/controls - Create control rule
 * - PUT /bff/assets/controls/{id} - Update control rule
 * - DELETE /bff/assets/controls/{id} - Delete control rule
 * - GET /bff/assets/export - Export assets to Excel
 */
@RestController
@RequestMapping("/bff/assets")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class AssetBFFController {

    private final AssetAggregationService assetService;

    public AssetBFFController(AssetAggregationService assetService) {
        this.assetService = assetService;
    }

    // ============================================================================
    // Companies
    // ============================================================================

    /**
     * Get companies with role-based filtering
     * Returns companies based on user permissions (Admin_MU, MU_jednotlive, etc.)
     */
    @GetMapping("/companies")
    public ResponseEntity<List<CompanyWithPermissionsDTO>> getCompaniesWithRoleFiltering() {
        List<CompanyWithPermissionsDTO> companies = assetService.getCompaniesWithRoleFiltering();
        return ResponseEntity.ok(companies);
    }

    // ============================================================================
    // Equity Stakes (Majetkové Účasti)
    // ============================================================================

    /**
     * Get equity stakes for a specific company
     */
    @GetMapping("/companies/{companyId}/participations")
    public ResponseEntity<List<EquityStakeDTO>> getEquityStakesForCompany(
            @PathVariable Long companyId) {
        List<EquityStakeDTO> stakes = assetService.getEquityStakesForCompany(companyId);
        return ResponseEntity.ok(stakes);
    }

    /**
     * Get single equity stake by ID
     */
    @GetMapping("/participations/{id}")
    public ResponseEntity<EquityStakeDTO> getEquityStakeById(@PathVariable Long id) {
        EquityStakeDTO stake = assetService.getEquityStakeById(id);
        return ResponseEntity.ok(stake);
    }

    /**
     * Create new equity stake
     */
    @PostMapping("/participations")
    public ResponseEntity<EquityStakeDTO> createEquityStake(@RequestBody EquityStakeDTO dto) {
        EquityStakeDTO created = assetService.createEquityStake(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Update existing equity stake
     */
    @PutMapping("/participations/{id}")
    public ResponseEntity<EquityStakeDTO> updateEquityStake(
            @PathVariable Long id,
            @RequestBody EquityStakeDTO dto) {
        EquityStakeDTO updated = assetService.updateEquityStake(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete equity stake
     */
    @DeleteMapping("/participations/{id}")
    public ResponseEntity<Void> deleteEquityStake(@PathVariable Long id) {
        assetService.deleteEquityStake(id);
        return ResponseEntity.noContent().build();
    }

    // ============================================================================
    // Asset Overview
    // ============================================================================

    /**
     * Get asset overview with calculations
     *
     * @param asOfDate Snapshot date (YYYY-MM-DD), defaults to today
     * @param companyId Filter by company (optional)
     * @return List of asset overviews with ownership percentages and gain/loss
     */
    @GetMapping("/overview")
    public ResponseEntity<List<AssetOverviewDTO>> getAssetOverview(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate asOfDate,
            @RequestParam(required = false) Long companyId) {

        List<AssetOverviewDTO> overview = assetService.getAssetOverview(asOfDate, companyId);
        return ResponseEntity.ok(overview);
    }

    // ============================================================================
    // Control Rules
    // ============================================================================

    /**
     * Get all control rules
     */
    @GetMapping("/controls")
    public ResponseEntity<List<AssetControlRuleDTO>> getControlRules() {
        List<AssetControlRuleDTO> rules = assetService.getControlRules();
        return ResponseEntity.ok(rules);
    }

    /**
     * Get single control rule by ID
     */
    @GetMapping("/controls/{id}")
    public ResponseEntity<AssetControlRuleDTO> getControlRuleById(@PathVariable Long id) {
        AssetControlRuleDTO rule = assetService.getControlRuleById(id);
        return ResponseEntity.ok(rule);
    }

    /**
     * Create new control rule
     */
    @PostMapping("/controls")
    public ResponseEntity<AssetControlRuleDTO> createControlRule(
            @RequestBody AssetControlRuleDTO dto) {
        AssetControlRuleDTO created = assetService.createControlRule(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Update control rule
     */
    @PutMapping("/controls/{id}")
    public ResponseEntity<AssetControlRuleDTO> updateControlRule(
            @PathVariable Long id,
            @RequestBody AssetControlRuleDTO dto) {
        AssetControlRuleDTO updated = assetService.updateControlRule(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete control rule
     */
    @DeleteMapping("/controls/{id}")
    public ResponseEntity<Void> deleteControlRule(@PathVariable Long id) {
        assetService.deleteControlRule(id);
        return ResponseEntity.noContent().build();
    }

    // ============================================================================
    // Export
    // ============================================================================

    /**
     * Export assets to Excel
     *
     * Optional query parameters:
     * - companyId: Filter by company
     * - asOfDate: Snapshot date
     */
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportAssetsToExcel(
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate asOfDate) {

        // For now, return empty Excel file
        // TODO: Implement actual Excel generation
        byte[] excelData = new byte[0];

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "assets.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .body(excelData);
    }
}
