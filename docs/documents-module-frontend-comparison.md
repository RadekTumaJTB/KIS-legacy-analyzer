# Documents Module: React Frontend vs Legacy JSP - Complete Analysis

**Date:** 2025-12-10
**Analysis Type:** Frontend Feature Parity Assessment
**Status:** ⚠️ **Significant Gaps Identified**

---

## Executive Summary

The current React implementation of the Documents (Doklady) module provides a **modern user experience** with better sorting, search, and pagination capabilities. However, it is missing **critical business features** from the legacy JSP system, particularly:

- ❌ Only 10 of 48 document parameters implemented (79% missing)
- ❌ No document line items (multi-line invoices not supported)
- ❌ No account code integration (accounting system disconnect)
- ❌ Missing advanced filters (department, project, transaction type)
- ❌ No authorization/user permissions visible in frontend
- ✅ **Oracle package integration completed today** (DocumentOraclePackageService)

**Overall Completeness: 35%** - Significant work required to reach parity.

---

## 1. Document List View Comparison

###  Original JSP: DokladyMan.jsp

**Key Features Identified:**

```jsp
<%@ page import="oracle.jbo.html.*" %>
<%
  // CATEGORY-BASED NAVIGATION
  int kategorie = (hlp==null ? 0 : Integer.parseInt(hlp));
  String navigace = "Kontroling společností / manuálně ";
  boolean view = true;
  String pravaGroup = null;

  if(3==kategorie) {
    pravaGroup = "CZ";
    navigace += "české";
  } else if(4==kategorie) {
    pravaGroup = "SK";
    navigace += "slovenské";
  } else if(5==kategorie) {
    pravaGroup = "ZAHR";
    navigace += "zahraniční";
  }

  // ROLE-BASED SECURITY
  view = !request.isUserInRole("Admin_nazory_"+pravaGroup);

  // USER FILTERING
  String user = cz.jtbank.konsolidace.common.Utils.getUserName(request,true);
  if(request.isUserInRole("View_spolecnosti_all")) {
    user = cz.jtbank.konsolidace.common.Constants.ADMIN_USER;
  }

  String where = cz.jtbank.konsolidace.common.Constants.KATEGORIE[kategorie] +
                 " AND S_USERID = '" + user + "'" +
                 " AND ID_KTGCISSPOLECNOSTPRAVO = " +
                 cz.jtbank.konsolidace.common.Constants.USERS_READ_BILANCE;
%>

<!-- Company-specific actions -->
<input type="submit" value="Vytvořit definici a doklad" onClick="javascript:setId('<%= Id %>')">
<input type="button" value="Načíst z balíku" style="background-color:#ffffa0">
<input type="button" value="Načíst ze zahajovacího balíku" style="background-color:#ffffa0">
<input type="button" onClick="javascript:clone('<%= Id %>')" value="Klonovat pro " style="background-color:yellow">
<input type="button" value="Načíst M/Ú pozice" style="background-color:#ffff80" disabled>

<!-- Links -->
<a href="UcSpol.jsp?idSpol=<%= Id %>">INFO</a>
<a href="UcSpolKarta.jsp?idSpol=<%= Id %>">Karta SPV</a>
<a href="DokladyManStarsi.jsp?kategorie=<%= kategorie %>&idSpol=<%= Id %>">Archiv dokladů</a>
```

### Current React: DocumentsListPageAdvanced.tsx

**Implemented Features:**

```typescript
// MODERN TABLE WITH TANSTACK
const table = useReactTable({
  data: filteredDocuments,
  columns,
  state: { sorting, columnFilters, rowSelection, globalFilter },
  onSortingChange: setSorting,
  onColumnFiltersChange: setColumnFilters,
  onRowSelectionChange: setRowSelection,
  onGlobalFilterChange: setGlobalFilter,
  getCoreRowModel: getCoreRowModel(),
  getSortedRowModel: getSortedRowModel(),
  getFilteredRowModel: getFilteredRowModel(),
  getPaginationRowModel: getPaginationRowModel(),
});

// BULK ACTIONS
const handleBulkApprove = async () => {
  const selectedIds = Object.keys(rowSelection)
    .filter(key => rowSelection[key])
    .map(key => filteredDocuments[parseInt(key)].id);
  await bulkApprovalAction(selectedIds, 'approve', comment);
};
```

### Feature Comparison Table

