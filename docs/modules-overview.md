# KIS Banking App - Přehled Modulů

## Status Implementace

Datum: 2025-12-10

---

## ✅ HOTOVÉ MODULY (React + Spring Boot + Oracle)

### 1. **Projekty (projekt/)** - 100% ✅
**Legacy:** 161 Java souborů
**Nová implementace:**
- ✅ Frontend: ProjectListPage, ProjectDetailPage
- ✅ Backend: ProjectAggregationService, 8 REST endpointů
- ✅ Databáze: 6 tabulek (KP_DAT_PROJEKT, KP_DAT_PROJEKT_CASHFLOW, atd.)
- ✅ E2E testy: Playwright
- **Status:** Plně funkční

### 2. **Dokumenty (dokument/)** - 100% ✅
**Legacy:** 135 Java souborů
**Nová implementace:**
- ✅ Frontend: DocumentListPage (advanced), DocumentDetailPage
- ✅ Backend: DocumentAggregationService, REST endpointy
- ✅ Databáze: 4 tabulky (KP_DAT_DOKUMENT, KP_CIS_DOKUMENT_TYP, atd.)
- ✅ Schvalovací workflow (Approval Actions Modal)
- ✅ Inline editing, komentáře, timeline
- **Status:** Plně funkční

### 3. **Rozpočty (budget/)** - 100% ✅
**Legacy:** 84 Java souborů
**Nová implementace:**
- ✅ Frontend: BudgetListPage, BudgetDetailPage, BudgetDashboard
- ✅ Backend: BudgetAggregationService
- ✅ Databáze: 3 tabulky (KP_DAT_ROZPOCET, KP_DAT_ROZPOCET_POLOZKA, atd.)
- ✅ Měsíční položky (12 měsíců × rozpočet)
- ✅ Create/Edit modals
- **Status:** Plně funkční

### 4. **Dashboard** - 100% ✅
**Nová implementace:**
- ✅ Frontend: DashboardPage
- ✅ Agregovaná data ze všech modulů
- ✅ Statistiky, grafy, recent documents widget
- **Status:** Plně funkční

---

## ⏳ ROZPRACOVANÉ MODULY

### 5. **Majetek (majetek/, fininv/)** - 85% ⏳
**Legacy:** 35 Java souborů (majetek) + 27 Java souborů (fininv)
**Nová implementace:**
- ✅ Frontend: AssetCompaniesPage (OPRAVENO - nyní konzistentní design)
- ✅ Frontend: 12+ komponent v src/components/assets/
- ✅ Backend: EmissionBFFController, AssetBFFController (20 endpointů)
- ✅ Backend: EmissionAggregationService, AssetAggregationService
- ❌ **Databáze: 5 tabulek NEEXISTUJE** (SQL skripty připraveny)
- ⏳ E2E testy: 52% passing (138 testů)

**Chybí:**
- ❌ Spustit `./run_assets_migration.sh` pro vytvoření tabulek
- ❌ Přesunout zbylé komponenty z src/components/assets/ do src/pages/
- ❌ Opravit všechny komponenty na konzistentní CSS design

**Následující kroky:**
```bash
cd kis-bff-simple
./run_assets_migration.sh  # Vytvoří 5 tabulek + testovací data
```

---

## ❌ NEIMPLEMENTOVANÉ MODULY (Legacy pouze)

### 6. **IFRS Reporting (ifrs/)** - 0% ❌
**Legacy:** 54 Java souborů, 8 JSP souborů
**Účel:** IFRS reporting, mezinárodní účetní standardy
**Klíčové JSP:**
- IfrsNew.jsp
- IfrsSchvalovani.jsp
- IfrsVypoctyData.jsp
- IfrsZmeny.jsp (4 soubory)

**Priorita:** Vysoká (IFRS compliance)

### 7. **Excel Import/Export (excel/)** - 0% ❌
**Legacy:** 88 Java souborů, integrováno do jiných modulů
**Účel:** Excel import/export pro dokumenty, rozpočty, bilance
**Klíčové JSP:**
- BilanceDetailExcelProcess.jsp
- BudgetExcelProcess.jsp
- DocCisNewOdborExcelProcess.jsp

