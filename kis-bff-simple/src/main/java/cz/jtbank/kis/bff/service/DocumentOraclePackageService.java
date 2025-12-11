package cz.jtbank.kis.bff.service;

import cz.jtbank.kis.bff.dto.document.DocumentSummaryDTO;
import cz.jtbank.kis.bff.repository.oraclepackage.DocumentPackageRepository;
import cz.jtbank.kis.bff.repository.oraclepackage.DocumentPackageRepository.DokumentResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Service using Oracle Package calls instead of JPA for Documents module.
 *
 * This service mirrors the original Oracle ADF/BC4J architecture
 * by calling stored procedures in KAP_DOKUMENT package.
 *
 * Pattern: Service → DocumentPackageRepository → CallableStatement → Oracle Package
 *
 * Original: DokumentModuleImpl.dokumentCreate() → db_jt.kap_dokument.p_dokument()
 * New:      DocumentOraclePackageService → DocumentPackageRepository.callDokument()
 */
@Service
@Transactional
public class DocumentOraclePackageService {

    private static final Logger logger = LoggerFactory.getLogger(DocumentOraclePackageService.class);

    private final DocumentPackageRepository packageRepository;
    private final JdbcTemplate jdbcTemplate;

    public DocumentOraclePackageService(
            DocumentPackageRepository packageRepository,
            JdbcTemplate jdbcTemplate) {
        this.packageRepository = packageRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Create a new document by calling Oracle package procedure.
     * Demonstration method - parameters would come from DocumentCreateRequestDTO in real implementation.
     */
    public Integer createDocumentDemo(
            String description,
            Integer documentTypeId,
            Integer companyId,
            String documentNumber,
            Double amount,
            String currency,
            LocalDate dueDate,
            String currentUser) throws SQLException {

        logger.info("Creating document: {} by user: {}", description, currentUser);

        // Generate new document ID using sequence
        Integer newDocumentId = jdbcTemplate.queryForObject(
                "SELECT DB_JT.SEQ_KP_DOKUMENT.NEXTVAL FROM DUAL",
                Integer.class
        );

        Date sqlDueDate = dueDate != null ? Date.valueOf(dueDate) : null;

        // Call Oracle package procedure with action 'I' (Insert)
        // Using minimal required parameters for demonstration
        DokumentResult result = packageRepository.callDokument(
                "I",                        // akce: Insert
                newDocumentId,              // id (IN OUT)
                documentTypeId,             // idCisDok
                companyId,                  // idSpol
                description,                // popis
                null,                       // idTypTran (nullable)
                null,                       // typizovanaTran
                null,                       // typizovanaOd
                null,                       // typizovanaDo
                currency != null ? currency : "CZK", // mena
                null,                       // pisemne
                null,                       // idProti
                null,                       // protistrana
                documentNumber,             // cislo
                sqlDueDate,                 // dtSplatnosti
                1,                          // idZadavatel (default user)
                1,                          // idCisStatus (default: Nový)
                "N",                        // pokladniTransakce
                null,                       // dtPozadUhrada
                null,                       // idGestor
                1,                          // userId
                null,                       // typLink
                null,                       // duvodZruseni
                null,                       // idTypPreceneni
                null,                       // ucet
                null,                       // ucet2
                "N",                        // dph
                null,                       // idBudget
                null,                       // idTranDev
                null,                       // idUhrada
                null,                       // idSpolB
                null,                       // ucetB
                null,                       // ucetB2
                null,                       // cerpatRezervu
                null,                       // dtNavyseniSchvaleno
                null,                       // dtNavyseniZamitnuto
                null,                       // sNavyseniZamitnuto
                null,                       // idNavyseniMb
                null,                       // cisloFa
                null,                       // dtPrijato
                amount,                     // castka
                null,                       // feisPrenos
                null,                       // dtDph
                null,                       // dtDatOdes
                null,                       // idTypFkt
                null,                       // capexOpex
                Date.valueOf(LocalDate.now()) // datVystaveni
        );

        logger.info("Successfully created document with ID: {}, sendMail: {}",
                result.getIdRet(), result.getSendMail());
        return result.getIdRet();
    }

    /**
     * Update existing document by calling Oracle package procedure.
     * Demonstration method.
     */
    public void updateDocumentDemo(
            Integer documentId,
            String description,
            Integer documentTypeId,
            Integer companyId,
            String documentNumber,
            Double amount,
            String currency,
            LocalDate dueDate,
            String currentUser) throws SQLException {

        logger.info("Updating document ID: {} by user: {}", documentId, currentUser);

        Date sqlDueDate = dueDate != null ? Date.valueOf(dueDate) : null;

        // Call Oracle package procedure with action 'U' (Update)
        DokumentResult result = packageRepository.callDokument(
                "U",                        // akce: Update
                documentId,                 // id (IN OUT)
                documentTypeId,             // idCisDok
                companyId,                  // idSpol
                description,                // popis
                null,                       // idTypTran
                null,                       // typizovanaTran
                null,                       // typizovanaOd
                null,                       // typizovanaDo
                currency != null ? currency : "CZK", // mena
                null,                       // pisemne
                null,                       // idProti
                null,                       // protistrana
                documentNumber,             // cislo
                sqlDueDate,                 // dtSplatnosti
                1,                          // idZadavatel
                1,                          // idCisStatus
                "N",                        // pokladniTransakce
                null,                       // dtPozadUhrada
                null,                       // idGestor
                1,                          // userId
                null,                       // typLink
                null,                       // duvodZruseni
                null,                       // idTypPreceneni
                null,                       // ucet
                null,                       // ucet2
                "N",                        // dph
                null,                       // idBudget
                null,                       // idTranDev
                null,                       // idUhrada
                null,                       // idSpolB
                null,                       // ucetB
                null,                       // ucetB2
                null,                       // cerpatRezervu
                null,                       // dtNavyseniSchvaleno
                null,                       // dtNavyseniZamitnuto
                null,                       // sNavyseniZamitnuto
                null,                       // idNavyseniMb
                null,                       // cisloFa
                null,                       // dtPrijato
                amount,                     // castka
                null,                       // feisPrenos
                null,                       // dtDph
                null,                       // dtDatOdes
                null,                       // idTypFkt
                null,                       // capexOpex
                Date.valueOf(LocalDate.now()) // datVystaveni
        );

        logger.info("Successfully updated document ID: {}, sendMail: {}",
                documentId, result.getSendMail());
    }

    /**
     * Delete document by calling Oracle package procedure.
     */
    public void deleteDocument(Integer documentId, String currentUser) throws SQLException {
        logger.info("Deleting document ID: {} by user: {}", documentId, currentUser);

        // Call Oracle package procedure with action 'D' (Delete)
        // Only documentId and action are required for delete
        DokumentResult result = packageRepository.callDokument(
                "D",                        // akce: Delete
                documentId,                 // id
                null,                       // idCisDok
                null,                       // idSpol
                null,                       // popis
                null,                       // idTypTran
                null,                       // typizovanaTran
                null,                       // typizovanaOd
                null,                       // typizovanaDo
                null,                       // mena
                null,                       // pisemne
                null,                       // idProti
                null,                       // protistrana
                null,                       // cislo
                null,                       // dtSplatnosti
                null,                       // idZadavatel
                null,                       // idCisStatus
                null,                       // pokladniTransakce
                null,                       // dtPozadUhrada
                null,                       // idGestor
                1,                          // userId (required)
                null,                       // typLink
                null,                       // duvodZruseni
                null,                       // idTypPreceneni
                null,                       // ucet
                null,                       // ucet2
                null,                       // dph
                null,                       // idBudget
                null,                       // idTranDev
                null,                       // idUhrada
                null,                       // idSpolB
                null,                       // ucetB
                null,                       // ucetB2
                null,                       // cerpatRezervu
                null,                       // dtNavyseniSchvaleno
                null,                       // dtNavyseniZamitnuto
                null,                       // sNavyseniZamitnuto
                null,                       // idNavyseniMb
                null,                       // cisloFa
                null,                       // dtPrijato
                null,                       // castka
                null,                       // feisPrenos
                null,                       // dtDph
                null,                       // dtDatOdes
                null,                       // idTypFkt
                null,                       // capexOpex
                null                        // datVystaveni
        );

        logger.info("Successfully deleted document ID: {}", documentId);
    }

    /**
     * Set payment for a document by calling Oracle package procedure.
     */
    public void setDocumentPayment(Integer documentId, Integer paymentId) throws SQLException {
        logger.info("Setting payment {} for document ID: {}", paymentId, documentId);
        packageRepository.callSetUhrada(documentId, paymentId);
        logger.info("Successfully set payment for document ID: {}", documentId);
    }

    /**
     * Change document status by calling Oracle package procedure.
     */
    public void changeDocumentStatus(Integer documentId, Integer newStatusId) throws SQLException {
        logger.info("Changing status of document ID: {} to status: {}", documentId, newStatusId);
        packageRepository.callStatusChange(documentId, newStatusId);
        logger.info("Successfully changed status for document ID: {}", documentId);
    }

    /**
     * Get document list by querying Oracle views directly.
     * Uses SELECT instead of JPA for better compatibility with Oracle packages.
     *
     * @return List of documents
     */
    public List<DocumentSummaryDTO> getDocumentList() {
        logger.info("Fetching document list using direct SQL query");

        String sql = """
                SELECT
                    d.ID as id,
                    d.S_CISLO as number,
                    dt.S_NAZEV as type,
                    d.R_CASTKA as amount,
                    d.S_MENA as currency,
                    d.DT_SPLATNOST as dueDate,
                    ds.S_POPIS as status,
                    s.S_NAZEV as companyName,
                    u.S_JMENO || ' ' || u.S_PRIJMENI as createdByName
                FROM DB_JT.KP_DAT_DOKUMENT d
                LEFT JOIN DB_JT.KP_CIS_DOKLAD dt ON d.ID_CIS_DOK = dt.ID
                LEFT JOIN DB_JT.KP_CIS_DOKUMENTSTATUS ds ON d.ID_CIS_STATUS = ds.ID
                LEFT JOIN DB_JT.KP_DAT_SPOLECNOST s ON d.ID_SPOL = s.ID
                LEFT JOIN DB_JT.KP_DAT_APPUSER u ON d.ID_ZADAVATEL = u.ID
                ORDER BY d.ID DESC
                """;

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

        return rows.stream()
                .map(row -> DocumentSummaryDTO.builder()
                        .id(((Number) row.get("id")).longValue())
                        .number((String) row.get("number"))
                        .type((String) row.get("type"))
                        .amount(row.get("amount") != null
                                ? BigDecimal.valueOf(((Number) row.get("amount")).doubleValue())
                                : null)
                        .currency((String) row.get("currency"))
                        .dueDate(row.get("dueDate") != null
                                ? ((java.sql.Timestamp) row.get("dueDate")).toLocalDateTime().toLocalDate()
                                : null)
                        .status((String) row.get("status"))
                        .companyName((String) row.get("companyName"))
                        .createdByName((String) row.get("createdByName"))
                        .build())
                .toList();
    }

    /**
     * Get document by ID using direct SQL query.
     *
     * @param documentId Document ID
     * @return Document details or null if not found
     */
    public DocumentSummaryDTO getDocumentById(Long documentId) {
        logger.info("Fetching document by ID: {}", documentId);

        String sql = """
                SELECT
                    d.ID as id,
                    d.S_CISLO as number,
                    dt.S_NAZEV as type,
                    d.R_CASTKA as amount,
                    d.S_MENA as currency,
                    d.DT_SPLATNOST as dueDate,
                    ds.S_POPIS as status,
                    s.S_NAZEV as companyName,
                    u.S_JMENO || ' ' || u.S_PRIJMENI as createdByName
                FROM DB_JT.KP_DAT_DOKUMENT d
                LEFT JOIN DB_JT.KP_CIS_DOKLAD dt ON d.ID_CIS_DOK = dt.ID
                LEFT JOIN DB_JT.KP_CIS_DOKUMENTSTATUS ds ON d.ID_CIS_STATUS = ds.ID
                LEFT JOIN DB_JT.KP_DAT_SPOLECNOST s ON d.ID_SPOL = s.ID
                LEFT JOIN DB_JT.KP_DAT_APPUSER u ON d.ID_ZADAVATEL = u.ID
                WHERE d.ID = ?
                """;

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, documentId);

        if (rows.isEmpty()) {
            return null;
        }

        Map<String, Object> row = rows.get(0);

        return DocumentSummaryDTO.builder()
                .id(((Number) row.get("id")).longValue())
                .number((String) row.get("number"))
                .type((String) row.get("type"))
                .amount(row.get("amount") != null
                        ? BigDecimal.valueOf(((Number) row.get("amount")).doubleValue())
                        : null)
                .currency((String) row.get("currency"))
                .dueDate(row.get("dueDate") != null
                        ? ((java.sql.Timestamp) row.get("dueDate")).toLocalDateTime().toLocalDate()
                        : null)
                .status((String) row.get("status"))
                .companyName((String) row.get("companyName"))
                .createdByName((String) row.get("createdByName"))
                .build();
    }
}
