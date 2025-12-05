package cz.jtbank.kis.bff.controller;

import cz.jtbank.kis.bff.dto.document.DocumentDetailDTO;
import cz.jtbank.kis.bff.dto.document.DocumentSummaryDTO;
import cz.jtbank.kis.bff.service.DocumentAggregationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

/**
 * Document BFF Controller
 *
 * Provides frontend-optimized endpoints for document operations
 *
 * Performance:
 * - /bff/documents/{id}/detail: 1 API call instead of 5 (80% faster)
 *
 * Endpoints:
 * - GET /bff/documents - List of all documents
 * - GET /bff/documents/{id}/detail - Complete document with approvals, transactions, line items
 */
@RestController
@RequestMapping("/bff/documents")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"}) // React dev servers
public class DocumentBFFController {

    private static final Logger logger = Logger.getLogger(DocumentBFFController.class.getName());

    private final DocumentAggregationService documentAggregationService;

    public DocumentBFFController(DocumentAggregationService documentAggregationService) {
        this.documentAggregationService = documentAggregationService;
    }

    /**
     * Get list of documents
     *
     * Returns summary data for all documents
     *
     * @return List of document summaries
     */
    @GetMapping
    public ResponseEntity<List<DocumentSummaryDTO>> getDocuments() {
        logger.info("GET /bff/documents");

        List<DocumentSummaryDTO> documents = documentAggregationService.getDocumentList();

        return ResponseEntity.ok(documents);
    }

    /**
     * Get complete document detail
     *
     * Returns all data needed for Document Detail page:
     * - Document entity
     * - Approval chain with user details
     * - Related transactions
     * - Line items
     * - Metadata (permissions)
     *
     * @param id Document ID
     * @return Complete document detail
     */
    @GetMapping("/{id}/detail")
    public ResponseEntity<DocumentDetailDTO> getDocumentDetail(@PathVariable Long id) {
        logger.info("GET /bff/documents/" + id + "/detail");

        DocumentDetailDTO detail = documentAggregationService.getFullDocumentDetail(id);

        return ResponseEntity.ok(detail);
    }

    /**
     * Health check for Document BFF endpoints
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Document BFF is healthy");
    }
}
