# React BFF vs Legacy JSP - Kompletní Audit

**Datum:** 2025-12-10
**Autor:** Claude Code

---

## 🔴 KRITICKÉ ZJIŠTĚNÍ

Současná React + BFF implementace **NEODPOVÍDÁ** původní JSP aplikaci v těchto klíčových oblastech:

1. **❌ Oracle procedury nejsou volány** - BFF používá pouze JPA místo PL/SQL procedur
2. **❌ Role-based security chybí** - Žádné kontroly uživatelských práv
3. **❌ Filtrace je minimální** - JSP má 15+ filtrů, React má 2-3
4. **❌ Reference dropdowny chybí** - Všude jsou prosté input fieldy místo `<select>`
5. **❌ Audit logging není implementován** - Žádné logy změn
6. **❌ Email notifikace chybí** - Původní app posílá emaily
7. **❌ Chybí 80% funkcionalit** - Memorandum, Návrhy, Uživatelská práva, atd.

---

## MODUL 1: PROJEKTY

### Legacy JSP (53 souborů)

**Hlavní funkcionality:**
1. **Core CRUD** - 7 JSP souborů
2. **Cash Flow** - 9 JSP souborů
3. **Memorandum** - 5 JSP souborů
4. **Návrhy** - 4 JSP soubory
5. **Budget** - 8 JSP souborů
6. **Transakce** - 4 JSP soubory
7. **Bilance** - 4 JSP soubory
8. **Uživatelé a práva** - 6 JSP souborů
9. **Společnosti** - 2 JSP soubory
10. **Doklady** - 3 JSP soubory
11. **Ostatní** - 6 JSP souborů

**Oracle Backend:**
```sql
-- Package: KAP_PROJEKT
PROCEDURE p_KpProjekt(
  p_id IN OUT NUMBER,
  p_cisloOld VARCHAR2,
  p_nazev VARCHAR2,
  p_status NUMBER,
  p_kategorie NUMBER,
  p_mngsegment NUMBER,
  p_pmanager NUMBER,
  p_valuationStartDate DATE,
  p_valuationEndDate DATE,
  -- ... 10+ dalších parametrů
  p_action VARCHAR2  -- 'I'nsert, 'U'pdate, 'D'elete
);

PROCEDURE p_KpProjektCashFlow(...);
PROCEDURE p_KpProjektMemorandum(...);
PROCEDURE p_KpProjektNavrh(...);
PROCEDURE p_KpRelProjektUcSpol(...);
```

**View Objects (BC4J):**
- `VwKtgProjektOverviewView1` - Seznam všech projektů
- `VwKtgProjektuserpravaOverviewView1` - Projekty s filtrací podle práv
- `KpCisProjektstatusView1` - Stavy projektů
- `KpCisProjektkategorieView1` - Kategorie
- `KpCisManagementSegmentView1` - Management segmenty

**Role-based Security:**
```jsp
boolean admin = request.isUserInRole("Admin_projektu");
boolean projektManager = request.isUserInRole("Project_manager");
boolean sefSegmentu = request.isUserInRole("Sef_segmentu");
boolean viewProjekty = request.isUserInRole("View_projekty_jednotlive");
```

**15+ Filtrů:**
- Název projektu
- Staré číslo
- ID projektu
- Status (6 možností)
- Navrhovatel
- Segment boss
- Sponzor
- TOP partner
- Project manager
- Kategorie
- Management segment
- Společnost
- Oceňování (NN)
- IMS memorandum
- Typ bilance
- Sleduje budget (ANO/NE)
- Typ budgetu

### React BFF Implementace

**Frontend:**
- ✅ ProjectListPage (1 soubor)
- ✅ ProjectDetailPage (1 soubor)
- ⏳ NewProjectModal (částečně)
- ⏳ EditProjectModal (částečně)

**Co CHYBÍ:**
- ❌ Cash Flow management (9 JSP)
- ❌ Memorandum (5 JSP)
- ❌ Návrhy projektů (4 JSP)
- ❌ Budget operace (8 JSP)
- ❌ Transakce (4 JSP)
- ❌ Bilance (4 JSP)
- ❌ Uživatelská práva (6 JSP)
- ❌ Správa společností (2 JSP)
- ❌ Doklady (3 JSP)
- ❌ 13 z 15 filtrů
- ❌ Dropdowny pro reference fields
- ❌ Role-based zobrazení