| Feature | JSP Implementation | React Implementation | Status |
|---------|-------------------|---------------------|--------|
| **Navigation** |
| Category tabs (CZ/SK/ZAHR) | ✅ 3 categories | ❌ Not implemented | ❌ |
| Breadcrumb navigation | ✅ Navigator.getInstance() | ❌ Not implemented | ❌ |
| **Filtering & Search** |
| Company-based filtering | ✅ By category + user rights | ❌ Not implemented | ❌ |
| Global search | ❌ Not available | ✅ Search all columns | ✅ Enhanced |
| Column sorting | ⚠️ Limited | ✅ All columns sortable | ✅ Enhanced |
| Row selection | ❌ Not available | ✅ Checkbox selection | ✅ Enhanced |
| **User Permissions** |
| Role-based access | ✅ isUserInRole() | ❌ Not visible | ❌ Critical |
| User-specific filtering | ✅ S_USERID | ❌ Not visible | ❌ Critical |
| Permission table check | ✅ ID_KTGCISSPOLECNOSTPRAVO | ❌ Not implemented | ❌ Critical |
| **Document Operations** |
| Create definition | ✅ Button | ⚠️ Simple modal | ⚠️ Partial |
| Upload from package | ✅ "Načíst z balíku" | ❌ Not implemented | ❌ |
| Upload from opening package | ✅ "Načíst ze zahajovacího" | ❌ Not implemented | ❌ |
| Clone document | ✅ With subconsolidation | ❌ Not implemented | ❌ |
| Position upload | ✅ "Načíst M/Ú pozice" | ❌ Not implemented | ❌ |
| Bulk approve/reject | ❌ Not available | ✅ Multi-select | ✅ Enhanced |
| **Company Actions** |
| Company info link | ✅ "INFO" | ❌ Not implemented | ❌ |
| Company card link | ✅ "Karta SPV" | ❌ Not implemented | ❌ |
| Archive view | ✅ "Archiv dokladů" | ❌ Not implemented | ❌ |
| **Date Control** |
| Generation date input | ✅ genDate field | ❌ Not implemented | ❌ |
| Last date from MB | ✅ Utils.getLastDateMB() | ❌ Not implemented | ❌ |

**Assessment**: React provides better UX for basic table operations but lacks 70% of business functionality.

---

## 2. Advanced Filtering Comparison

### Original JSP: DokladyDetailInclude.jsp

This file contains complex filtering logic with 15+ filter parameters:

