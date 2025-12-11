package cz.jtbank.kis.bff.controller;

import cz.jtbank.kis.bff.dto.emission.EmissionItemDTO;
import cz.jtbank.kis.bff.dto.emission.EmissionWithItemsDTO;
import cz.jtbank.kis.bff.service.EmissionAggregationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * BFF Controller for Emissions Module
 *
 * Endpoints:
 * - GET /bff/emissions - Get all financial investments with emissions
 * - GET /bff/emissions/{id} - Get specific financial investment with emissions
 * - GET /bff/emissions/history/{financialInvestmentId} - Get emission history
 * - POST /bff/emissions/{financialInvestmentId}/items - Batch update emission items
 * - POST /bff/emissions - Create new financial investment
 * - GET /bff/emissions/export - Export emissions to Excel
 */
@RestController
@RequestMapping("/bff/emissions")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class EmissionBFFController {

    private final EmissionAggregationService emissionService;

    public EmissionBFFController(EmissionAggregationService emissionService) {
        this.emissionService = emissionService;
    }

    /**
     * Get all financial investments with their emission items
     */
    @GetMapping
    public ResponseEntity<List<EmissionWithItemsDTO>> getAllEmissions() {
        List<EmissionWithItemsDTO> emissions = emissionService.getAllEmissions();
        return ResponseEntity.ok(emissions);
    }

    /**
     * Get specific financial investment with its emission items
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmissionWithItemsDTO> getEmissionById(@PathVariable Long id) {
        EmissionWithItemsDTO emission = emissionService.getEmissionById(id);
        return ResponseEntity.ok(emission);
    }

    /**
     * Get emission history for a financial investment
     * Returns all historical versions sorted by validFrom desc
     */
    @GetMapping("/history/{financialInvestmentId}")
    public ResponseEntity<List<EmissionItemDTO>> getEmissionHistory(
            @PathVariable Long financialInvestmentId) {
        List<EmissionItemDTO> history = emissionService.getEmissionHistory(financialInvestmentId);
        return ResponseEntity.ok(history);
    }

    /**
     * Batch update emission items (Insert/Update/Delete)
     *
     * @param financialInvestmentId Financial investment ID
     * @param items List of emission items with action flags (I/U/D)
     * @return Updated financial investment with all items
     */
    @PostMapping("/{financialInvestmentId}/items")
    public ResponseEntity<EmissionWithItemsDTO> batchUpdateEmissionItems(
            @PathVariable Long financialInvestmentId,
            @RequestBody List<EmissionItemDTO> items) {
        EmissionWithItemsDTO result = emissionService.batchUpdateEmissionItems(
                financialInvestmentId, items);
        return ResponseEntity.ok(result);
    }

    /**
     * Create new financial investment
     */
    @PostMapping
    public ResponseEntity<EmissionWithItemsDTO> createFinancialInvestment(
            @RequestBody EmissionWithItemsDTO dto) {
        // For now, return the input as-is
        // TODO: Implement actual creation logic
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    /**
     * Export emissions to Excel
     *
     * Optional query parameters:
     * - companyId: Filter by company
     * - currency: Filter by currency
     * - dateFrom: Filter by valid from date
     * - dateTo: Filter by valid to date
     */
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportEmissionsToExcel(
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) String currency,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo) {

        // For now, return empty Excel file
        // TODO: Implement actual Excel generation
        byte[] excelData = new byte[0];

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "emissions.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .body(excelData);
    }
}