**Backend (BFF):**
```java
// Současná implementace - ProjectAggregationService.java
public List<ProjectSummaryDTO> getAllProjects() {
    // ❌ ŠPATNĚ - Používá JPA místo procedury
    return projectRepository.findAll().stream()
        .map(this::mapToSummaryDTO)
        .collect(Collectors.toList());
}

// ❌ createProject() - Volá pouze JPA save()
// ❌ updateProject() - Volá pouze JPA save()
// ❌ deleteProject() - Volá pouze JPA delete()
```

**Jak BY TO MĚLO být:**
```java
// ✅ SPRÁVNĚ - Volání Oracle procedury
public ProjectDTO createProject(ProjectFormData data) {
    SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
        .withCatalogName("KAP_PROJEKT")
        .withProcedureName("p_KpProjekt");

    SqlParameterSource params = new MapSqlParameterSource()
        .addValue("p_id", data.getId(), Types.NUMERIC)
        .addValue("p_nazev", data.getName())
        .addValue("p_status", data.getStatusId())
        .addValue("p_action", "I");  // Insert

    Map<String, Object> result = call.execute(params);

    // Procedura vrací ID, loguje změnu, posílá email, atd.
    Long newId = ((Number) result.get("p_id")).longValue();
    return getProjectById(newId);
}
```

---

## MODUL 2: DOKUMENTY

### Legacy JSP

**Hlavní funkcionality:**
1. **Schvalovací workflow** - Multilevel approval
2. **Substituty** - Náhradníci ve schvalovacím řetězci
3. **Komentáře** - Historie komentářů ke každému dokumentu
4. **Stavy** - Nový, Ke schválení, Schválený, Zamítnutý, atd.
5. **Email notifikace** - Automatické emaily schvalovatelům

**Oracle Backend:**
```sql
-- Package: KAP_DOKUMENT
PROCEDURE p_KpDokument(
  p_id IN OUT NUMBER,
  p_cislo VARCHAR2,
  p_nazev VARCHAR2,
  p_typ NUMBER,
  p_status NUMBER,
  p_castka NUMBER,
  p_mena VARCHAR2,
  p_schvalovatel1 NUMBER,
  p_schvalovatel2 NUMBER,
  p_schvalovatel3 NUMBER,
  p_action VARCHAR2
);

PROCEDURE p_approveDocument(p_id NUMBER, p_userId NUMBER, p_comment VARCHAR2);
PROCEDURE p_rejectDocument(p_id NUMBER, p_userId NUMBER, p_comment VARCHAR2);
PROCEDURE p_sendNotification(p_id NUMBER);
```

**Schvalovací řetězec:**
```
Dokument Nový
  ↓ (Email schvalovatel1)
Dokument Ke schválení (Level 1)
  ↓ (Schválen/Zamítnut)
Dokument Ke schválení (Level 2)
  ↓ (Schválen/Zamítnut)
Dokument Schválený / Dokument Zamítnutý
```

### React BFF Implementace

**Frontend:**
- ✅ DocumentListPage (1 soubor)
- ✅ DocumentDetailPage (1 soubor)
- ⏳ ApprovalActionsModal (zjednodušený)

**Co CHYBÍ:**
- ❌ Multilevel approval workflow
- ❌ Substituty
- ❌ Email notifikace
- ❌ Kompletní historie komentářů
- ❌ Auto-přechod stavů
- ❌ Role-based schvalování

**Backend (BFF):**
```java
// Současná implementace - DocumentAggregationService.java
public void approveDocument(Long id, String comment) {
    DokumentEntity doc = repository.findById(id).orElseThrow();
    doc.setStatus("SCHVALENO");  // ❌ ŠPATNĚ - Přímo mění stav
    repository.save(doc);
    // ❌ Chybí: Email, audit log, kontrola oprávnění
}
```

**Jak BY TO MĚLO být:**
```java
public void approveDocument(Long id, Long userId, String comment) {
    SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
        .withCatalogName("KAP_DOKUMENT")
        .withProcedureName("p_approveDocument");

    call.execute(
        new MapSqlParameterSource()
            .addValue("p_id", id)
            .addValue("p_userId", userId)
            .addValue("p_comment", comment)
    );

    // Procedura:
    // - Zkontroluje oprávnění
    // - Změní stav
    // - Zapíše audit log
    // - Pošle email dalšímu schvalovateli
    // - Nebo oznámí konečné schválení
}
```

---

## MODUL 3: ROZPOČTY

### Legacy JSP

**Hlavní funkcionality:**
1. **Měsíční položky** - 12 měsíců × rozpočet
2. **Můstky** - Transformace dat mezi systémy
3. **Export do budgetového systému**
4. **Gestor rozpočtu** - Workflow schvalování
5. **Porovnání plán vs skutečnost**