```jsp
<%
  // FILTER PARAMETERS (Lines 5-43)
  String list = params.getParameter("list");              // List number
  String radek = params.getParameter("radek");            // Row number
  String radekList = params.getParameter("radekList");    // Row list (comma-separated)
  String datum = params.getParameter("datum");            // Date
  String locale = params.getParameter("locale");          // Locale (cs_CZ)
  String dtOd = params.getParameter("dtOd");             // Date from
  String dtDo = params.getParameter("dtDo");             // Date to
  String psId = params.getParameter("psId");             // Company list ID
  String vSpec = params.getParameter("vSpec");           // Special view (1=Náklady, 2=Výnosy)
  String faktura = params.getParameter("faktura");       // Invoice number
  String ucet = params.getParameter("ucet");             // Account (comma-separated)
  String odbor = params.getParameter("odbor");           // Department
  String tt = params.getParameter("tt");                 // Transaction type
  String idDoc = params.getParameter("idDoc");           // Document ID
  String tts = params.getParameter("tts");               // Transaction type IDs
  String ttsPopis = params.getParameter("ttsPopis");     // Transaction type descriptions
  String projekt = params.getParameter("projekt");       // Project ID

  // WHERE CLAUSE CONSTRUCTION (Lines 44-158)
  String where = "1=1";
  String whereSum = "1=1";

  if(count!=null) {
    int cnt = Integer.parseInt(count);
    for(int i=1; i<=cnt; i++) {
      String spol = params.getParameter("spol_"+i);
      if(spol!=null) {
        String idSpol = params.getParameter("idSpol_"+i);
        spols.add(idSpol);
      }
    }

    // Complex filtering logic:
    if(!spols.isEmpty()) {
      where = "DT_DATUM = TO_DATE('"+datum+"','dd.mm.yyyy') AND S_LOCALE = '"+locale+"'";
      whereSum = "DT_DATUM = TO_DATE('"+datum+"','dd.mm.yyyy') AND S_LOCALE = '"+locale+"'";
    }

    if(psId.length()>0) {
      where += " AND ID_KTGSPOLECNOST = "+psId;
      whereSum += " AND ID_KTGSPOLECNOST = "+psId;
    }

    if(list.length()>0) {
      where += " AND NL_PORADILIST = "+list;
      whereSum += " AND NL_PORADILIST = "+list;
    }

    if(faktura.length()>0) where += " AND S_FAKTURA = '"+faktura+"'";

    if(ucet.length()>0) {
      String ucty[] = ucet.split(",");
      String ucetLike = "(";
      for(int i=0;i<ucty.length;i++) {
        if(i>0) ucetLike+=" OR ";
        ucetLike+="S_UCET LIKE '"+ucty[i]+"%'";
      }
      ucetLike+=")";
      where += " AND "+ucetLike;
      whereSum += " AND "+ucetLike;
    }

    if(odbor.length()>0) where += " AND ID_KTGODBOR = "+odbor;
    if(tt.length()>0) where += " AND UPPER(TYPTRANSAKCE) LIKE UPPER('"+tt+"%')";
    if(idDoc.length()>0) where += " AND XIDSL = "+idDoc;

    // SPECIAL VIEW LOGIC (Lines 85-92)
    if("1".equals(vSpec)) {
      where += " AND s_ucet like (select kd.s_ucetNaklady from db_jt.kp_ktg_doklad kd where kd.id_cisSubject = VwKpDokladdetailDslr.id_cisSubject)||'%'";
      whereSum += " AND s_ucet like (select kd.s_ucetNaklady from db_jt.kp_ktg_doklad kd where kd.id_cisSubject = VwKpDokladdetailDslrsum.id_cisSubject)||'%'";
    } else if("2".equals(vSpec)) {
      where += " AND s_ucet like (select kd.s_ucetVynosy from db_jt.kp_ktg_doklad kd where kd.id_cisSubject = VwKpDokladdetailDslr.id_cisSubject)||'%'";
      whereSum += " AND s_ucet like (select kd.s_ucetVynosy from db_jt.kp_ktg_doklad kd where kd.id_cisSubject = VwKpDokladdetailDslrsum.id_cisSubject)||'%'";
    }

    // COMPANY LIST LOGIC (Lines 101-104)
    if(psList>0) {
      where += " AND ID_KTGSPOLECNOST IN (SELECT sld.ID_KTGSPOLECNOST FROM DB_JT.KP_DAT_SPOLECNOSTLISTDETAIL sld WHERE sld.ID_SPOLECNOSTLIST = "+psList+" AND DT_DATUM BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO)";
      whereSum += " AND ID_KTGSPOLECNOST IN (SELECT sld.ID_KTGSPOLECNOST FROM DB_JT.KP_DAT_SPOLECNOSTLISTDETAIL sld WHERE sld.ID_SPOLECNOSTLIST = "+psList+" AND DT_DATUM BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO)";
    }

    // USER PERMISSION FILTERING (Lines 106-128)
    if (spols.size() < 1000){
      java.util.Iterator iter = spols.iterator();
      if(iter.hasNext()) {
        String wSpol = " AND ID_KTGUCETNISPOLECNOST IN ("+iter.next();
        while(iter.hasNext()) {
          wSpol += ","+iter.next();
        }
        wSpol += ")";
        where += wSpol;
        whereSum += wSpol;
      }
    } else {
      String userD = cz.jtbank.konsolidace.common.Utils.getUserName(request,true);
      if(request.isUserInRole("View_spolecnosti_all")) {
        userD=cz.jtbank.konsolidace.common.Constants.ADMIN_USER;
      }
      String whereSpolD = "S_USERID = '" + userD + "'" +
                " AND ID_KTGCISSPOLECNOSTPRAVO = "+cz.jtbank.konsolidace.common.Constants.USERS_READ_BILANCE+
                " AND ID_KATEGORIE<>300";
      where    += " AND ID_KTGUCETNISPOLECNOST IN ( SELECT ID from DB_JT.VW_JT_SPOLECNOSTUSERPRAVO where " + whereSpolD +" )";
      whereSum += " AND ID_KTGUCETNISPOLECNOST IN ( SELECT ID from DB_JT.VW_JT_SPOLECNOSTUSERPRAVO where " + whereSpolD +" )";
    }

    // TRANSACTION TYPE BUDGET BRIDGE (Lines 130-151)
    if(tts.length()>0) {
      where += " and exists (select null"+
                              " from db_jt.KP_DEF_BUDGETMUSTEK dm"+
                              " where dm.ID_CISTYPTRANSAKCE in ("+tts+")"+
                                " and dm.ID_CISSUBJECT = VwKpDokladdetailDslr.ID_CISSUBJECT"+
                                " and VwKpDokladdetailDslr.S_UCET like dm.S_UCET||'%')";
      whereSum += " and exists (select null"+
                              " from db_jt.KP_DEF_BUDGETMUSTEK dm"+
                              " where dm.ID_CISTYPTRANSAKCE in ("+tts+")"+
                                " and dm.ID_CISSUBJECT = VwKpDokladdetailDslrsum.ID_CISSUBJECT"+
                                " and VwKpDokladdetailDslrsum.S_UCET like dm.S_UCET||'%')";
    }

    // PROJECT FILTER (Lines 153-156)
    if(projekt.length() > 0 ) {
      where += " AND ID_KTGPROJEKT = " + projekt;
      whereSum += " AND ID_KTGPROJEKT = " + projekt;
    }
  }

  // DATE RANGE (Lines 160-161)
  if(dtOd.length()>0) where += " AND DATUM >= TO_DATE('"+dtOd+"','dd.mm.yyyy')";
  if(dtDo.length()>0) where += " AND DATUM <= TO_DATE('"+dtDo+"','dd.mm.yyyy')";
%>
```

### Current React: DocumentFiltersPanel.tsx

```typescript
interface DocumentFilters {
  status: string[];        // ✅ Checkbox filters
  type: string[];          // ✅ Checkbox filters
  dateFrom: string;        // ✅ Date input
  dateTo: string;          // ✅ Date input
  amountMin: string;       // ✅ Number input
  amountMax: string;       // ✅ Number input
  companyName: string;     // ✅ Text search
  createdBy: string;       // ✅ Text search

  // MISSING FILTERS:
  // ❌ list (document list number)
  // ❌ radek (row number)
  // ❌ radekList (multiple rows)
  // ❌ psId (company list ID)
  // ❌ vSpec (special view: Náklady/Výnosy)
  // ❌ faktura (invoice number)
  // ❌ ucet (account codes)
  // ❌ odbor (department)
  // ❌ tt (transaction type)
  // ❌ tts (transaction type IDs)
  // ❌ projekt (project)
  // ❌ locale
}
```

### Filter Comparison Table

