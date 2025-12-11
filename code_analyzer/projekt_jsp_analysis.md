# Analýza JSP Modulu Projekty - KIS Banking App

## Přehled
- **Celkem JSP souborů**: 53
- **Datum analýzy**: 2025-12-09
- **Zdroj**: /Users/radektuma/DEV/KIS/sources/JSP

## Logické celky (funkční oblasti)

### 1. CORE - Základní CRUD operace projektu (7 souborů)

| Soubor | Účel | Oracle Backend |
|--------|------|----------------|
| `Projekty.jsp` | **Hlavní seznam projektů** - Grid s filtry | `VwKtgProjektOverviewView1` |
| `ProjektyNew.jsp` | Formulář pro nový projekt | ? |
| `ProjektEdit.jsp` | Editace existujícího projektu | ? |
| `ProjektEditProcess.jsp` | **Zpracování úprav** | `KAP_PROJEKT.p_KpProjekt()` |
| `ProjektSetup.jsp` | Nastavení projektu | ? |
| `ProjektHist.jsp` | Historie změn projektu | `KP_LOG_PROJEKT` |
| `ProjektNaklady.jsp` | Náklady projektu | ? |

### 2. CASH FLOW - Správa peněžních toků (9 souborů)

| Soubor | Účel | Oracle Backend |
|--------|------|----------------|
| `ProjektCashFlow.jsp` | Seznam cash flow položek | ? |
| `ProjektCashFlowEdit.jsp` | Editace cash flow | ? |
| `ProjektCashFlowEditProcess.jsp` | **Zpracování CF** | `KAP_PROJEKT.p_KpProjektCashFlow()` |
| `ProjektCashFlowExportSetup.jsp` | Nastavení exportu | ? |
| `ProjektCashFlowExportSetupProcess.jsp` | Zpracování nastavení | ? |
| `ProjektCashFlowExcelProcess.jsp` | Export do Excel | ? |
| `ProjektCashFlowAllExcelProcess.jsp` | Export všech CF do Excel | ? |
| `ProjektSLDeveloper.jsp` | SL Developer view | ? |
| `ProjektSLDeveloperExcelProcess.jsp` | Export SL Developer | ? |

### 3. MEMORANDUM - Dokumentace projektů (5 souborů)

| Soubor | Účel | Oracle Backend |
|--------|------|----------------|
| `ProjektMemorandum.jsp` | Seznam memorand | ? |
| `ProjektMemorandumEdit.jsp` | Editace memoranda | ? |
| `ProjektMemorandumProcess.jsp` | **Zpracování memoranda** | `KAP_PROJEKT.p_KpProjektMemorandum()` |
| `ProjektMemorandumPrint.jsp` | Tisk memoranda | ? |

### 4. NÁVRHY PROJEKTŮ (4 soubory)

| Soubor | Účel | Oracle Backend |
|--------|------|----------------|
| `ProjektNavrhy.jsp` | Seznam návrhů | ? |
| `ProjektNavrhView.jsp` | Detail návrhu | ? |
| `ProjektNavrhEdit.jsp` | Editace návrhu | ? |
| `ProjektNavrhEditProcess.jsp` | **Zpracování návrhu** | `KAP_PROJEKT.p_KpProjektNavrh()` |

### 5. BUDGET - Rozpočtové operace (8 souborů)

| Soubor | Účel | Oracle Backend |
|--------|------|----------------|
| `BudgetProjektCreate.jsp` | Vytvoření projektu z budgetu | ? |
| `BudgetProjektGestor.jsp` | Gestor rozpočtu | ? |
| `BudgetProjektGestor2.jsp` | Gestor rozpočtu v2 | ? |
| `BudgetProjektStd.jsp` | Standardní budget | ? |
| `BudgetProjektStd2.jsp` | Standardní budget v2 | ? |
| `BudgetProjektView.jsp` | Zobrazení budgetu | ? |
| `BudgetProjektTransakce.jsp` | Transakce budgetu | ? |
| `BudgetProjektTransakceDoc.jsp` | Dokumenty transakcí | ? |

### 6. TRANSAKCE (4 soubory)

| Soubor | Účel | Oracle Backend |
|--------|------|----------------|
| `ProjektTransakce.jsp` | Seznam transakcí projektu | ? |
| `ProjektTransakceExcelProcess.jsp` | Export transakcí | ? |
| `ProjektTransakceAllExcelProcess.jsp` | Export všech transakcí | ? |
| `ProjektMISTransakce.jsp` | MIS transakce | ? |

### 7. BILANCE (4 soubory)

| Soubor | Účel | Oracle Backend |
|--------|------|----------------|
| `ProjektyBilance.jsp` | Bilance projektů | ? |
| `ProjektyBilanceProcess.jsp` | Zpracování bilance | ? |
| `ProjektyBilanceDelete.jsp` | Smazání bilance | ? |

### 8. UŽIVATELÉ A OPRÁVNĚNÍ (6 souborů)

| Soubor | Účel | Oracle Backend |
|--------|------|----------------|
| `ProjektUsers.jsp` | Uživatelé projektu | ? |
| `UsersProjekt.jsp` | Projekty uživatele | ? |
| `UsersProjektProcess.jsp` | Zpracování uživatelů | ? |
| `ProjektyPravaProcess.jsp` | **Zpracování práv** | ? |

### 9. SPOLEČNOSTI (2 soubory)