**Poznámka:** Excel funkcionalita je rozptýlená napříč moduly, ne samostatný modul

**Priorita:** Střední (utility funkce)

### 8. **Protistranové pozice (protistrany/)** - 0% ❌
**Legacy:** 65 Java souborů, 26 JSP souborů
**Účel:** Správa protistranových pozic, protistrany
**Klíčové JSP:**
- ProtiOsoby.jsp
- ProtiOsobyEdit.jsp
- ProtiOsobyKartaCetnost.jsp
- ProtiOsobyKartaPrehled.jsp
- ProtiOsobyReport*.jsp (multiple)

**Priorita:** Vysoká (rizikový management)

### 9. **Účetní skupiny (ucskup/)** - 0% ❌
**Legacy:** 162 Java souborů, 16 JSP souborů
**Účel:** Správa účetních skupin, hierarchie společností
**Klíčové JSP:**
- UcSkupDirect.jsp
- UcSkupDokladFind.jsp
- UcSkupInfo.jsp
- UcSkupPodrobna.jsp
- UcSkupRuzne*.jsp (multiple)

**Priorita:** Vysoká (core funkcionalita)

### 10. **Kapitál (kapital/)** - 0% ❌
**Legacy:** 112 Java souborů, 59 JSP souborů (!!)
**Účel:** Správa kapitálu, cenné papíry, akcie
**Klíčové JSP:**
- KapCennyPapir.jsp
- KapCennyPapirEdit.jsp
- KapCennyPapirEmise*.jsp (multiple)
- KapCennyPapirKarta.jsp
- KapCennyPapirOceneni*.jsp (multiple)

**Poznámka:** Největší modul po Dokladech (59 JSP souborů!)

**Priorita:** Velmi vysoká (velký rozsah)

### 11. **Subkonsolidace (subkons/)** - 0% ❌
**Legacy:** 96 Java souborů
**Účel:** Subkonsolidační procesy
**Klíčové JSP:**
- DokladySubkons.jsp
- DokladySubkonsFronta.jsp
- DokladySubkonsProcess.jsp

**Priorita:** Střední

### 12. **Doklady (doklady/)** - 0% ❌
**Legacy:** 448 Java souborů (!!)
**Účel:** Schvalovací workflow, dokladový systém
**Poznámka:** Největší modul v celé aplikaci

**Priorita:** Velmi vysoká (core funkcionalita)

### 13. **Evidence (evi/)** - 0% ❌
**Legacy:** 116 Java souborů
**Účel:** Evidence dodavatelů, společností, rejstříků
**Klíčové JSP:**
- EviDodavatel.jsp
- EviSpolecnost*.jsp (multiple)
- EviRejstrik*.jsp (multiple)
- EviProtistrana.jsp

**Priorita:** Vysoká (master data)

### 14. **Můstky (mustky/)** - 0% ❌
**Legacy:** 81 Java souborů
**Účel:** Účetní můstky, transformace dat
**Klíčové JSP:**
- BudgetMustky*.jsp (multiple)

**Priorita:** Střední

### 15. **Reporty (report/)** - 0% ❌
**Legacy:** 107 Java souborů
**Účel:** Reportovací systém
**Klíčové JSP:**
- ReportSpolProblemMU.jsp
- + další reporty integrované v jiných modulech

**Priorita:** Střední (lze postupně přidávat)

### 16. **Jobs (jobs/)** - 0% ❌
**Legacy:** 9 Java souborů
**Účel:** Background jobs, generátory
**Priorita:** Nízká (background processing)

### 17. **Email (mail/)** - 0% ❌
**Legacy:** 6 Java souborů
**Účel:** Email notifikace
**Klíčové JSP:**
- EmailMsg*.jsp (multiple)
- EviEmailMsg*.jsp (multiple)

**Priorita:** Nízká (utility)

### 18. **XML Export (xml/)** - 0% ❌
**Legacy:** 5 Java souborů
**Účel:** XML export dat
**Priorita:** Nízká (utility)