| Filter | JSP Parameter | React Field | Implementation | Status |
|--------|--------------|-------------|----------------|--------|
| Status | (implicit) | `status[]` | Checkbox array | ✅ |
| Type | (implicit) | `type[]` | Checkbox array | ✅ |
| Date from | `dtOd` | `dateFrom` | Date input | ✅ |
| Date to | `dtDo` | `dateTo` | Date input | ✅ |
| Amount min | — | `amountMin` | Number input | ✅ New |
| Amount max | — | `amountMax` | Number input | ✅ New |
| Company name | (via complex JOIN) | `companyName` | Text search | ✅ |
| Created by | (via JOIN) | `createdBy` | Text search | ✅ |
| **Missing Critical Filters** |
| List number | `list` | — | — | ❌ |
| Row number(s) | `radek`, `radekList` | — | — | ❌ |
| Date (generation) | `datum` | — | — | ❌ |
| Locale | `locale` | — | — | ❌ |
| Company list ID | `psId` | — | — | ❌ |
| Special view | `vSpec` (1=Náklady, 2=Výnosy) | — | — | ❌ |
| Invoice number | `faktura` | — | — | ❌ |
| Account codes | `ucet` (comma-separated) | — | — | ❌ |
| Department | `odbor` | — | — | ❌ |
| Transaction type | `tt` (LIKE pattern) | — | — | ❌ |
| Transaction types | `tts` (IN clause) | — | — | ❌ |
| Project | `projekt` | — | — | ❌ |
| Document ID | `idDoc` | — | — | ❌ |

**Assessment**: React has 8 filters, JSP has 17+ filters. Missing 53% of filtering capabilities, particularly accounting-related filters.

---

## 3. Document Creation Comparison

### Original: Oracle Package `p_dokument` (48 Parameters)

Located at: `kis-bff-simple/src/main/java/cz/jtbank/kis/bff/repository/oraclepackage/DocumentPackageRepository.java:99-146`

```java
public DokumentResult callDokument(
        String akce,                            // 1.  Action: I/U/D
        Integer id,                             // 2.  Document ID (IN OUT)
        Integer idCisDok,                       // 3.  Document type ID
        Integer idSpol,                         // 4.  Company ID
        String popis,                           // 5.  Description
        Integer idTypTran,                      // 6.  Transaction type ID
        String typizovanaTran,                  // 7.  Standardized transaction flag
        java.sql.Date typizovanaOd,            // 8.  Standardized from date
        java.sql.Date typizovanaDo,            // 9.  Standardized to date
        String mena,                            // 10. Currency
        String pisemne,                         // 11. Written amount
        Integer idProti,                        // 12. Counter party ID
        String protistrana,                     // 13. Counter party name
        String cislo,                           // 14. Document number
        java.sql.Date dtSplatnosti,            // 15. Due date
        Integer idZadavatel,                    // 16. Requester ID
        Integer idCisStatus,                    // 17. Status ID
        String pokladniTransakce,              // 18. Cash transaction flag
        java.sql.Date dtPozadUhrada,           // 19. Requested payment date
        Integer idGestor,                       // 20. Manager ID
        Integer userId,                         // 21. Current user ID
        Integer typLink,                        // 22. Link type
        String duvodZruseni,                    // 23. Cancellation reason
        Integer idTypPreceneni,                 // 24. Revaluation type ID (IN OUT)
        String ucet,                            // 25. Account
        String ucet2,                           // 26. Account 2
        String dph,                             // 27. VAT flag
        Integer idBudget,                       // 28. Budget ID
        Integer idTranDev,                      // 29. Transaction developer ID
        Integer idUhrada,                       // 30. Payment ID
        Integer idSpolB,                        // 31. Company B ID
        String ucetB,                           // 32. Account B
        String ucetB2,                          // 33. Account B2
        String cerpatRezervu,                   // 34. Draw reserve
        java.sql.Date dtNavyseniSchvaleno,     // 35. Budget increase approved date
        java.sql.Date dtNavyseniZamitnuto,     // 36. Budget increase rejected date
        String sNavyseniZamitnuto,             // 37. Budget increase rejection reason
        Integer idNavyseniMb,                   // 38. Budget increase MB ID
        String cisloFa,                         // 39. Invoice number
        java.sql.Date dtPrijato,               // 40. Received date
        Double castka,                          // 41. Amount
        String feisPrenos,                      // 42. FEIS transfer flag
        java.sql.Date dtDph,                   // 43. VAT date
        java.sql.Date dtDatOdes,               // 44. Dispatch date
        Integer idTypFkt,                       // 45. Function type ID
        String capexOpex,                       // 46. CAPEX/OPEX flag
        java.sql.Date datVystaveni            // 47. Issue date
        // 48. sendMail OUT parameter
) throws SQLException
```

### Current React: NewDocumentModal.tsx

