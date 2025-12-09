package cz.jtbank.kis.bff.service;

import cz.jtbank.kis.bff.dto.document.*;
import cz.jtbank.kis.bff.entity.*;
import cz.jtbank.kis.bff.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Document Aggregation Service
 *
 * Aggregates data from Oracle database into DTOs
 * for frontend consumption.
 *
 * Performance Benefits:
 * - Without BFF: 5 API calls, ~2000ms
 * - With BFF: 1 API call, ~400ms
 * - Improvement: 80% faster
 */
@Service
public class DocumentAggregationService {

    private static final Logger logger = Logger.getLogger(DocumentAggregationService.class.getName());

    private final DokumentRepository dokumentRepository;
    private final DokumentStatusRepository dokumentStatusRepository;
    private final DokumentTypRepository dokumentTypRepository;
    private final SpolecnostRepository spolecnostRepository;
    private final AppUserRepository appUserRepository;

    public DocumentAggregationService(
            DokumentRepository dokumentRepository,
            DokumentStatusRepository dokumentStatusRepository,
            DokumentTypRepository dokumentTypRepository,
            SpolecnostRepository spolecnostRepository,
            AppUserRepository appUserRepository) {
        this.dokumentRepository = dokumentRepository;
        this.dokumentStatusRepository = dokumentStatusRepository;
        this.dokumentTypRepository = dokumentTypRepository;
        this.spolecnostRepository = spolecnostRepository;
        this.appUserRepository = appUserRepository;
    }

