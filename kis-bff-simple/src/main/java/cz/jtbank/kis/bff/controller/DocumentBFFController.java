package cz.jtbank.kis.bff.controller;

import cz.jtbank.kis.bff.dto.document.*;
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
     * Create new document
     *
     * Creates a new document with the provided data
     *
     * @param request Document creation request
     * @return Created document detail
     */
    @PostMapping
    public ResponseEntity<DocumentDetailDTO> createDocument(@RequestBody DocumentCreateRequestDTO request) {
        logger.info("POST /bff/documents - Creating new document: " + request.getType());

        DocumentDetailDTO created = documentAggregationService.createDocument(request);

        return ResponseEntity.ok(created);
    }

    /**
     * Update document
     *
     * Updates editable fields of a document
     *
     * @param id Document ID
     * @param request Update request with changed fields
     * @return Updated document detail
     */
    @PutMapping("/{id}")
    public ResponseEntity<DocumentDetailDTO> updateDocument(
            @PathVariable Long id,
            @RequestBody DocumentUpdateRequestDTO request) {
        logger.info("PUT /bff/documents/" + id);

        DocumentDetailDTO updated = documentAggregationService.updateDocument(id, request);

        return ResponseEntity.ok(updated);
    }

    /**
     * Add comment to document
     *
     * @param id Document ID
     * @param request Comment request
     * @return Updated document detail with new comment
     */
    @PostMapping("/{id}/comments")
    public ResponseEntity<DocumentDetailDTO> addComment(
            @PathVariable Long id,
            @RequestBody CommentRequestDTO request) {
        logger.info("POST /bff/documents/" + id + "/comments");

        DocumentDetailDTO updated = documentAggregationService.addComment(id, request);

        return ResponseEntity.ok(updated);
    }

    /**
     * Approve or reject document(s)
     *
     * Supports both single document approval and bulk operations
     *
     * @param id Document ID (for single document operation)
     * @param request Approval action request
     * @return Updated document detail
     */
    @PostMapping("/{id}/approval-action")
    public ResponseEntity<DocumentDetailDTO> approvalAction(
            @PathVariable Long id,
            @RequestBody ApprovalActionRequestDTO request) {
        logger.info("POST /bff/documents/" + id + "/approval-action: " + request.getAction());

        DocumentDetailDTO updated = documentAggregationService.performApprovalAction(id, request);

        return ResponseEntity.ok(updated);
    }

    /**
     * Bulk approval action for multiple documents
     *
     * @param request Approval action request with list of document IDs
     * @return Success message
     */
    @PostMapping("/bulk-approval-action")
    public ResponseEntity<String> bulkApprovalAction(@RequestBody ApprovalActionRequestDTO request) {
        logger.info("POST /bff/documents/bulk-approval-action: " + request.getAction() +
                   " for " + request.getDocumentIds().size() + " documents");

        documentAggregationService.performBulkApprovalAction(request);

        return ResponseEntity.ok("Bulk " + request.getAction() + " completed for " +
                                request.getDocumentIds().size() + " documents");
    }

    /**
     * Health check for Document BFF endpoints
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Document BFF is healthy");
    }
}