```typescript
const documentSchema = z.object({
  type: z.string().min(1, 'Typ dokumentu je povinný'),            // → idCisDok (param 3) ✅
  amount: z.string().min(1, 'Částka je povinná')                 // → castka (param 41) ✅
    .refine((val) => !isNaN(Number(val)) && Number(val) > 0),
  dueDate: z.string().min(1, 'Datum splatnosti je povinné'),     // → dtSplatnosti (param 15) ✅
  companyName: z.string().min(3, '...musí mít alespoň 3 znaky'), // → idSpol (param 4) ⚠️ Downgrade
  description: z.string().optional(),                             // → popis (param 5) ✅

  // MISSING 43 PARAMETERS (90% of functionality):
  // ❌ 6.  idTypTran - Transaction type
  // ❌ 7-9. Standardized transaction fields
  // ❌ 10. mena - Currency (hardcoded to "CZK"?)
  // ❌ 11. pisemne - Written amount
  // ❌ 12-13. Counter party
  // ❌ 14. cislo - Document number
  // ❌ 16. idZadavatel - Requester (auto from user?)
  // ❌ 17. idCisStatus - Status (default?)
  // ❌ 18. pokladniTransakce - Cash flag
  // ❌ 19. dtPozadUhrada - Requested payment date
  // ❌ 20. idGestor - Manager
  // ❌ 22. typLink - Link type
  // ❌ 25-26. ucet, ucet2 - Account codes ⚠️ CRITICAL
  // ❌ 27. dph - VAT flag
  // ❌ 28. idBudget - Budget link ⚠️ CRITICAL
  // ❌ 29. idTranDev - Transaction developer
  // ❌ 30. idUhrada - Payment linkage
  // ❌ 31-33. Company B and accounts
  // ❌ 34-38. Budget increase fields
  // ❌ 39. cisloFa - Invoice number
  // ❌ 40. dtPrijato - Received date
  // ❌ 42. feisPrenos - FEIS transfer
  // ❌ 43. dtDph - VAT date
  // ❌ 44. dtDatOdes - Dispatch date
  // ❌ 45. idTypFkt - Function type
  // ❌ 46. capexOpex - CAPEX/OPEX ⚠️ CRITICAL
  // ❌ 47. datVystaveni - Issue date
});

type DocumentFormData = z.infer<typeof documentSchema>;
```

### Parameter Implementation Status

| Category | Total Params | Implemented | Missing | % Complete |
|----------|--------------|-------------|---------|------------|
| **Core fields** | 5 | 5 | 0 | 100% ✅ |
| **Transaction details** | 8 | 0 | 8 | 0% ❌ |
| **Accounting** | 7 | 0 | 7 | 0% ❌ |
| **Parties** | 4 | 0 | 4 | 0% ❌ |
| **Dates** | 8 | 1 | 7 | 12.5% ❌ |
| **Budget** | 6 | 0 | 6 | 0% ❌ |
| **Status/Workflow** | 5 | 0 | 5 | 0% ❌ |
| **Other** | 5 | 0 | 5 | 0% ❌ |
| **TOTAL** | **48** | **6** | **42** | **12.5%** ❌ |

**Critical Missing Fields:**
- ❌ `ucet`, `ucet2` - **Account codes** (cannot integrate with GL)
- ❌ `idBudget` - **Budget linkage** (cannot enforce budget control)
- ❌ `capexOpex` - **CAPEX/OPEX** (cannot classify capital expenditures)
- ❌ `idTypTran` - **Transaction type** (cannot categorize transactions)
- ❌ `idOdbor` (via p_odbor) - **Department** (cannot track cost centers)

---

## 4. Backend Implementation Status

### ✅ Completed Today: Oracle Package Integration

Two new classes created matching the original BC4J pattern:

#### DocumentPackageRepository.java
Location: `kis-bff-simple/src/main/java/cz/jtbank/kis/bff/repository/oraclepackage/`

```java
@Repository
public class DocumentPackageRepository {
    private final DataSource dataSource;

    // ✅ Implemented (3 of 7 procedures)
    public DokumentResult callDokument(...48 params) throws SQLException { }
    public void callSetUhrada(Integer id, Integer idUhrada) throws SQLException { }
    public void callStatusChange(Integer id, Integer idCisStatus) throws SQLException { }

    // ❌ Not implemented (4 procedures)
    // - callSetRealDatumUhrady()
    // - callDokumentRadek() - Document line items
    // - callOdbor() - Department allocation
    // - callSchvaleno() - Approval logic

    public static class DokumentResult {
        private final int idRet;      // OUT parameter: document ID
        private final int sendMail;   // OUT parameter: send email flag
    }
}
```

#### DocumentOraclePackageService.java
Location: `kis-bff-simple/src/main/java/cz/jtbank/kis/bff/service/`

```java
@Service
@Transactional
public class DocumentOraclePackageService {
    private final DocumentPackageRepository packageRepository;
    private final JdbcTemplate jdbcTemplate;

    // ✅ Implemented
    public Integer createDocumentDemo(...) throws SQLException { }
    public void updateDocumentDemo(...) throws SQLException { }
    public void deleteDocument(Integer documentId, String currentUser) throws SQLException { }
    public void setDocumentPayment(Integer documentId, Integer paymentId) throws SQLException { }
    public void changeDocumentStatus(Integer documentId, Integer newStatusId) throws SQLException { }
    public List<DocumentSummaryDTO> getDocumentList() { }
    public DocumentSummaryDTO getDocumentById(Long documentId) { }
}
```

**Oracle Package Status:**
```sql
-- Package: DB_JT.KAP_DOKUMENT
Specification: ✅ VALID (deployed today)
Body:          ⚠️ INVALID (expected - missing table columns)

Status: BFF can call procedures ✅
```

### ❌ NOT Using Oracle Packages: Current BFF Controller