**Oracle Backend:**
```sql
-- Package: KAP_BUDGET
PROCEDURE p_KpBudget(
  p_id IN OUT NUMBER,
  p_rok NUMBER,
  p_odbor NUMBER,
  p_typ NUMBER,
  p_status NUMBER,
  p_action VARCHAR2
);

PROCEDURE p_KpBudgetPolozka(
  p_id IN OUT NUMBER,
  p_idBudget NUMBER,
  p_mesic NUMBER,
  p_castka NUMBER,
  p_action VARCHAR2
);

PROCEDURE p_generateMustky(p_idBudget NUMBER);
PROCEDURE p_exportToSystem(p_idBudget NUMBER);
```

### React BFF Implementace

**Frontend:**
- ✅ BudgetListPage
- ✅ BudgetDetailPage
- ⏳ NewBudgetModal
- ⏳ EditBudgetModal

**Co CHYBÍ:**
- ❌ Měsíční položky (pouze agregace, ne editace)
- ❌ Můstky
- ❌ Export funkcionalita
- ❌ Gestor workflow
- ❌ Plán vs skutečnost

**Backend (BFF):**
```java
// Současná implementace
public BudgetDetailDTO getBudgetDetail(Long id) {
    BudgetEntity budget = repository.findById(id).orElseThrow();
    List<BudgetPolozkaEntity> polozky = polozkaRepository.findByBudgetId(id);

    // ❌ Pouze SELECT, žádná business logika
    return mapToDTO(budget, polozky);
}
```

**Jak BY TO MĚLO být:**
```java
public BudgetDetailDTO createBudgetWithMonths(BudgetFormData data) {
    // 1. Vytvoř budget přes proceduru
    SimpleJdbcCall callBudget = new SimpleJdbcCall(jdbcTemplate)
        .withCatalogName("KAP_BUDGET")
        .withProcedureName("p_KpBudget");

    Map<String, Object> result = callBudget.execute(...);
    Long budgetId = ((Number) result.get("p_id")).longValue();

    // 2. Vytvoř 12 měsíčních položek
    SimpleJdbcCall callPolozka = new SimpleJdbcCall(jdbcTemplate)
        .withCatalogName("KAP_BUDGET")
        .withProcedureName("p_KpBudgetPolozka");

    for (int mesic = 1; mesic <= 12; mesic++) {
        callPolozka.execute(
            new MapSqlParameterSource()
                .addValue("p_idBudget", budgetId)
                .addValue("p_mesic", mesic)
                .addValue("p_castka", data.getMonthAmount(mesic))
                .addValue("p_action", "I")
        );
    }

    // 3. Generuj můstky (pokud je potřeba)
    if (data.isGenerateMustky()) {
        SimpleJdbcCall callMustky = new SimpleJdbcCall(jdbcTemplate)
            .withCatalogName("KAP_BUDGET")
            .withProcedureName("p_generateMustky");
        callMustky.execute(new MapSqlParameterSource().addValue("p_idBudget", budgetId));
    }

    return getBudgetDetail(budgetId);
}
```

---

## SHRNUTÍ - Co je potřeba OPRAVIT

### 🔴 KRITICKÁ PRIORITA (týden)

#### 1. **BFF - Přepsat na Oracle procedury**

**Projekty:**
```java
// kis-bff-simple/src/main/java/cz/jtbank/kis/bff/service/ProjectService.java
@Service
public class ProjectService {
    private final JdbcTemplate jdbcTemplate;

    public ProjectDTO createProject(ProjectFormData data) {
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
            .withCatalogName("KAP_PROJEKT")
            .withProcedureName("p_KpProjekt");

        SqlParameterSource params = new MapSqlParameterSource()
            .addValue("p_id", null, Types.NUMERIC)
            .addValue("p_nazev", data.getName())
            .addValue("p_status", data.getStatusId())
            .addValue("p_kategorie", data.getCategoryId())
            .addValue("p_pmanager", data.getProjectManagerId())
            .addValue("p_action", "I");

        Map<String, Object> result = call.execute(params);
        Long newId = ((Number) result.get("p_id")).longValue();
        return getProjectById(newId);
    }
}
```

**Dokumenty:**
```java
public void approveDocument(Long id, Long userId, String comment) {
    SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
        .withCatalogName("KAP_DOKUMENT")
        .withProcedureName("p_approveDocument");

    call.execute(new MapSqlParameterSource()
        .addValue("p_id", id)
        .addValue("p_userId", userId)
        .addValue("p_comment", comment));
}
```

