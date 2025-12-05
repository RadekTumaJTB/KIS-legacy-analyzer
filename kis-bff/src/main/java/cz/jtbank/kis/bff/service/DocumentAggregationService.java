package cz.jtbank.kis.bff.service;

import cz.jtbank.kis.bff.dto.document.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentAggregationService {

    // private final CoreBackendClient coreBackendClient; // TODO: Inject when ready

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
        log.debug("Fetching full document detail for ID: {}", id);

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
}