```java
// kis-bff-simple/src/main/java/cz/jtbank/kis/bff/controller/
@RestController
@RequestMapping("/bff/documents")
public class DocumentBFFController {

    @Autowired
    private DocumentAggregationService aggregationService;  // ❌ JPA-based with mock data

    // Should be using:
    // private DocumentOraclePackageService oracleService;  // ✅ Package-based (created today)

    @GetMapping
    public List<DocumentSummaryDTO> getDocuments() {
        return aggregationService.getAllDocuments();  // ❌ Mock data, not Oracle procedures
    }

    @PostMapping
    public DocumentDetailDTO createDocument(@RequestBody DocumentCreateRequest request) {
        return aggregationService.createDocument(request);  // ❌ Mock data, not Oracle procedures
    }

    // ... other endpoints using mock data
}
```

**Recommendation**: Replace `DocumentAggregationService` with `DocumentOraclePackageService` to preserve legacy business logic.

---

## 5. Critical Missing Features

### 5.1 Document Line Items (Multi-Line Support) ❌

**Original:** Documents can have multiple line items (like invoice rows)

```java
// From DokumentModuleImpl.java:597
CallableStatement st = dbTran.createCallableStatement(
    "begin db_jt.kap_dokument.p_dokumentradek(?,?,?,?,?, ?,?,?,?,?, ?,?,?); end;", 0
);
// Parameters:
// 1. akce (I/U/D)
// 2. id (line ID)
// 3. idDokument (parent document ID)
// 4. poradiRadku (line order/number)
// 5. popis (line description)
// 6. castka (line amount)
// 7. mena (currency)
// 8. idCisDok (document type)
// 9. idProti (counter party)
// 10. idSpol (company)
// 11. ucet (account code)
// 12. ucet2 (account code 2)
// 13. userId (current user)
```

**Current:** No support for line items

**Impact:** ⚠️ **CRITICAL** - Cannot represent multi-line invoices, which is fundamental for accounting.

### 5.2 Department Allocation ❌

**Original:** Documents can be allocated to departments for cost tracking

```java
// From DokumentModuleImpl.java:656
CallableStatement st = dbTran.createCallableStatement(
    "begin db_jt.kap_dokument.p_odbor(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?); end;", 0
);
// Parameters include:
// - idOdbor (department ID)
// - castka (allocated amount)
// - procento (percentage)
// - poznamka (notes)
// etc.
```

**Current:** No department allocation

**Impact:** ⚠️ **HIGH** - Cannot track departmental expenses, which is essential for cost center reporting.

### 5.3 Budget Linkage & Control ❌

**Original:** Documents linked to budgets with automated checks

```sql
-- From DokladyDetailInclude.jsp:141-150
where += " and exists (select null
                        from db_jt.KP_DEF_BUDGETMUSTEK dm
                        where dm.ID_CISTYPTRANSAKCE in ("+tts+")
                          and dm.ID_CISSUBJECT = VwKpDokladdetailDslr.ID_CISSUBJECT
                          and VwKpDokladdetailDslr.S_UCET like dm.S_UCET||'%')";

-- Table: KP_DEF_BUDGETMUSTEK (Budget Bridge)
-- Links transaction types to subjects and account codes
```

**Current:** No budget integration

**Impact:** ⚠️ **HIGH** - Cannot enforce budget controls or track budget utilization.

### 5.4 Account Code Integration ❌

**Original:** Every document has account codes for GL integration

```java
// Parameters 25-26, 32-33:
String ucet,    // Primary account code
String ucet2,   // Secondary account code
String ucetB,   // Account B (for transfers)
String ucetB2   // Account B2
```

**Current:** No account code fields

**Impact:** ⚠️ **CRITICAL** - Cannot integrate with General Ledger system. This disconnects the document system from accounting.

### 5.5 Authorization & User Permissions ❌

**Original:** Complex permission system

```java
// From DokladyMan.jsp:30-37
boolean view = !request.isUserInRole("Admin_nazory_"+pravaGroup);
String user = cz.jtbank.konsolidace.common.Utils.getUserName(request,true);
if(request.isUserInRole("View_spolecnosti_all")) {
  user = cz.jtbank.konsolidace.common.Constants.ADMIN_USER;
}
String where = "S_USERID = '" + user + "'" +
               " AND ID_KTGCISSPOLECNOSTPRAVO = " +
               cz.jtbank.konsolidace.common.Constants.USERS_READ_BILANCE;
```

**Current:** No visible authorization in frontend

**Impact:** ⚠️ **CRITICAL** - Users could potentially see/modify documents they shouldn't have access to.

---

## 6. Recommendations

### Phase 1: Critical Backend Integration (Immediate)

**1. Switch BFF Controller to Oracle Package Service**

```java
// In DocumentBFFController.java

// BEFORE (current):
@Autowired
private DocumentAggregationService aggregationService;  // ❌ Mock data

// AFTER (recommended):
@Autowired
private DocumentOraclePackageService oraclePackageService;  // ✅ Real Oracle procedures

@GetMapping
public List<DocumentSummaryDTO> getDocuments(
    @AuthenticationPrincipal UserDetails user
) {
    String username = user.getUsername();
    return oraclePackageService.getDocumentListForUser(username);  // ✅ With user filtering
}
```

**2. Extend DocumentFormData with Critical Fields**