| Soubor | Účel | Oracle Backend |
|--------|------|----------------|
| `ProjektUcSpol.jsp` | Účetní společnosti projektu | `KP_REL_PROJEKTUCSPOL` |
| `ProjektUcSpolProcess.jsp` | **Zpracování společností** | `KAP_PROJEKT.p_KpRelProjektUcSpol()` |

### 10. DOKLADY (3 soubory)

| Soubor | Účel | Oracle Backend |
|--------|------|----------------|
| `DokladManProjektOdbor.jsp` | Manuální doklady odbor | ? |
| `DokladManProjektOdbor2.jsp` | Manuální doklady odbor v2 | ? |
| `DokladManProjektOdborProcess.jsp` | Zpracování dokladů | ? |

### 11. OSTATNÍ (6 souborů)

| Soubor | Účel | Oracle Backend |
|--------|------|----------------|
| `LogsProjektDoklad.jsp` | Logy projektových dokladů | `KP_LOG_PROJEKTDOKLAD` |
| `QProjektOdbor.jsp` | Query projekt/odbor | ? |
| `QProjektOdborProcess.jsp` | Zpracování query | ? |
| `UveryProjekt.jsp` | Úvěry projektu | ? |
| `ProjektyExcelProcess.jsp` | Export projektů do Excel | ? |

---

## Zjištěné Oracle procedury (z PL/SQL analýzy)

### Package: KAP_PROJEKT

| Procedura | Parametry | Funkce |
|-----------|-----------|--------|
| `p_KpProjekt` | 20 params | **CRUD projekt** - INSERT/UPDATE/DELETE |
| `p_KpProjektCashFlow` | ? | CRUD cash flow |
| `p_KpProjektNavrh` | ? | CRUD návrhy |
| `p_KpProjektMemorandum` | 5 params | CRUD memorandum |
| `p_KpRelProjektUcSpol` | 6 params | Vazba projekt-společnost |
| `p_setPouzitHv` | 3 params | Nastavení hlavní společnosti |

### Audit log tabulky

| Tabulka | Účel |
|---------|------|
| `KP_LOG_PROJEKT` | Historie změn projektu |
| `KP_LOG_PROJEKTDOKLAD` | Historie dokladů projektu |

---

## KRITICKÉ ZJIŠTĚNÍ

### Co dělá původní JSP aplikace:

1. **Selecty/Dropdowny** - Všechny reference tables jako `<select>`
   ```jsp
   <jbo:DataSource id="dsStatusF" appid="ProjektModule"
       viewobject="KpCisProjektstatusView1"/>
   <select name="fStatus">
     <jbo:RowsetIterate datasource="dsStatusF">
       <option value='<jbo:ShowValue dataitem="Id"/>'>
       <jbo:ShowValue dataitem="SPopis"/>
   ```

2. **Filtry** - Dynamické WHERE klauzule (Projekty.jsp:40-80)
   ```jsp
   String where = "1 = 1 ";
   if(filterStatus>0) where += " AND ID_STATUS = "+filterStatus;
   if(filterNazev.length()>0) where += " AND UPPER(S_NAZEV) LIKE ...";
   ```

3. **Role-based view** (Projekty.jsp:5-8)
   ```jsp
   boolean admin = request.isUserInRole("Admin_projektu");
   boolean jednotlive = request.isUserInRole("Project_manager");
   String viewObjectName = jednotlive ?
       "VwKtgProjektuserpravaOverviewView1" :
       "VwKtgProjektOverviewView1";
   ```

4. **Email notifikace** - Java Mail API (ProjektModuleImpl.java:299-323)

---

## Co chybí v současném BFF

### Backend (kis-bff-simple):
- ❌ Volání Oracle procedur (`KAP_PROJEKT.p_KpProjekt`)
- ❌ Role-based filtering
- ❌ Email notifikace
- ❌ Audit logging
- ❌ Auto výpočty (datum konce podle frekvence)
- ❌ Default hodnoty
- ✅ Reference table lookups (máme repositories)

### Frontend (kis-frontend):
- ❌ Dropdown selectors pro reference fields
- ❌ Filtry v seznamu
- ❌ Role-based zobrazení
- ❌ Správa společností projektu
- ❌ Cash Flow management
- ❌ Memorandum
- ❌ Návrhy
- ❌ Uživatelská oprávnění

---

## Doporučený plán migrace

### Fáze 1: Core CRUD (priorita 1)
1. ✅ Projekty.jsp → ProjectListPage (HOTOVO)
2. ⏳ ProjektEdit.jsp → Enhance ProjectDetailPage
3. 🔴 **BFF: Přepsat na volání `KAP_PROJEKT.p_KpProjekt()`**
4. 🔴 Přidat dropdowny pro všechny reference fields
5. 🔴 Implementovat filtry

### Fáze 2: Cash Flow (priorita 2)
1. ProjektCashFlow.jsp → CashFlowListPage
2. ProjektCashFlowEdit.jsp → CashFlowEditModal
3. BFF: Volání `KAP_PROJEKT.p_KpProjektCashFlow()`

### Fáze 3: Ostatní funkce (priorita 3)
- Memorandum, Návrhy, Budget, atd.

---

## Další akce

1. **Analyzovat View Objects** - Najít SQL SELECT definice v BC4J
2. **Mapovat Oracle Views** - Zjistit, které views používat místo procedur
3. **Implementovat BFF s procedurami** - Zachovat 100% původní logiku
4. **Rozšířit frontend** podle JSP vzorů