### 19. **CSV Import/Export (csv/)** - 0% ❌
**Legacy:** 7 Java souborů
**Účel:** CSV operace
**Priorita:** Nízká (utility)

### 20. **Cartesis Integrace (cartesis/)** - 0% ❌
**Legacy:** 34 Java souborů
**Účel:** Integrace s Cartesis systémem
**Klíčové JSP:**
- Cartesis.jsp
- CartesisProcess.jsp

**Priorita:** Závisí na aktuálním používání

### 21. **Administrace (admin/)** - 0% ❌
**Legacy:** 16 Java souborů
**Účel:** Administrace, parametry, kalendář
**Klíčové JSP:**
- DocPrintAdmin.jsp
- EviExtAdmin*.jsp (multiple)

**Priorita:** Střední

### 22. **Uživatelé a práva (users/)** - 0% ❌
**Legacy:** 42 Java souborů
**Účel:** Správa uživatelů, autorizace, role
**Priorita:** Vysoká (bezpečnost)

---

## 📊 STATISTIKA PROJEKTU

### Implementováno:
- **Frontend:** 4 moduly (Dashboard, Projekty, Dokumenty, Rozpočty)
- **Backend:** 4 moduly plně + 1 částečně (Majetek - čeká na DB)
- **Databáze:** 13 tabulek (+ 5 připraveno pro Majetek)
- **E2E testy:** 138 testů pro Majetek (52% passing)

### Zbývá implementovat:
- **17 velkých modulů** (doklady, kapital, ucskup, protistrany, atd.)
- **Odhadovaný rozsah:**
  - ~1400+ Java souborů
  - ~200+ JSP souborů
  - ~100+ databázových tabulek (odhad)

### Největší moduly k implementaci:
1. **Doklady:** 448 Java souborů
2. **Účetní skupiny:** 162 Java souborů
3. **Evidence:** 116 Java souborů
4. **Kapitál:** 112 Java souborů, 59 JSP
5. **Reporty:** 107 Java souborů

---

## 🎯 DOPORUČENÉ PRIORITY

### Fáze 1 - OKAMŽITĚ (týden):
1. ✅ ~~Opravit Majetek modul~~ (HOTOVO - AssetCompaniesPage)
2. ❌ Spustit databázovou migraci pro Majetek
3. ❌ Dokončit zbylé Assets komponenty (přesun do pages/, CSS fix)

### Fáze 2 - KRÁTKODOBĚ (měsíc):
4. ❌ Uživatelé a práva (bezpečnost)
5. ❌ Evidence (master data)
6. ❌ IFRS Reporting (compliance)

### Fáze 3 - STŘEDNĚDOBĚ (3 měsíce):
7. ❌ Účetní skupiny (core)
8. ❌ Protistranové pozice (riziko)
9. ❌ Kapitál (velký rozsah)
10. ❌ Doklady (největší modul)

### Fáze 4 - DLOUHODOBĚ (6 měsíců):
11. ❌ Subkonsolidace
12. ❌ Můstky
13. ❌ Reporty
14. ❌ Utility moduly (Excel, CSV, XML, Email, Jobs)
15. ❌ Integrace (Cartesis)
16. ❌ Administrace

---

## 📁 UMÍSTĚNÍ LEGACY KÓDU

**Java soubory:**
```
/Users/radektuma/DEV/KIS/KIS_App_64bit_JAVA17_Linux/src/main/java/cz/jtbank/konsolidace/
```

**JSP soubory:**
```
/Users/radektuma/DEV/KIS/sources/JSP/
```

---

## 🚀 DALŠÍ KROKY

1. **Opravit Majetek modul** - AssetCompaniesPage (✅ HOTOVO)
2. **Spustit databázovou migraci:**
   ```bash
   cd kis-bff-simple
   ./run_assets_migration.sh
   ```
3. **Dokončit Assets modul** - přesunout komponenty, opravit CSS
4. **Prioritizovat** - domluvit s týmem, které moduly jsou kritické
5. **Plánovat** - rozdělit práci na sprinty

---

**Poslední aktualizace:** 2025-12-10
**Autor:** Claude Code