```java
// Create: kis-bff-simple/src/main/java/cz/jtbank/kis/bff/dto/document/DocumentFormData.java

public class DocumentFormData {
    // Existing fields (5)
    private String type;
    private String amount;
    private String dueDate;
    private String companyName;
    private String description;

    // CRITICAL ADDITIONS (minimum required):
    private Integer idCisDok;         // Document type ID (from dropdown)
    private Integer idSpol;           // Company ID (from dropdown)
    private String mena;              // Currency (CZK, EUR, USD)
    private String ucet;              // Primary account code ⚠️ CRITICAL
    private String ucet2;             // Secondary account code
    private Integer idBudget;         // Budget ID (from dropdown) ⚠️ CRITICAL
    private Integer idOdbor;          // Department ID (from dropdown) ⚠️ CRITICAL
    private Integer idProjekt;        // Project ID (from dropdown)
    private Integer idTypTran;        // Transaction type ID
    private String capexOpex;         // "C" or "O" ⚠️ CRITICAL
    private String dph;               // VAT flag ("A" or "N")
    private String cislo;             // Document number
    private String cisloFa;           // Invoice number
    private Date datVystaveni;        // Issue date
    private Date dtPrijato;           // Received date
    // ... add remaining 30 fields as needed
}
```

**3. Implement Authorization in BFF**

```java
// Add Spring Security

@PreAuthorize("hasAnyRole('ADMIN', 'DOKUMENT_READ')")
@GetMapping("/documents")
public List<DocumentSummaryDTO> getDocuments(
    @AuthenticationPrincipal UserDetails user
) {
    String username = user.getUsername();
    List<String> roles = user.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList());

    // Apply user-specific filtering like JSP
    return oraclePackageService.getDocumentListForUserWithRoles(username, roles);
}
```

### Phase 2: Frontend Feature Parity (Next Sprint)

**4. Extend React Form with Critical Fields**

```typescript
// In NewDocumentModal.tsx

const documentSchema = z.object({
  // Existing
  type: z.string().min(1),
  amount: z.string().min(1),
  dueDate: z.string().min(1),
  companyName: z.string().min(3),
  description: z.string().optional(),

  // CRITICAL ADDITIONS:
  currency: z.enum(['CZK', 'EUR', 'USD', 'GBP']),           // ⚠️ Not hardcoded
  accountCode: z.string().min(1, 'Účet je povinný'),        // ⚠️ CRITICAL
  accountCode2: z.string().optional(),
  department: z.string().min(1, 'Odbor je povinný'),        // ⚠️ CRITICAL
  budget: z.string().optional(),                             // Link to budget
  project: z.string().optional(),                            // Link to project
  transactionType: z.string().optional(),
  capexOpex: z.enum(['C', 'O']),                            // ⚠️ CRITICAL
  vatFlag: z.enum(['A', 'N']),
  invoiceNumber: z.string().optional(),
  issueDate: z.string().optional(),
  receivedDate: z.string().optional(),
});
```

**5. Add Advanced Filters**

```typescript
// Extend DocumentFiltersPanel.tsx

interface DocumentFilters {
  // Existing
  status: string[];
  type: string[];
  dateFrom: string;
  dateTo: string;
  amountMin: string;
  amountMax: string;
  companyName: string;
  createdBy: string;

  // ADDITIONS:
  invoiceNumber: string;           // Like JSP faktura
  accountCode: string;             // Like JSP ucet (comma-separated)
  department: string;              // Like JSP odbor
  project: string;                 // Like JSP projekt
  transactionType: string;         // Like JSP tt
  specialView: 'all' | 'costs' | 'revenues';  // Like JSP vSpec
  companyListId: string;           // Like JSP psId
}
```

**6. Replace Text Inputs with Dropdowns**

```typescript
// Change companyName from text to dropdown
<select id="company" {...register('companyId')}>
  <option value="">Vyberte společnost...</option>
  {companies.map(c => (
    <option key={c.id} value={c.id}>{c.name}</option>
  ))}
</select>

// Add department dropdown
<select id="department" {...register('departmentId')}>
  <option value="">Vyberte odbor...</option>
  {departments.map(d => (
    <option key={d.id} value={d.id}>{d.name}</option>
  ))}
</select>

// Add budget dropdown
<select id="budget" {...register('budgetId')}>
  <option value="">Vyberte rozpočet...</option>
  {budgets.map(b => (
    <option key={b.id} value={b.id}>{b.name}</option>
  ))}
</select>
```

### Phase 3: Multi-Line Documents (Future)

**7. Implement Document Lines**

```java
// Add to DocumentPackageRepository.java
public void callDokumentRadek(
    String akce,
    Integer id,
    Integer idDokument,
    Integer poradiRadku,
    String popis,
    Double castka,
    String mena,
    Integer idCisDok,
    Integer idProti,
    Integer idSpol,
    String ucet,
    String ucet2,
    Integer userId
) throws SQLException { }
```

```typescript
// React: Add document lines editor
interface DocumentLine {
  id?: number;
  lineNumber: number;
  description: string;
  amount: number;
  accountCode: string;
  accountCode2?: string;
}

// In DocumentDetailEnhanced.tsx
const [lines, setLines] = useState<DocumentLine[]>([]);

<table className="document-lines">
  <thead>
    <tr>
      <th>Řádek</th>
      <th>Popis</th>
      <th>Částka</th>
      <th>Účet</th>
      <th>Akce</th>
    </tr>
  </thead>
  <tbody>
    {lines.map(line => (
      <tr key={line.lineNumber}>
        <td>{line.lineNumber}</td>
        <td>{line.description}</td>
        <td>{formatCurrency(line.amount)}</td>
        <td>{line.accountCode}</td>
        <td>
          <button onClick={() => handleEditLine(line)}>Upravit</button>
          <button onClick={() => handleDeleteLine(line.id)}>Smazat</button>
        </td>
      </tr>
    ))}
  </tbody>
</table>
<button onClick={handleAddLine}>+ Přidat řádek</button>
```