**Rozpočty:**
```java
public void updateMonthlyBudget(Long budgetId, int month, BigDecimal amount) {
    SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
        .withCatalogName("KAP_BUDGET")
        .withProcedureName("p_KpBudgetPolozka");

    call.execute(new MapSqlParameterSource()
        .addValue("p_idBudget", budgetId)
        .addValue("p_mesic", month)
        .addValue("p_castka", amount)
        .addValue("p_action", "U"));
}
```

#### 2. **Frontend - Přidat dropdowny pro reference fields**

```typescript
// Example: ProjectForm.tsx
<Select
  label="Status"
  value={formData.statusId}
  onChange={(e) => setFormData({...formData, statusId: e.target.value})}
>
  {statuses.map(status => (
    <option key={status.id} value={status.id}>
      {status.name}
    </option>
  ))}
</Select>

<Select label="Kategorie" ...>
<Select label="Management Segment" ...>
<Select label="Project Manager" ...>
```

#### 3. **Frontend - Implementovat filtry**

```typescript
// ProjectListPage.tsx - Přidat všechny filtry jako v JSP
const [filters, setFilters] = useState({
  name: '',
  oldNumber: '',
  id: '',
  statusId: null,
  categoryId: null,
  segmentId: null,
  projectManagerId: null,
  sponsorId: null,
  // ... 10+ dalších
});
```

### 📋 VYSOKÁ PRIORITA (měsíc)

1. **Role-based Security**
   - Implementovat Spring Security
   - Kontrolovat role při každém API callu
   - Filtrovat data podle uživatelských práv

2. **Audit Logging**
   - Logovat všechny změny do `KP_LOG_*` tabulek
   - Ukládat kdo, kdy, co změnil

3. **Email Notifikace**
   - Spring Mail integration
   - Šablony emailů
   - Asynchronní odesílání

### 📊 STŘEDNÍ PRIORITA (3 měsíce)

1. **Rozšíření frontend funkcionalit:**
   - Cash Flow management
   - Memorandum
   - Návrhy projektů
   - Budget operace
   - Uživatelská práva

2. **Export funkce:**
   - Excel export (Apache POI)
   - PDF export (iText)

---

## 📁 SOUBORY K ÚPRAVĚ

### Backend (kis-bff-simple)

```
src/main/java/cz/jtbank/kis/bff/
├── service/
│   ├── ProjectService.java           [PŘEPSAT - použít procedury]
│   ├── DocumentAggregationService.java [PŘEPSAT - použít procedury]
│   └── BudgetAggregationService.java  [PŘEPSAT - použít procedury]
├── security/
│   └── RoleBasedSecurityConfig.java  [VYTVOŘIT]
├── audit/
│   └── AuditLogService.java         [VYTVOŘIT]
└── notification/
    └── EmailService.java             [VYTVOŘIT]
```

### Frontend (kis-frontend)

```
src/
├── pages/
│   ├── ProjectListPage.tsx          [ROZŠÍŘIT - přidat filtry]
│   ├── ProjectDetailPage.tsx        [ROZŠÍŘIT - cash flow, memo]
│   ├── DocumentListPage.tsx         [ROZŠÍŘIT - workflow]
│   └── BudgetDetailPage.tsx         [ROZŠÍŘIT - editace měsíců]
├── components/
│   ├── ui/
│   │   └── Select.tsx               [VYTVOŘIT - chybí dropdown]
│   └── forms/
│       ├── ProjectFilters.tsx       [VYTVOŘIT]
│       ├── DocumentFilters.tsx      [VYTVOŘIT]
│       └── BudgetMonthlyEdit.tsx    [VYTVOŘIT]
└── hooks/
    ├── useReferenceData.ts          [VYTVOŘIT - pro dropdowny]
    └── useUserRoles.ts              [VYTVOŘIT - security]
```

---

## 🚨 DOPORUČENÍ

1. **OKAMŽITĚ:**
   - Přepsat BFF services na volání Oracle procedur
   - Přidat dropdowny místo plain inputs
   - Implementovat základní filtry

2. **TENTO TÝDEN:**
   - Zjistit všechny Oracle procedury (analýza PL/SQL)
   - Dokumentovat parametry procedur
   - Vytvořit migrace guide

3. **TENTO MĚSÍC:**
   - Role-based security
   - Audit logging
   - Email notifikace
   - Kompletní filtrace

4. **DLOUHODOBĚ:**
   - Rozšířit funkcionality na 100% pokrytí JSP
   - Export funkcionalita
   - Advanced workflow

---

**Poslední aktualizace:** 2025-12-10
**Závěr:** Současná implementace je **proof-of-concept** (30% funkčnosti), nikoliv **production-ready** náhrada za JSP aplikaci.