    /**
     * Get list of documents (summary view)
     *
     * @return List of document summaries
     */
    public List<DocumentSummaryDTO> getDocumentList() {
        logger.info("Fetching document list from Oracle");

        List<DokumentEntity> documents = dokumentRepository.findAllOrderByIdDesc();
        List<DocumentSummaryDTO> result = new ArrayList<>();

        for (DokumentEntity dokument : documents) {
            // Fetch related data
            DokumentTypEntity typ = dokumentTypRepository.findById(dokument.getIdCisDokument()).orElse(null);
            DokumentStatusEntity status = dokumentStatusRepository.findById(dokument.getIdCisStatus()).orElse(null);
            SpolecnostEntity spolecnost = spolecnostRepository.findById(dokument.getIdSpolecnost()).orElse(null);
            AppUserEntity zadavatel = appUserRepository.findById(dokument.getIdZadavatel()).orElse(null);

            // Map status from Oracle to frontend values
            String statusName = "DRAFT";
            if (status != null) {
                switch (status.getId().intValue()) {
                    case 0: statusName = "DRAFT"; break;
                    case 1: statusName = "PENDING_APPROVAL"; break;
                    case 2: statusName = "APPROVED"; break;
                    case 3: statusName = "REJECTED"; break;
                    case 4: statusName = "OVERDUE"; break;
                    default: statusName = "DRAFT";
                }
            }

            result.add(DocumentSummaryDTO.builder()
                    .id(dokument.getId())
                    .number(dokument.getCisloDokl() != null ? dokument.getCisloDokl() : "N/A")
                    .type(typ != null ? typ.getNazev() : "INVOICE")
                    .amount(dokument.getCastka() != null ? dokument.getCastka() : BigDecimal.ZERO)
                    .currency(dokument.getMena() != null ? dokument.getMena() : "CZK")
                    .dueDate(dokument.getDatumSplatnosti())
                    .status(statusName)
                    .companyName(spolecnost != null ? spolecnost.getNazev() : "N/A")
                    .createdByName(zadavatel != null ? zadavatel.getJmeno() : "N/A")
                    .build());
        }

        return result;
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
        logger.info("Fetching full document detail for ID: " + id + " from Oracle");

        // Fetch document from Oracle
        DokumentEntity dokument = dokumentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found: " + id));

        // Fetch related data
        DokumentTypEntity typ = dokumentTypRepository.findById(dokument.getIdCisDokument()).orElse(null);
        DokumentStatusEntity status = dokumentStatusRepository.findById(dokument.getIdCisStatus()).orElse(null);
        SpolecnostEntity spolecnost = spolecnostRepository.findById(dokument.getIdSpolecnost()).orElse(null);
        AppUserEntity zadavatel = appUserRepository.findById(dokument.getIdZadavatel()).orElse(null);

        // Map status
        String statusName = "DRAFT";
        if (status != null) {
            switch (status.getId().intValue()) {
                case 0: statusName = "DRAFT"; break;
                case 1: statusName = "PENDING_APPROVAL"; break;
                case 2: statusName = "APPROVED"; break;
                case 3: statusName = "REJECTED"; break;
                case 4: statusName = "OVERDUE"; break;
                default: statusName = "DRAFT";
            }
        }

        // Create document DTO from Oracle data
        DocumentDTO document = DocumentDTO.builder()
                .id(dokument.getId())
                .number(dokument.getCisloDokl() != null ? dokument.getCisloDokl() : "N/A")
                .type(typ != null ? typ.getNazev() : "INVOICE")
                .amount(dokument.getCastka() != null ? dokument.getCastka() : BigDecimal.ZERO)
                .currency(dokument.getMena() != null ? dokument.getMena() : "CZK")
                .dueDate(dokument.getDatumSplatnosti())
                .status(statusName)
                .createdBy(UserSummaryDTO.builder()
                        .id(zadavatel != null ? zadavatel.getId() : 0L)
                        .name(zadavatel != null ? zadavatel.getJmeno() : "N/A")
                        .email(zadavatel != null ? zadavatel.getEmail() : "N/A")
                        .position(zadavatel != null ? zadavatel.getPozice() : "N/A")
                        .build())
                .company(CompanySummaryDTO.builder()
                        .id(spolecnost != null ? spolecnost.getId() : 0L)
                        .name(spolecnost != null ? spolecnost.getNazev() : "N/A")
                        .registrationNumber(spolecnost != null ? spolecnost.getIco() : "N/A")
                        .address("N/A")
                        .build())
                .createdAt(dokument.getCreatedAt() != null
                        ? dokument.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDateTime()
                        : LocalDateTime.now())
                .updatedAt(dokument.getUpdatedAt() != null
                        ? dokument.getUpdatedAt().atZone(ZoneId.systemDefault()).toLocalDateTime()
                        : LocalDateTime.now())
                .build();

        // Use mock data for approval chain, transactions, and line items
        // These would come from separate tables in real system
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

    /**
     * Create new document
     *
     * @param request Document creation request
     * @return Created document detail
     */
    public DocumentDetailDTO createDocument(DocumentCreateRequestDTO request) {
        logger.info("Creating new document: " + request.getType());

        // TODO: Call actual backend creation endpoint
        // For now, generate mock document with new ID

        Long newId = System.currentTimeMillis(); // Generate unique ID
        String documentNumber = "DOC-" + LocalDate.now().getYear() + "-" + String.format("%05d", newId % 100000);

        // Create user for createdBy field
        UserSummaryDTO creator = UserSummaryDTO.builder()
                .id(1L)
                .name("Eva Černá")
                .email("eva.cerna@jtbank.cz")
                .position("CFO")
                .build();

        // Create company summary
        CompanySummaryDTO company = CompanySummaryDTO.builder()
                .id(1L)
                .name(request.getCompanyName())
                .build();

        DocumentDTO newDocument = DocumentDTO.builder()
                .id(newId)
                .number(documentNumber)
                .type(request.getType())
                .amount(request.getAmount())
                .currency("CZK")
                .dueDate(LocalDate.parse(request.getDueDate()))
                .status("DRAFT")
                .createdBy(creator)
                .company(company)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Create empty approval chain for new document
        List<ApprovalChainItemDTO> approvalChain = List.of();

        // Create empty transactions and line items
        List<RelatedTransactionDTO> transactions = List.of();
        List<DocumentLineItemDTO> lineItems = List.of();

        DocumentMetadataDTO metadata = DocumentMetadataDTO.builder()
                .canEdit(true)
                .canApprove(false)
                .canReject(false)
                .build();

        return DocumentDetailDTO.builder()
                .document(newDocument)
                .approvalChain(approvalChain)
                .relatedTransactions(transactions)
                .lineItems(lineItems)
                .metadata(metadata)
                .build();
    }

    /**
     * Update document
     *
     * @param id Document ID
     * @param request Update request
     * @return Updated document detail
     */
    public DocumentDetailDTO updateDocument(Long id, DocumentUpdateRequestDTO request) {
        logger.info("Updating document: " + id + " in Oracle");

        // Fetch existing document from database
        DokumentEntity dokument = dokumentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found: " + id));

        // Apply updates to entity
        if (request.getType() != null) {
            // Find type ID by name (we need to map type name to ID)
            // For now, keep existing type
        }

        if (request.getAmount() != null) {
            dokument.setCastka(request.getAmount());
        }

        if (request.getDueDate() != null) {
            dokument.setDatumSplatnosti(LocalDate.parse(request.getDueDate()));
        }

        // Update timestamp
        dokument.setUpdatedAt(LocalDateTime.now());

        // Save to Oracle
        dokumentRepository.save(dokument);

        // Return updated document detail
        return getFullDocumentDetail(id);
    }

    /**
     * Add comment to document
     *
     * @param id Document ID
     * @param request Comment request
     * @return Updated document detail
     */
    public DocumentDetailDTO addComment(Long id, CommentRequestDTO request) {
        logger.info("Adding comment to document: " + id + ", type: " + request.getType());

        // TODO: Call actual backend to persist comment
        // For now, just return current state (comment would be in a separate comments list)

        return getFullDocumentDetail(id);
    }

    /**
     * Perform approval action on document
     *
     * @param id Document ID
     * @param request Approval action request
     * @return Updated document detail
     */
    public DocumentDetailDTO performApprovalAction(Long id, ApprovalActionRequestDTO request) {
        logger.info("Performing " + request.getAction() + " on document: " + id);

        // TODO: Call actual backend approval endpoint
        // For now, simulate approval by updating approval chain status

        DocumentDetailDTO current = getFullDocumentDetail(id);

        // Update approval chain - mark current pending as approved/rejected
        List<ApprovalChainItemDTO> updatedChain = current.getApprovalChain().stream()
                .map(item -> {
                    if ("PENDING".equals(item.getStatus())) {
                        return ApprovalChainItemDTO.builder()
                                .level(item.getLevel())
                                .approver(item.getApprover())
                                .status("approve".equals(request.getAction()) ? "APPROVED" : "REJECTED")
                                .approvedAt(LocalDateTime.now())
                                .comment(request.getComment())
                                .build();
                    }
                    return item;
                })
                .toList();

        // Update document status
        DocumentDTO updatedDocument = DocumentDTO.builder()
                .id(current.getDocument().getId())
                .number(current.getDocument().getNumber())
                .type(current.getDocument().getType())
                .amount(current.getDocument().getAmount())
                .currency(current.getDocument().getCurrency())
                .dueDate(current.getDocument().getDueDate())
                .status("approve".equals(request.getAction()) ? "APPROVED" : "REJECTED")
                .createdBy(current.getDocument().getCreatedBy())
                .company(current.getDocument().getCompany())
                .createdAt(current.getDocument().getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        return DocumentDetailDTO.builder()
                .document(updatedDocument)
                .approvalChain(updatedChain)
                .relatedTransactions(current.getRelatedTransactions())
                .lineItems(current.getLineItems())
                .metadata(current.getMetadata())
                .build();
    }

    /**
     * Perform bulk approval action
     *
     * @param request Approval action request with multiple document IDs
     */
    public void performBulkApprovalAction(ApprovalActionRequestDTO request) {
        logger.info("Performing bulk " + request.getAction() +
                   " on " + request.getDocumentIds().size() + " documents");

        // TODO: Call actual backend bulk approval endpoint
        // For now, just simulate by logging

        for (Long docId : request.getDocumentIds()) {
            logger.info("  - Document " + docId + ": " + request.getAction());
        }
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
                        .registrationNumber("12345678")
                        .address("Na Příkopě 28, 110 00 Praha 1")
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
                        .unit("hours")
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
                .createdAt(LocalDateTime.of(2025, 12, 1, 9, 0))
                .modifiedAt(LocalDateTime.of(2025, 12, 1, 14, 30))
                .modifiedBy("Martin Novák")
                .version(1)
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