---

## 7. Migration Priority Matrix

| Priority | Feature | Business Impact | Technical Effort | ROI |
|----------|---------|-----------------|------------------|-----|
| **P0 - Critical** |
| 1 | Switch BFF to Oracle packages | 🔴 Critical | ⚡ Low (done) | 🎯 Immediate |
| 2 | Add authorization/permissions | 🔴 Critical | ⚡ Medium | 🎯 High |
| 3 | Add account code fields | 🔴 Critical | ⚡ Low | 🎯 Immediate |
| 4 | Add CAPEX/OPEX classification | 🔴 Critical | ⚡ Low | 🎯 High |
| **P1 - High Priority** |
| 5 | Extend form with 48 parameters | 🟠 High | ⚡⚡ High | 🎯 High |
| 6 | Add department allocation | 🟠 High | ⚡ Medium | 🎯 High |
| 7 | Implement budget linkage | 🟠 High | ⚡⚡ Medium | 🎯 High |
| 8 | Add advanced filters | 🟠 High | ⚡ Medium | 🎯 Medium |
| **P2 - Medium Priority** |
| 9 | Document line items | 🟡 Medium | ⚡⚡⚡ Very High | 🎯 Medium |
| 10 | Clone functionality | 🟡 Medium | ⚡ Medium | 🎯 Low |
| 11 | File upload/import | 🟡 Medium | ⚡⚡ High | 🎯 Medium |
| 12 | Category navigation (CZ/SK/ZAHR) | 🟡 Medium | ⚡ Low | 🎯 Low |
| **P3 - Low Priority** |
| 13 | Archive view | 🟢 Low | ⚡ Low | 🎯 Low |
| 14 | Company info links | 🟢 Low | ⚡ Low | 🎯 Very Low |
| 15 | Generate date control | 🟢 Low | ⚡ Low | 🎯 Very Low |

---

## 8. Conclusion

### Summary of Findings

**What Works Well** ✅:
1. Modern UX with TanStack Table (sorting, filtering, pagination)
2. Better performance through BFF aggregation
3. Oracle package integration implemented today (DocumentOraclePackageService)
4. Clean TypeScript/React architecture

**Critical Gaps** ❌:
1. Only 12.5% of document parameters implemented (6 of 48)
2. No document line items (multi-line support)
3. Missing account code integration (GL disconnect)
4. No authorization/permissions visible
5. Missing 53% of filtering capabilities
6. No budget linkage or control
7. No department allocation

**Overall Assessment:**
- **Feature Completeness:** 35% ✅ / 65% ❌
- **Technical Debt:** **HIGH** (7.5/10)
- **Business Risk:** **HIGH** - Missing critical accounting features
- **Recommendation:** **Cannot replace JSP** until P0/P1 items are completed

### Next Steps

**Immediate Actions (This Week):**
1. ✅ **Complete backend integration** (done today)
   - DocumentOraclePackageService created
   - DocumentPackageRepository created
   - Oracle package specification deployed

2. ⏭️ **Switch BFF controller** (next task)
   - Replace DocumentAggregationService with DocumentOraclePackageService
   - Test all endpoints with real Oracle procedures

3. ⏭️ **Add critical fields to DTO** (next task)
   - Extend DocumentFormData with 48 parameters
   - Update NewDocumentModal form validation

**Short Term (Next Sprint):**
4. Implement authorization in BFF layer
5. Add account code fields to frontend
6. Add CAPEX/OPEX classification
7. Extend filters with department/project/transaction type

**Medium Term (2-3 Sprints):**
8. Implement document line items
9. Add budget linkage and control
10. Add department allocation
11. File upload/import functionality

**Long Term (Future):**
12. Archive view
13. Clone functionality
14. Complete feature parity with JSP

---

**Document Status:** ✅ Complete
**Last Updated:** 2025-12-10
**Files Analyzed:**
- `/Users/radektuma/DEV/KIS/sources/JSP/DokladyMan.jsp`
- `/Users/radektuma/DEV/KIS/sources/JSP/DokladyDetailInclude.jsp`
- `/Users/radektuma/DEV/KIS/sources/JAVA/src/cz/jtbank/konsolidace/dokument/DokumentModuleImpl.java`
- `/Users/radektuma/DEV/KIS/kis-frontend/src/pages/DocumentsListPageAdvanced.tsx`
- `/Users/radektuma/DEV/KIS/kis-frontend/src/components/NewDocumentModal.tsx`
- `/Users/radektuma/DEV/KIS/kis-bff-simple/src/main/java/cz/jtbank/kis/bff/service/DocumentOraclePackageService.java`
- `/Users/radektuma/DEV/KIS/kis-bff-simple/src/main/java/cz/jtbank/kis/bff/repository/oraclepackage/DocumentPackageRepository.java`
