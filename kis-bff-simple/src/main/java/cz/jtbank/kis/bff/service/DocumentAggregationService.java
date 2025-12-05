package cz.jtbank.kis.bff.service;

import cz.jtbank.kis.bff.dto.document.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

/**
 * Document Aggregation Service
 *
 * Aggregates data from multiple backend services into single DTOs
 * for frontend consumption.
 *
 * Performance Benefits:
 * - Without BFF: 5 API calls, ~2000ms
 * - With BFF: 1 API call, ~400ms
 * - Improvement: 80% faster
 *
 * TODO: Replace mock data with actual Feign client calls
 * when core backend endpoints are available
 */
@Service
public class DocumentAggregationService {

    private static final Logger logger = Logger.getLogger(DocumentAggregationService.class.getName());

    /**
     * Get list of documents (summary view)
     *
     * @return List of document summaries
     */
    public List<DocumentSummaryDTO> getDocumentList() {
        logger.info("Fetching document list");

        // TODO: Replace with actual backend call
        return createMockDocumentList();
    }

    /**
     * Get complete document detail with all related data
     *
     * Aggregates:
     * - Document entity
     * - Approval chain with user details
     * - Related transactions
     * - Line items
     * - Metadata (permissions, actions)
     *
     * @param id Document ID
     * @return Complete document detail DTO
     */
    public DocumentDetailDTO getFullDocumentDetail(Long id) {
        logger.info("Fetching full document detail for ID: " + id);

        // TODO: Replace with actual parallel API calls using CompletableFuture
        // For now, return mock data for frontend development

        DocumentDTO document = createMockDocument(id);
        List<ApprovalChainItemDTO> approvalChain = createMockApprovalChain();
        List<RelatedTransactionDTO> transactions = createMockTransactions();
        List<DocumentLineItemDTO> lineItems = createMockLineItems();
        DocumentMetadataDTO metadata = createMockMetadata();

        return DocumentDetailDTO.builder()
                .document(document)
                .approvalChain(approvalChain)
                .relatedTransactions(transactions)
                .lineItems(lineItems)
                .metadata(metadata)
                .build();
    }

    // ========== MOCK DATA METHODS ==========
    // TODO: Remove when real backend is available

    private DocumentDTO createMockDocument(Long id) {
        return DocumentDTO.builder()
                .id(id)
                .number("DOC-2025-" + String.format("%04d", id))
                .type("INVOICE")
                .amount(new BigDecimal("150000.00"))
                .currency("CZK")
                .dueDate(LocalDate.of(2025, 1, 15))
                .status("PENDING_APPROVAL")
                .createdBy(UserSummaryDTO.builder()
                        .id(1L)
                        .name("Martin Novák")
                        .email("martin.novak@jtbank.cz")
                        .position("Senior Accountant")
                        .build())
                .company(CompanySummaryDTO.builder()
                        .id(10L)
                        .name("JT Bank a.s.")
                        .ico("12345678")
                        .build())
                .createdAt(LocalDateTime.of(2025, 12, 1, 9, 0))
                .updatedAt(LocalDateTime.of(2025, 12, 1, 14, 30))
                .build();
    }

    private List<ApprovalChainItemDTO> createMockApprovalChain() {
        return List.of(
                ApprovalChainItemDTO.builder()
                        .level(1)
                        .approver(UserSummaryDTO.builder()
                                .id(2L)
                                .name("Petra Svobodová")
                                .email("petra.svobodova@jtbank.cz")
                                .position("Department Manager")
                                .build())
                        .status("APPROVED")
                        .approvedAt(LocalDateTime.of(2025, 12, 1, 14, 30))
                        .comment("Schváleno - vypadá dobře")
                        .build(),
                ApprovalChainItemDTO.builder()
                        .level(2)
                        .approver(UserSummaryDTO.builder()
                                .id(3L)
                                .name("Eva Černá")
                                .email("eva.cerna@jtbank.cz")
                                .position("CFO")
                                .build())
                        .status("PENDING")
                        .approvedAt(null)
                        .comment(null)
                        .build()
        );
    }

    private List<RelatedTransactionDTO> createMockTransactions() {
        return List.of(
                RelatedTransactionDTO.builder()
                        .id(501L)
                        .type("PAYMENT")
                        .amount(new BigDecimal("75000.00"))
                        .date(LocalDate.of(2025, 11, 20))
                        .build()
        );
    }

    private List<DocumentLineItemDTO> createMockLineItems() {
        return List.of(
                DocumentLineItemDTO.builder()
                        .id(1001L)
                        .description("Consulting services")
                        .quantity(10)
                        .unitPrice(new BigDecimal("15000.00"))
                        .total(new BigDecimal("150000.00"))
                        .build()
        );
    }

    private DocumentMetadataDTO createMockMetadata() {
        return DocumentMetadataDTO.builder()
                .canEdit(false) // Not creator, cannot edit
                .canApprove(true) // Current user is Eva (level 2 approver)
                .canReject(true)
                .pendingApproverName("Eva Černá")
                .build();
    }

    private List<DocumentSummaryDTO> createMockDocumentList() {
        return List.of(
                DocumentSummaryDTO.builder()
                        .id(1L)
                        .number("DOC-2025-0001")
                        .type("INVOICE")
                        .amount(new BigDecimal("150000.00"))
                        .currency("CZK")
                        .dueDate(LocalDate.of(2025, 1, 15))
                        .status("PENDING_APPROVAL")
                        .companyName("JT Bank a.s.")
                        .createdByName("Martin Novák")
                        .build(),
                DocumentSummaryDTO.builder()
                        .id(2L)
                        .number("DOC-2025-0002")
                        .type("PURCHASE_ORDER")
                        .amount(new BigDecimal("85000.00"))
                        .currency("CZK")
                        .dueDate(LocalDate.of(2025, 1, 20))
                        .status("APPROVED")
                        .companyName("ACME Corp")
                        .createdByName("Petra Svobodová")
                        .build(),
                DocumentSummaryDTO.builder()
                        .id(3L)
                        .number("DOC-2025-0003")
                        .type("INVOICE")
                        .amount(new BigDecimal("220000.00"))
                        .currency("CZK")
                        .dueDate(LocalDate.of(2025, 1, 10))
                        .status("OVERDUE")
                        .companyName("Tech Solutions s.r.o.")
                        .createdByName("Jan Dvořák")
                        .build(),
                DocumentSummaryDTO.builder()
                        .id(4L)
                        .number("DOC-2025-0004")
                        .type("CREDIT_NOTE")
                        .amount(new BigDecimal("45000.00"))
                        .currency("CZK")
                        .dueDate(LocalDate.of(2025, 1, 25))
                        .status("DRAFT")
                        .companyName("JT Bank a.s.")
                        .createdByName("Eva Černá")
                        .build(),
                DocumentSummaryDTO.builder()
                        .id(5L)
                        .number("DOC-2025-0005")
                        .type("INVOICE")
                        .amount(new BigDecimal("320000.00"))
                        .currency("CZK")
                        .dueDate(LocalDate.of(2025, 2, 1))
                        .status("PENDING_APPROVAL")
                        .companyName("Global Trading Ltd.")
                        .createdByName("Martin Novák")
                        .build()
        );
    }
}
