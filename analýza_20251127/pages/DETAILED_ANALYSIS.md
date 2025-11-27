# Detailní analýza JSP stránek

**Perspektiva:** Senior Java Developer

**Analyzováno:** 644 stránek

---

## Budgetování

*Celkem 49 stránek v této doméně*

### BudgetCreate

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/BudgetCreate.jsp`

**Primární funkce:** Vytváření nových záznamů

#### Java integrace

**Volané metody:**
- `BudgetModule.useApplicationModule()`
- `Calendar.getInstance()`
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `get()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### BudgetExcelProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/BudgetExcelProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `BudgetModule.useApplicationModule()`
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `ate()`

#### Business pravidla

- Export dat do externích formátů
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### BudgetGestor

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/BudgetGestor.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Utils.getTodaysDate()`
- `getParameter()`
- `getQueryString()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getTodaysDate
- Business služba: HtmlServices.getRequestParameters

---

### BudgetGestor2

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/BudgetGestor2.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Utils.getTodaysDate()`
- `getParameter()`
- `getQueryString()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getTodaysDate
- Business služba: HtmlServices.getRequestParameters

---

### BudgetHistorieNavyseni

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/BudgetHistorieNavyseni.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `getAttribute()`
- `getParameter()`
- `reload()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### BudgetInfo

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/BudgetInfo.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Utils.getTodaysDate()`
- `Utils.getUserName()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getTodaysDate
- Utility třída: Utils.getUserName
- Business služba: HtmlServices.getRequestParameters

---

### BudgetMustky

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/BudgetMustky.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Utils.getTodaysDate()`
- `equals()`
- `getParameter()`
- `if()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getTodaysDate
- Business služba: HtmlServices.getRequestParameters

---

### BudgetMustkyExport

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/BudgetMustkyExport.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `BudgetModule.useApplicationModule()`
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `ate()`

#### Business pravidla

- Export dat do externích formátů
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### BudgetMustkyFind

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/BudgetMustkyFind.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Utils.getTodaysDate()`
- `aDelete()`
- `aUpdate()`
- `addLine()`

#### Business pravidla

- Validace vstupních dat
- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getTodaysDate
- Business služba: HtmlServices.getRequestParameters

---

### BudgetMustkyProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/BudgetMustkyProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`
- `if()`
- `length()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### BudgetNastaveni

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/BudgetNastaveni.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `reload()`
- `set()`

#### Business pravidla

- Import dat z externích zdrojů

---

### BudgetNastaveniProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/BudgetNastaveniProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `BudgetModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `for()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### BudgetNastaveniUrovni

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/BudgetNastaveniUrovni.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `reload()`
- `set()`

#### Business pravidla

- Import dat z externích zdrojů

---

### BudgetNastaveniUrovniProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/BudgetNastaveniUrovniProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `BudgetModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `for()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### BudgetProjektCreate

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/BudgetProjektCreate.jsp`

**Primární funkce:** Vytváření nových záznamů

#### Java integrace

**Volané metody:**
- `BudgetModule.useApplicationModule()`
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `equals()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### BudgetProjektGestor

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/BudgetProjektGestor.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Utils.getTodaysDate()`
- `getParameter()`
- `getQueryString()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getTodaysDate
- Business služba: HtmlServices.getRequestParameters

---

### BudgetProjektGestor2

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/BudgetProjektGestor2.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Utils.getTodaysDate()`
- `getParameter()`
- `getQueryString()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getTodaysDate
- Business služba: HtmlServices.getRequestParameters

---

### BudgetProjektStd

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/BudgetProjektStd.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Utils.getTodaysDate()`
- `Utils.getUserName()`
- `getParameter()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getTodaysDate
- Utility třída: Utils.getUserName
- Business služba: HtmlServices.getRequestParameters

---

### BudgetProjektStd2

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/BudgetProjektStd2.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Utils.getTodaysDate()`
- `Utils.getUserName()`
- `getParameter()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getTodaysDate
- Utility třída: Utils.getUserName
- Business služba: HtmlServices.getRequestParameters

---

### BudgetProjektTransakce

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/BudgetProjektTransakce.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Databázové operace

- Čte data z KP_DAT_BUDGET (DT_PLATNOSTOD)

**Používané sloupce:**

- `KP_DAT_BUDGET`: DT_PLATNOSTOD

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getAttribute()`
- `getParameter()`
- `to_date()`
- `trunc()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### BudgetProjektTransakceDoc

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/BudgetProjektTransakceDoc.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Databázové operace

- Čte data z KP_DAT_BUDGET (DT_PLATNOSTOD)

**Používané sloupce:**

- `KP_DAT_BUDGET`: DT_PLATNOSTOD

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getAttribute()`
- `getParameter()`
- `to_date()`
- `trunc()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### BudgetProjektView

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/BudgetProjektView.jsp`

**Primární funkce:** Zobrazení dat pouze pro čtení

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Utils.getTodaysDate()`
- `Utils.getUserName()`
- `getParameter()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getTodaysDate
- Utility třída: Utils.getUserName
- Business služba: HtmlServices.getRequestParameters

---

### BudgetStd

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/BudgetStd.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Utils.getTodaysDate()`
- `Utils.getUserName()`
- `getParameter()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getTodaysDate
- Utility třída: Utils.getUserName
- Business služba: HtmlServices.getRequestParameters

---

### BudgetStd2

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/BudgetStd2.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Utils.getUserName()`
- `getParameter()`
- `getQueryString()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getUserName
- Business služba: HtmlServices.getRequestParameters

---

### BudgetStdProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/BudgetStdProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getAttribute()`
- `getParameter()`
- `getRequestURI()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### BudgetStdSchval

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/BudgetStdSchval.jsp`

**Primární funkce:** Schvalovací workflow

#### Java integrace

**Volané metody:**
- `BudgetModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getAttribute()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### BudgetStdSchval2

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/BudgetStdSchval2.jsp`

**Primární funkce:** Schvalovací workflow

#### Java integrace

**Volané metody:**
- `BudgetModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `for()`
- `getAttribute()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### BudgetStdSchvalVse

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/BudgetStdSchvalVse.jsp`

**Primární funkce:** Schvalovací workflow

#### Java integrace

**Volané metody:**
- `BudgetModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getAttribute()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### BudgetTranDruhInfo

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/BudgetTranDruhInfo.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### BudgetTranInfo

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/BudgetTranInfo.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### BudgetTransakce

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/BudgetTransakce.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getAttribute()`
- `getParameter()`
- `to_date()`
- `trunc()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### BudgetTransakceCapDoc

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/BudgetTransakceCapDoc.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getAttribute()`
- `getParameter()`
- `to_date()`
- `trunc()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### BudgetTransakceDoc

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/BudgetTransakceDoc.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getAttribute()`
- `getParameter()`
- `to_date()`
- `trunc()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### BudgetUcty

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/BudgetUcty.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### BudgetView

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/BudgetView.jsp`

**Primární funkce:** Zobrazení dat pouze pro čtení

#### Java integrace

**Volané metody:**
- `BudgetModule.useApplicationModule()`
- `BunkaData1.clearCache()`
- `BunkaData2.clearCache()`
- `BunkaData2X.clearCache()`
- `HtmlServices.getRequestParameters()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getTodaysDate
- Utility třída: Utils.getUserName
- Business služba: HtmlServices.getRequestParameters

---

### BudgetZastup

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/BudgetZastup.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `BudgetModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`
- `isUserInRole()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### BudgetZastupEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/BudgetZastupEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Utils.getTodaysDate()`
- `getParameter()`
- `length()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getTodaysDate
- Business služba: HtmlServices.getRequestParameters

---

### BudgetZastupEditProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/BudgetZastupEditProcess.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `BudgetModule.useApplicationModule()`
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Odesílání notifikací
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocBudgetDefine

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocBudgetDefine.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `DokumentModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getAttribute()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocBudgetDefineProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocBudgetDefineProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `DokumentModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `dokument()`
- `equals()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocBudgetDirect

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocBudgetDirect.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `DokumentModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `dokument()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocBudgetEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocBudgetEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `DokumentModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `for()`
- `getAttribute()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocBudgetEditProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocBudgetEditProcess.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getAttribute()`
- `getParameter()`
- `length()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocBudgetSchval

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocBudgetSchval.jsp`

**Primární funkce:** Schvalovací workflow

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getAttribute()`
- `getParameter()`
- `getRequestURI()`

#### Business pravidla

- Validace vstupních dat
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocBudgetView

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocBudgetView.jsp`

**Primární funkce:** Zobrazení dat pouze pro čtení

#### Java integrace

**Volané metody:**
- `DokumentModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getAttribute()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocCompBudgetDefine

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocCompBudgetDefine.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `DokumentModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Navigator.getInstance()`
- `getAttribute()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocCompBudgetDefineProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocCompBudgetDefineProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `DokumentModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `dokument()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocCompBudgetEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocCompBudgetEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getAttribute()`
- `getHeader()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocCompBudgetEditProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocCompBudgetEditProcess.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getAttribute()`
- `getParameter()`
- `length()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

## Dokumenty

*Celkem 70 stránek v této doméně*

### DocCis

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocCis.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `reload()`
- `set()`

---

### DocCisNewDevSpol

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocCisNewDevSpol.jsp`

**Primární funkce:** Vytváření nových záznamů

#### Databázové operace

- Čte data z KP_DEF_DEVELOPERSPOLECNOST (NULL)

**Používané sloupce:**

- `KP_DEF_DEVELOPERSPOLECNOST`: NULL

#### Business pravidla

- Import dat z externích zdrojů

---

### DocCisNewDevSpolProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocCisNewDevSpolProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `DokumentModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `defDeveloper()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocCisNewOdbor

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocCisNewOdbor.jsp`

**Primární funkce:** Vytváření nových záznamů

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocCisNewOdborEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocCisNewOdborEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocCisNewOdborEditProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocCisNewOdborEditProcess.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `equalsIgnoreCase()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocCisNewOdborExcelProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocCisNewOdborExcelProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `ate()`
- `getParameter()`
- `if()`

#### Business pravidla

- Export dat do externích formátů
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocCisNewTypTran

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocCisNewTypTran.jsp`

**Primární funkce:** Vytváření nových záznamů

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `equals()`
- `getParameter()`
- `if()`
- `isUserInRole()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocCisNewTypTranEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocCisNewTypTranEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Num.intValue()`
- `getAttribute()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocCisNewTypTranEditProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocCisNewTypTranEditProcess.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `equalsIgnoreCase()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocCisNewTypTranGroup

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocCisNewTypTranGroup.jsp`

**Primární funkce:** Vytváření nových záznamů

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `deleteId()`
- `equals()`
- `getElementById()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocCisNewTypTranGroupEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocCisNewTypTranGroupEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`
- `if()`
- `length()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocCisNewTypTranGroupEditProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocCisNewTypTranGroupEditProcess.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `equalsIgnoreCase()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocCisNewUsek

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocCisNewUsek.jsp`

**Primární funkce:** Vytváření nových záznamů

#### Java integrace

**Volané metody:**
- `deleteId()`
- `getElementById()`
- `submit()`
- `submitId()`

#### Business pravidla

- Import dat z externích zdrojů

---

### DocCisNewUsekEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocCisNewUsekEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocCisNewUsekEditProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocCisNewUsekEditProcess.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `DokumentModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `equalsIgnoreCase()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocCisStatus

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocCisStatus.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `reload()`
- `set()`

---

### DocCisStatusEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocCisStatusEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `reload()`
- `set()`

---

### DocDefine

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocDefine.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `DokumentModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Utils.getTodaysDate()`
- `getAttribute()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getTodaysDate
- Business služba: HtmlServices.getRequestParameters

---

### DocDefineProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocDefineProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `DokumentModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Utils.getTodaysDate()`
- `getAttribute()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getTodaysDate
- Business služba: HtmlServices.getRequestParameters

---

### DocEditProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocEditProcess.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getAttribute()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocEditZamit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocEditZamit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `DokumentModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getAttribute()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocEviDodavatelDirect

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocEviDodavatelDirect.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Castka.doubleValue()`
- `DokumentModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `dokument()`
- `getAttribute()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocFileUpload

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocFileUpload.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `accept()`
- `getParameter()`
- `ile()`
- `ileFilter()`

#### Business pravidla

- Import dat z externích zdrojů

---

### DocFilterSpol

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocFilterSpol.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `DokumentModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getAttribute()`
- `getParameter()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocFilterSpolProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocFilterSpolProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `DokumentModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`
- `length()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocKlonuj

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocKlonuj.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `DokumentModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocKontroling

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocKontroling.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Databázové operace

- Čte data z KP_REL_APPUSER_SPOLECNOSTPRAVO (NULL)

**Používané sloupce:**

- `KP_REL_APPUSER_SPOLECNOSTPRAVO`: NULL

#### Java integrace

**Volané metody:**
- `DokumentModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Prava.length()`
- `Utils.getDocSchvaleno()`
- `Utils.getUserName()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getDocSchvaleno
- Utility třída: Utils.getUserName
- Business služba: HtmlServices.getRequestParameters

---

### DocListExport

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocListExport.jsp`

**Primární funkce:** Seznam záznamů s možností vyhledávání

#### Java integrace

**Volané metody:**
- `DokumentModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `excelOutput()`
- `getExportRedir()`
- `getParameter()`

#### Business pravidla

- Export dat do externích formátů
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocMisCisUcet

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocMisCisUcet.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `aDelete()`
- `getElementById()`
- `getParameter()`
- `if()`

#### Business pravidla

- Validace vstupních dat
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocMisCisUcetProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocMisCisUcetProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocObj2Schval

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocObj2Schval.jsp`

**Primární funkce:** Schvalovací workflow

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getAttribute()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocObj2SchvalRadky

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocObj2SchvalRadky.jsp`

**Primární funkce:** Schvalovací workflow

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `append()`
- `for()`
- `getAttribute()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocObj2SchvalRadkyProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocObj2SchvalRadkyProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `DokumentModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `for()`
- `getAttribute()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocOdbor

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocOdbor.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `DokumentModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocOdeslane

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocOdeslane.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `DokumentModule.useApplicationModule()`
- `getQueryString()`
- `getRequestURI()`
- `getWhereOdeslane()`
- `if()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

---

### DocPrijate

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocPrijate.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `InetAddress.getLocalHost()`
- `Navigator.getInstance()`
- `equals()`
- `getHostName()`
- `set()`

#### Business pravidla

- Import dat z externích zdrojů

---

### DocPrijateFast

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocPrijateFast.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Databázové operace

- Čte data z KTG_APPUSER (ID)

**Používané sloupce:**

- `KTG_APPUSER`: ID

#### Java integrace

**Volané metody:**
- `chvalovaniQuery()`
- `getName()`
- `getUserPrincipal()`
- `indexOf()`
- `setCurrentUserId()`

#### Business pravidla

- Import dat z externích zdrojů

---

### DocPrijateFast2

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocPrijateFast2.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `set()`

---

### DocPrijatePuvodni

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocPrijatePuvodni.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `DokumentModule.useApplicationModule()`
- `getQueryString()`
- `getRequestURI()`
- `getWhereGestorovane()`
- `getWherePrijate()`

#### Business pravidla

- Import dat z externích zdrojů

---

### DocPrint

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocPrint.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocPrintAdmin

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocPrintAdmin.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocPrintLikvidacka

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocPrintLikvidacka.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocPrintMajetek

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocPrintMajetek.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocSchval

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocSchval.jsp`

**Primární funkce:** Schvalovací workflow

#### Databázové operace

- Čte data z DUAL (1)

**Používané sloupce:**

- `DUAL`: 1

#### Java integrace

**Volané metody:**
- `Class.forName()`
- `getAttribute()`
- `getRequestURI()`
- `setAttribute()`

#### Business pravidla

- Import dat z externích zdrojů

---

### DocTest

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocTest.jsp`

**Primární funkce:** Obecná business funkcionalita

---

### DocUcetni

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocUcetni.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Databázové operace

- Čte data z KP_DAT_DOCZASTUP (ID_KOHO)
- Čte data z KP_DAT_DOKUMENTRADEK (NULL)

**Používané sloupce:**

- `KP_DAT_DOCZASTUP`: ID_KOHO
- `KP_DAT_DOKUMENTRADEK`: NULL

#### Java integrace

**Volané metody:**
- `DokumentModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Utils.getDocSchvaleno()`
- `currentUsersId()`
- `getParameter()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getDocSchvaleno
- Business služba: HtmlServices.getRequestParameters

---

### DocUrovneMis

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocUrovneMis.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `DokumentModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Navigator.getInstance()`
- `getParameter()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocUrovneMisProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocUrovneMisProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `DokumentModule.useApplicationModule()`
- `Formater.parseDouble()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `for()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocView

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocView.jsp`

**Primární funkce:** Zobrazení dat pouze pro čtení

#### Java integrace

**Volané metody:**
- `DokumentModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getAttribute()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocViewProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocViewProcess.jsp`

**Primární funkce:** Zobrazení dat pouze pro čtení

#### Java integrace

**Volané metody:**
- `DokumentModule.useApplicationModule()`
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getAttribute()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocZadavani

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocZadavani.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Databázové operace

- Čte data z KP_DAT_DOCZASTUP (ID_KOHO)

**Používané sloupce:**

- `KP_DAT_DOCZASTUP`: ID_KOHO

#### Java integrace

**Volané metody:**
- `DokumentModule.useApplicationModule()`
- `currentUsersId()`
- `getQueryString()`
- `getRequestURI()`
- `if()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

---

### DocZadavaniDefine

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocZadavaniDefine.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Utils.getTodaysDate()`
- `getAttribute()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getTodaysDate
- Business služba: HtmlServices.getRequestParameters

---

### DocZadavaniDefineProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocZadavaniDefineProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocZadavaniEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocZadavaniEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `DokumentModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Utils.getTodaysDate()`
- `getAttribute()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getTodaysDate
- Business služba: HtmlServices.getRequestParameters

---

### DocZadavaniEditProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocZadavaniEditProcess.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocZastup

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocZastup.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `DokumentModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`
- `isUserInRole()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DocZastupEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocZastupEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Utils.getTodaysDate()`
- `getParameter()`
- `length()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getTodaysDate
- Business služba: HtmlServices.getRequestParameters

---

### DocZastupEditProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DocZastupEditProcess.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`
- `length()`

#### Business pravidla

- Odesílání notifikací
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### PbDocELK

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/PbDocELK.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### PbDocLRK

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/PbDocLRK.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### PbDocUcet

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/PbDocUcet.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### PbDocUcetProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/PbDocUcetProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`
- `length()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ReportDoc

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ReportDoc.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `reload()`
- `set()`

---

### ReportDocBil

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ReportDocBil.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getElementById()`
- `getParameter()`
- `getWindowHeight()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ReportDocNezauctovano

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ReportDocNezauctovano.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `getParameter()`
- `if()`
- `reload()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ReportDocSpol

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ReportDocSpol.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `getParameter()`
- `if()`
- `reload()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ReportDocUser

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ReportDocUser.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `equals()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ReportDocVN

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ReportDocVN.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `equals()`
- `getParameter()`
- `getWindowHeight()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

## Evidence

*Celkem 30 stránek v této doméně*

### EviDodavatel

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/EviDodavatel.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `confirm()`
- `deleteIt()`
- `if()`
- `isUserInRole()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

---

### EviDodavatelNaklad

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/EviDodavatelNaklad.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `EviModule.useApplicationModule()`
- `isUserInRole()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

---

### EviDodavatelNakladProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/EviDodavatelNakladProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `EviModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### EviDodavatelProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/EviDodavatelProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `EviModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `equals()`
- `eviExtDodavatel()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### EviEmailMsg

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/EviEmailMsg.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `getElementById()`
- `reload()`
- `set()`
- `setDeleteUser()`

---

### EviEmailMsgNavrh

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/EviEmailMsgNavrh.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `EviModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Spol.length()`
- `addLine()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### EviEmailMsgNavrhProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/EviEmailMsgNavrhProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Spol.length()`
- `ashSet()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### EviEmailMsgProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/EviEmailMsgProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `EviModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Module.insertEmailUser()`
- `getParameter()`

#### Business pravidla

- Odesílání notifikací
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### EviEmailMsgSend

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/EviEmailMsgSend.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Agent.toLowerCase()`
- `EviModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Ucspol.length()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### EviEmailMsgSendProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/EviEmailMsgSendProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Ucspol.length()`
- `ashSet()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### EviExtAdmin

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/EviExtAdmin.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `EviModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Id.length()`
- `getParameter()`
- `if()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### EviExtAdminEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/EviExtAdminEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### EviExtAdminEditProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/EviExtAdminEditProcess.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `equalsIgnoreCase()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### EviExtAdminSpolecnost

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/EviExtAdminSpolecnost.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Id.length()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### EviInfoProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/EviInfoProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`
- `if()`
- `startsWith()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### EviInfoUpload

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/EviInfoUpload.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Type.indexOf()`
- `ataInputStream()`
- `getContentType()`
- `getInputStream()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

---

### EviPodpisoveVzory

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/EviPodpisoveVzory.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `getParameter()`
- `reload()`
- `set()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### EviProtistrana

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/EviProtistrana.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Param.length()`
- `close()`
- `getElementById()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### EviRejstrik

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/EviRejstrik.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `EviModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`
- `isCurrentUserAdministrator()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### EviRejstrik3M

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/EviRejstrik3M.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `getElementById()`
- `getWindowHeight()`
- `if()`
- `rebuildWindow()`

#### Business pravidla

- Import dat z externích zdrojů

---

### EviRejstrik3MExport

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/EviRejstrik3MExport.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `EviModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `excelOutput()`
- `getExportRedir()`
- `xportEviOR3M()`

#### Business pravidla

- Export dat do externích formátů
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### EviRejstrikEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/EviRejstrikEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Utils.getTodaysDate()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters
- Utility třída: Utils.getTodaysDate

---

### EviRejstrikEditProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/EviRejstrikEditProcess.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `equalsIgnoreCase()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### EviSpolecnost

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/EviSpolecnost.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `EviModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Id.length()`
- `getParameter()`
- `if()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### EviSpolecnostEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/EviSpolecnostEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Utils.getTodaysDate()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getTodaysDate
- Business služba: HtmlServices.getRequestParameters

---

### EviSpolecnostEditProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/EviSpolecnostEditProcess.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `equalsIgnoreCase()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### EviSpolecnostHistorie

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/EviSpolecnostHistorie.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### EviSpolecnostHlavniHistorie

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/EviSpolecnostHlavniHistorie.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `getParameter()`
- `reload()`
- `set()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### EviSpolecnostView

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/EviSpolecnostView.jsp`

**Primární funkce:** Zobrazení dat pouze pro čtení

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Utils.getTodaysDate()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getTodaysDate
- Business služba: HtmlServices.getRequestParameters

---

### EviSpolecnostViewSimple

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/EviSpolecnostViewSimple.jsp`

**Primární funkce:** Zobrazení dat pouze pro čtení

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Navigator.getInstance()`
- `Utils.getTodaysDate()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getTodaysDate
- Business služba: HtmlServices.getRequestParameters

---

## IFRS reporting

*Celkem 8 stránek v této doméně*

### IfrsNew

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/IfrsNew.jsp`

**Primární funkce:** Vytváření nových záznamů

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `Utils.getLastDateAsString()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getLastDateAsString
- Business služba: HtmlServices.getRequestParameters

---

### IfrsNewPravidla

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/IfrsNewPravidla.jsp`

**Primární funkce:** Vytváření nových záznamů

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getAttribute()`
- `getParameter()`
- `if()`
- `isUserInRole()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### IfrsNewPravidloDetail

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/IfrsNewPravidloDetail.jsp`

**Primární funkce:** Vytváření nových záznamů

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Utils.getUserName()`
- `getAttribute()`
- `getParameter()`
- `if()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getUserName
- Business služba: HtmlServices.getRequestParameters

---

### IfrsNewPravidloDetailProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/IfrsNewPravidloDetailProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### IfrsNewPravidloHist

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/IfrsNewPravidloHist.jsp`

**Primární funkce:** Vytváření nových záznamů

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Utils.getUserName()`
- `getAttribute()`
- `getParameter()`
- `if()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getUserName
- Business služba: HtmlServices.getRequestParameters

---

### IfrsNewPravidloHistProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/IfrsNewPravidloHistProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### IfrsNewPravidloProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/IfrsNewPravidloProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### IfrsNewProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/IfrsNewProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `getParameter()`
- `if()`

#### Business pravidla

- Export dat do externích formátů
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

## Obecné

*Celkem 383 stránek v této doméně*

### .precompileJSPs

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/.precompileJSPs.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Context.getServletContext()`
- `compileAllJsps()`
- `endsWith()`
- `flush()`
- `for()`

#### Business pravidla

- Import dat z externích zdrojů

---

### Alert

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/Alert.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `alert()`
- `ate()`
- `equals()`
- `getHours()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### Archiv

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/Archiv.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Module.getPrilohy()`
- `UcSkupModule.useApplicationModule()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ArchivFileUpload

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ArchivFileUpload.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `Type.indexOf()`
- `Type.lastIndexOf()`
- `Type.length()`
- `Type.substring()`

#### Business pravidla

- Import dat z externích zdrojů

---

### ArchivFoldersCreate

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ArchivFoldersCreate.jsp`

**Primární funkce:** Vytváření nových záznamů

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `getCurrentLink()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

---

### ArchivView

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ArchivView.jsp`

**Primární funkce:** Zobrazení dat pouze pro čtení

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Module.getPrilohy()`
- `UcSkupModule.useApplicationModule()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### AutomatLog

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/AutomatLog.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Runtime.getRuntime()`
- `SystemStatus.getInstance()`
- `freeMemory()`
- `getStatusHtml()`
- `maxMemory()`

---

### Bilance

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/Bilance.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `getParameter()`
- `reload()`
- `set()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### BilanceDetail

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/BilanceDetail.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `equals()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### BilanceDetailExcelProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/BilanceDetailExcelProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `DokladyModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `exportExcelBD()`
- `getParameter()`
- `if()`

#### Business pravidla

- Export dat do externích formátů
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### BilanceProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/BilanceProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `DokladyModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `getParameter()`
- `getPreviousLink()`

#### Business pravidla

- Export dat do externích formátů
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### Cartesis

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/Cartesis.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `Utils.getLastDateMB()`
- `reload()`
- `set()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getLastDateMB
- Business služba: HtmlServices.getRequestParameters

---

### CartesisProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/CartesisProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `CartesisModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### CisMngSegment

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/CisMngSegment.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `decode()`
- `deleteId()`
- `getElementById()`
- `submitId()`

#### Business pravidla

- Import dat z externích zdrojů

---

### CisMngSegmentBoss

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/CisMngSegmentBoss.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `deleteId()`
- `getElementById()`
- `submit()`
- `submitId()`

#### Business pravidla

- Import dat z externích zdrojů

---

### CisMngSegmentBossEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/CisMngSegmentBossEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### CisMngSegmentBossEditProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/CisMngSegmentBossEditProcess.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `IfrsModule.useApplicationModule()`
- `Integer.parseInt()`
- `equalsIgnoreCase()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### CisMngSegmentEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/CisMngSegmentEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getAttribute()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### CisMngSegmentEditProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/CisMngSegmentEditProcess.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `IfrsModule.useApplicationModule()`
- `Integer.parseInt()`
- `equalsIgnoreCase()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### CisQVazbaProdukt

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/CisQVazbaProdukt.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `aDelete()`
- `aUpdate()`
- `addLine()`
- `createElement()`

#### Business pravidla

- Validace vstupních dat
- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### CisQVazbaProduktProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/CisQVazbaProduktProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `DokladyModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `equalsIgnoreCase()`
- `for()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### CisSegment

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/CisSegment.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `reload()`
- `set()`

---

### DanoveSazby

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DanoveSazby.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Utils.getTodaysDate()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters
- Utility třída: Utils.getTodaysDate

---

### DanoveSazbyProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DanoveSazbyProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `UcSkupModule.useApplicationModule()`
- `for()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DataEditComponent

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DataEditComponent.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DataHandlerComponent

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DataHandlerComponent.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getParameter()`
- `name()`

#### Business pravidla

- Transakční zpracování
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DataRecordComponent

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DataRecordComponent.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getParameter()`
- `if()`

#### Business pravidla

- Validace vstupních dat
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DataScrollerComponent

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DataScrollerComponent.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DataTableComponent

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DataTableComponent.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `equalsIgnoreCase()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DataTransactionComponent

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DataTransactionComponent.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Ref.useApplicationModule()`
- `Utils.getAMRefFromContext()`
- `getParameter()`
- `getTransaction()`

#### Business pravidla

- Transakční zpracování
- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getAMRefFromContext
- Business služba: HtmlServices.getRequestParameters

---

### EmailMsg

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/EmailMsg.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Cis.getAttribute()`
- `Navigator.getInstance()`
- `getElementById()`
- `intValue()`
- `reload()`

#### Business pravidla

- Odesílání notifikací

---

### EmailMsgProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/EmailMsgProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `UsersModule.useApplicationModule()`
- `getParameter()`
- `if()`

#### Business pravidla

- Odesílání notifikací
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### FileDelete

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/FileDelete.jsp`

**Primární funkce:** Mazání záznamů

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `URLDecoder.decode()`
- `delete()`
- `exists()`
- `getAttribute()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### FileDeleteOwn

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/FileDeleteOwn.jsp`

**Primární funkce:** Mazání záznamů

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `URLDecoder.decode()`
- `delete()`
- `exists()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### FininvEmiseHist

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/FininvEmiseHist.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `getParameter()`
- `reload()`
- `set()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### FininvEmiseHistProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/FininvEmiseHistProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `FininvModule.useApplicationModule()`
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### FininvEmiseProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/FininvEmiseProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `FininvModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### FininvInvestice

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/FininvInvestice.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `aDelete()`
- `getElementById()`
- `getParameter()`

#### Business pravidla

- Validace vstupních dat
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### FininvInvesticeExcelProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/FininvInvesticeExcelProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `FininvModule.useApplicationModule()`
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `ate()`
- `exportExcelEmise()`

#### Business pravidla

- Export dat do externích formátů
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### FininvProtistrany

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/FininvProtistrany.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `alert()`
- `getElementById()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### FininvPsProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/FininvPsProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `FininvModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`
- `insertFinInv()`

#### Business pravidla

- Transakční zpracování
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### GestorTransakceSegment

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/GestorTransakceSegment.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Utils.getUserName()`
- `getParameter()`
- `getQueryString()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getUserName
- Business služba: HtmlServices.getRequestParameters

---

### GestorTransakceSegmentProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/GestorTransakceSegmentProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `DokumentModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `for()`
- `getAttribute()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### GestorTransakceSpolecnost

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/GestorTransakceSpolecnost.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Utils.getUserName()`
- `getParameter()`
- `getQueryString()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getUserName
- Business služba: HtmlServices.getRequestParameters

---

### GestorTransakceSpolecnostProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/GestorTransakceSpolecnostProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `DokumentModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `for()`
- `getAttribute()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### Intro

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/Intro.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `SystemStatus.getInstance()`
- `getName()`
- `getStatus()`
- `getUserPrincipal()`

#### Business pravidla

- Kontrola oprávnění uživatele

---

### Intro2

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/Intro2.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `reload()`
- `set()`

---

### Intro3

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/Intro3.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `getParameter()`
- `reload()`
- `set()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### Intro4

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/Intro4.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `reload()`
- `set()`

---

### KamilExport

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KamilExport.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `DokladyModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Rozkladu.length()`
- `excelOutput()`

#### Business pravidla

- Export dat do externích formátů
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### KamilPrehledObdobi

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KamilPrehledObdobi.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `getParameter()`
- `if()`
- `length()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### KamilPrehledObdobi2

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KamilPrehledObdobi2.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getElementById()`
- `getParameter()`
- `if()`
- `length()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### KamilPrehledProtistrana

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KamilPrehledProtistrana.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `Zahlavi.getAttribute()`
- `getElementById()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### KamilPrehledRozklad

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KamilPrehledRozklad.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getElementById()`
- `getParameter()`
- `if()`
- `length()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### KamilSubkonPrehled

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KamilSubkonPrehled.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getElementById()`
- `getParameter()`
- `if()`
- `length()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### KapCennyPapir

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapCennyPapir.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Param.length()`
- `aUpdate()`
- `getElementById()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### KapCennyPapirProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapCennyPapirProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `equals()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### KapCisBusinessLinie

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapCisBusinessLinie.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `deleteId()`
- `getElementById()`
- `submit()`
- `submitId()`

#### Business pravidla

- Import dat z externích zdrojů

---

### KapCisBusinessLinieEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapCisBusinessLinieEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getAttribute()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### KapCisBusinessLinieEditProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapCisBusinessLinieEditProcess.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `Formater.parseDouble()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `KapitalModule.useApplicationModule()`
- `equalsIgnoreCase()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### KapCisCPIndex

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapCisCPIndex.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `deleteId()`
- `getElementById()`
- `submit()`
- `submitId()`

#### Business pravidla

- Import dat z externích zdrojů

---

### KapCisCPIndexEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapCisCPIndexEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getAttribute()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### KapCisCPIndexEditProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapCisCPIndexEditProcess.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `KapitalModule.useApplicationModule()`
- `cpIndex()`
- `equalsIgnoreCase()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### KapCisExterniRating

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapCisExterniRating.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `deleteId()`
- `getElementById()`
- `submit()`
- `submitId()`

#### Business pravidla

- Import dat z externích zdrojů

---

### KapCisExterniRatingEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapCisExterniRatingEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getAttribute()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### KapCisExterniRatingEditProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapCisExterniRatingEditProcess.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `Formater.parseDouble()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `KapitalModule.useApplicationModule()`
- `equalsIgnoreCase()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### KapCisIndustryType

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapCisIndustryType.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `deleteId()`
- `getElementById()`
- `submit()`
- `submitId()`

#### Business pravidla

- Import dat z externích zdrojů

---

### KapCisIndustryTypeEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapCisIndustryTypeEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `equals()`
- `getAttribute()`
- `getParameter()`
- `if()`

#### Business pravidla

- Validace vstupních dat
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### KapCisIndustryTypeEditProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapCisIndustryTypeEditProcess.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `KapitalModule.useApplicationModule()`
- `equalsIgnoreCase()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### KapCisKategorieProtistran

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapCisKategorieProtistran.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `deleteId()`
- `getElementById()`
- `submit()`
- `submitId()`

#### Business pravidla

- Import dat z externích zdrojů

---

### KapCisKategorieProtistranEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapCisKategorieProtistranEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `equals()`
- `getAttribute()`
- `getParameter()`

#### Business pravidla

- Validace vstupních dat
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### KapCisKategorieProtistranEditProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapCisKategorieProtistranEditProcess.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `equalsIgnoreCase()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### KapCisNastrojOperace

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapCisNastrojOperace.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `deleteId()`
- `getElementById()`
- `reload()`
- `set()`

#### Business pravidla

- Import dat z externích zdrojů

---

### KapCisNastrojOperaceEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapCisNastrojOperaceEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### KapCisNastrojOperaceEditProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapCisNastrojOperaceEditProcess.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `equalsIgnoreCase()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### KapCisSYU590

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapCisSYU590.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `deleteId()`
- `getElementById()`
- `submit()`
- `submitId()`

#### Business pravidla

- Import dat z externích zdrojů

---

### KapCisSYU590Edit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapCisSYU590Edit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### KapCisSYU590EditProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapCisSYU590EditProcess.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `KapitalModule.useApplicationModule()`
- `equalsIgnoreCase()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### KapCisTypNastroje

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapCisTypNastroje.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `deleteId()`
- `getAttribute()`
- `getElementById()`
- `reload()`

#### Business pravidla

- Import dat z externích zdrojů

---

### KapCisTypNastrojeEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapCisTypNastrojeEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getAttribute()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### KapCisTypNastrojeEditProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapCisTypNastrojeEditProcess.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `equalsIgnoreCase()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### KapCisTypOperace

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapCisTypOperace.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `deleteId()`
- `getElementById()`
- `submit()`
- `submitId()`

#### Business pravidla

- Import dat z externích zdrojů

---

### KapCisTypOperaceEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapCisTypOperaceEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### KapCisTypOperaceEditProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapCisTypOperaceEditProcess.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `equalsIgnoreCase()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### KapCisTypZajisteni

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapCisTypZajisteni.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `deleteId()`
- `getElementById()`
- `submit()`
- `submitId()`

#### Business pravidla

- Import dat z externích zdrojů

---

### KapCisTypZajisteniEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapCisTypZajisteniEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Navigator.getInstance()`
- `Num.intValue()`
- `getAttribute()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### KapCisTypZajisteniEditProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapCisTypZajisteniEditProcess.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `KapitalModule.useApplicationModule()`
- `equalsIgnoreCase()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### KapCisUcelUveru

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapCisUcelUveru.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `deleteId()`
- `getElementById()`
- `submit()`
- `submitId()`

#### Business pravidla

- Import dat z externích zdrojů

---

### KapCisUcelUveruEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapCisUcelUveruEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `equals()`
- `getAttribute()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### KapCisUcelUveruEditProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapCisUcelUveruEditProcess.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `KapitalModule.useApplicationModule()`
- `equalsIgnoreCase()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### KapDuplicity

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapDuplicity.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Navigator.getInstance()`
- `Utils.getLastDateMB()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getLastDateMB
- Business služba: HtmlServices.getRequestParameters

---

### KapDuplicityDetailView

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapDuplicityDetailView.jsp`

**Primární funkce:** Zobrazení dat pouze pro čtení

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getParameter()`
- `getWindowHeight()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### KapDuplicityView

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapDuplicityView.jsp`

**Primární funkce:** Zobrazení dat pouze pro čtení

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`
- `length()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### KapMustky

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapMustky.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Locale.length()`
- `Utils.getLocale()`
- `Utils.setLocale()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getLocale
- Utility třída: Utils.setLocale
- Business služba: HtmlServices.getRequestParameters

---

### KapMustkyProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapMustkyProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### KapORCalculate

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapORCalculate.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Utils.getLastDateMB()`
- `if()`
- `isUserInRole()`
- `sendError()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů
- Výpočty a kalkulace

#### Integrační body

- Utility třída: Utils.getLastDateMB

---

### KapORCalculateProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapORCalculateProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `KapitalModule.useApplicationModule()`
- `Spol.length()`

#### Business pravidla

- Import dat z externích zdrojů
- Výpočty a kalkulace

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### KapORDisplay

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapORDisplay.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Formater.formatDate()`
- `getAttribute()`
- `getElementById()`
- `if()`
- `switchMe()`

#### Business pravidla

- Import dat z externích zdrojů

---

### KapOperacniRiziko

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapOperacniRiziko.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Business pravidla

- Export dat do externích formátů
- Import dat z externích zdrojů

---

### KapOperacniRizikoExport

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapOperacniRizikoExport.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `KapitalModule.useApplicationModule()`
- `Spol.length()`

#### Business pravidla

- Export dat do externích formátů
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### KapSpolecnost

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapSpolecnost.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Databázové operace

- Čte data z KP_REL_SPOLECNOSTEXTERNIRATING (NULL)

**Používané sloupce:**

- `KP_REL_SPOLECNOSTEXTERNIRATING`: NULL

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Param.length()`
- `aUpdate()`
- `getElementById()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### KapSpolecnostProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapSpolecnostProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `equals()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### KapUcSpol

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapUcSpol.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `Spol.getAttribute()`
- `if()`
- `reload()`
- `set()`

#### Business pravidla

- Import dat z externích zdrojů

---

### KapUcSpolProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapUcSpolProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `KapitalModule.useApplicationModule()`
- `for()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### KurzListek

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KurzListek.jsp`

**Primární funkce:** Seznam záznamů s možností vyhledávání

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Utils.getLastDateAsString()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters
- Utility třída: Utils.getLastDateAsString

---

### KurzListekProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KurzListekProcess.jsp`

**Primární funkce:** Seznam záznamů s možností vyhledávání

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### LeftMenu

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/LeftMenu.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Constants.getHostName()`
- `equalsIgnoreCase()`

---

### Location

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/Location.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `alert()`
- `for()`
- `function()`
- `getElementsByTagName()`
- `hideselectboxes()`

---

### Logs

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/Logs.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `DokladyModule.useApplicationModule()`
- `Module.getLogFiles()`
- `Navigator.getInstance()`
- `isUserInRole()`
- `print()`

#### Business pravidla

- Kontrola oprávnění uživatele

---

### LogsDelete

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/LogsDelete.jsp`

**Primární funkce:** Mazání záznamů

#### Java integrace

**Volané metody:**
- `Del.delete()`
- `HtmlServices.getRequestParameters()`
- `getParameter()`
- `ile()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### LogsSubkonsDetail

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/LogsSubkonsDetail.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Databázové operace

- Čte data z KP_DAT_DOKLAD (ID)

**Používané sloupce:**

- `KP_DAT_DOKLAD`: ID

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `getParameter()`
- `set()`
- `to_date()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### Majetek

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/Majetek.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `Utils.getUserName()`
- `getName()`
- `getUserPrincipal()`
- `indexOf()`

#### Integrační body

- Utility třída: Utils.getUserName

---

### MajetekCis

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/MajetekCis.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `reload()`
- `set()`

---

### MajetekCisTypTran

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/MajetekCisTypTran.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `reload()`
- `set()`

---

### MajetekCisTypTranEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/MajetekCisTypTranEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `reload()`
- `set()`

---

### MajetekCisZpusob

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/MajetekCisZpusob.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `reload()`
- `set()`

---

### MajetekCisZpusobEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/MajetekCisZpusobEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `reload()`
- `set()`

---

### MajetekKontrola

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/MajetekKontrola.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`
- `length()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### MajetekKontrolaProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/MajetekKontrolaProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### MajetekPrehled

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/MajetekPrehled.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Formater.formatDate()`
- `HtmlServices.getRequestParameters()`
- `MajetekModule.useApplicationModule()`
- `Param.getAttribute()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### MajetekPrehledFiltr

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/MajetekPrehledFiltr.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `MajetekModule.useApplicationModule()`
- `Utils.getLastDateMB()`
- `Utils.getTodaysDate()`
- `getEmiseMap()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getTodaysDate
- Utility třída: Utils.getLastDateMB
- Business služba: HtmlServices.getRequestParameters

---

### MajetekPrehledProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/MajetekPrehledProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Str.length()`
- `getParameter()`

#### Business pravidla

- Export dat do externích formátů
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### MajetekPrehledSum

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/MajetekPrehledSum.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Formater.formatDate()`
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `MajetekModule.useApplicationModule()`
- `Navigator.getInstance()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getUserName
- Business služba: HtmlServices.getRequestParameters

---

### MajetekPrehledSumRev

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/MajetekPrehledSumRev.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `MajetekModule.useApplicationModule()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### MajetekUcastHist

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/MajetekUcastHist.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `getParameter()`
- `reload()`
- `set()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### MajetekUcastHistProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/MajetekUcastHistProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### MajetekUcastProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/MajetekUcastProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Validace vstupních dat
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### MajetekUcasti

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/MajetekUcasti.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `V.length()`
- `getParameter()`
- `if()`

#### Business pravidla

- Validace vstupních dat
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ManualniAkce

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ManualniAkce.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `getParameter()`
- `if()`
- `reload()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ManualniAkceProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ManualniAkceProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### Mazani

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/Mazani.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `equals()`
- `for()`
- `getAbsolutePath()`
- `getName()`
- `if()`

#### Business pravidla

- Validace vstupních dat
- Import dat z externích zdrojů

---

### MazaniProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/MazaniProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `delete()`
- `equals()`
- `for()`
- `getAbsolutePath()`
- `getName()`

#### Business pravidla

- Import dat z externích zdrojů

---

### MazaniProcess2

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/MazaniProcess2.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `delete()`
- `deleteFiles()`
- `equals()`
- `for()`
- `getAbsolutePath()`

#### Business pravidla

- Import dat z externích zdrojů

---

### MilosExport

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/MilosExport.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `DokladyModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Rozkladu.length()`
- `excelOutput()`

#### Business pravidla

- Export dat do externích formátů
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### MilosPrehledObdobi2

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/MilosPrehledObdobi2.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getElementById()`
- `getParameter()`
- `if()`
- `length()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### MilosPrehledProtistrana

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/MilosPrehledProtistrana.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `Zahlavi.getAttribute()`
- `getElementById()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### MilosPrehledRozklad

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/MilosPrehledRozklad.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getElementById()`
- `getParameter()`
- `if()`
- `length()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### Mustky

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/Mustky.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Locale.length()`
- `Typ.length()`
- `Utils.getLocale()`
- `Utils.getTodaysDate()`

#### Business pravidla

- Export dat do externích formátů
- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getTodaysDate
- Utility třída: Utils.getUserName
- Utility třída: Utils.getLocale

---

### Mustky2007

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/Mustky2007.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Doklad.length()`
- `HtmlServices.getRequestParameters()`
- `Locale.length()`
- `Typ.length()`
- `Utils.getLocale()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Export dat do externích formátů
- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getTodaysDate
- Utility třída: Utils.getLocale
- Utility třída: Utils.setLocale

---

### Mustky2007Find

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/Mustky2007Find.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Doklad.length()`
- `HtmlServices.getRequestParameters()`
- `Typ.length()`
- `Utils.getLocale()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getLocale
- Business služba: HtmlServices.getRequestParameters

---

### Mustky2007Process

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/Mustky2007Process.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### MustkyBunkaHist

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/MustkyBunkaHist.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Utils.getUserName()`
- `getParameter()`
- `getUserPrincipal()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getUserName
- Business služba: HtmlServices.getRequestParameters

---

### MustkyBunkaHistProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/MustkyBunkaHistProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Utils.getUserName()`
- `equals()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getUserName
- Business služba: HtmlServices.getRequestParameters

---

### MustkyExport

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/MustkyExport.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `System.currentTimeMillis()`
- `Utils.getUserName()`
- `ate()`

#### Business pravidla

- Export dat do externích formátů
- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getUserName
- Business služba: HtmlServices.getRequestParameters

---

### MustkyExport2007

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/MustkyExport2007.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `System.currentTimeMillis()`
- `ate()`
- `excelOutput()`

#### Business pravidla

- Export dat do externích formátů
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### MustkyExportUnif

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/MustkyExportUnif.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `System.currentTimeMillis()`
- `ate()`
- `getParameter()`

#### Business pravidla

- Export dat do externích formátů
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### MustkyFind

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/MustkyFind.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Typ.length()`
- `Utils.getLocale()`
- `Utils.getUserName()`
- `equals()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getUserName
- Utility třída: Utils.getLocale
- Business služba: HtmlServices.getRequestParameters

---

### MustkyPodnikatel

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/MustkyPodnikatel.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Locale.length()`
- `Typ.length()`
- `Utils.getLocale()`
- `Utils.getTodaysDate()`

#### Business pravidla

- Export dat do externích formátů
- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getTodaysDate
- Utility třída: Utils.getUserName
- Utility třída: Utils.getLocale

---

### MustkyPodnikatel2010

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/MustkyPodnikatel2010.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Locale.length()`
- `Typ.length()`
- `Utils.getLocale()`
- `Utils.getTodaysDate()`

#### Business pravidla

- Export dat do externích formátů
- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getTodaysDate
- Utility třída: Utils.getUserName
- Utility třída: Utils.getLocale

---

### MustkyPodnikatel2010Find

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/MustkyPodnikatel2010Find.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Typ.length()`
- `Utils.getLocale()`
- `Utils.getUserName()`
- `equals()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getUserName
- Utility třída: Utils.getLocale
- Business služba: HtmlServices.getRequestParameters

---

### MustkyPodnikatel2010Process

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/MustkyPodnikatel2010Process.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Utils.getUserName()`
- `equals()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getUserName
- Business služba: HtmlServices.getRequestParameters

---

### MustkyPodnikatelExport

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/MustkyPodnikatelExport.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `System.currentTimeMillis()`
- `Utils.getUserName()`
- `ate()`

#### Business pravidla

- Export dat do externích formátů
- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getUserName
- Business služba: HtmlServices.getRequestParameters

---

### MustkyPodnikatelFind

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/MustkyPodnikatelFind.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Typ.length()`
- `Utils.getLocale()`
- `Utils.getUserName()`
- `equals()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getUserName
- Utility třída: Utils.getLocale
- Business služba: HtmlServices.getRequestParameters

---

### MustkyPodnikatelProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/MustkyPodnikatelProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Utils.getUserName()`
- `equals()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getUserName
- Business služba: HtmlServices.getRequestParameters

---

### MustkyProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/MustkyProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Utils.getUserName()`
- `equals()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getUserName
- Business služba: HtmlServices.getRequestParameters

---

### ParametryCommit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ParametryCommit.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `catch()`
- `getParameter()`
- `if()`

#### Business pravidla

- Transakční zpracování
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ParametryEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ParametryEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `Row.getAttribute()`
- `equals()`
- `eval()`
- `if()`

#### Business pravidla

- Validace vstupních dat
- Kontrola oprávnění uživatele
- Transakční zpracování

---

### PoziceCashFlowExport

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/PoziceCashFlowExport.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `DokladyModule.useApplicationModule()`
- `excelOutput()`
- `getExportRedir()`
- `xportCashFlow()`

#### Business pravidla

- Export dat do externích formátů
- Import dat z externích zdrojů

---

### PoziceKapital

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/PoziceKapital.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `aDelete()`
- `aUpdate()`
- `getElementById()`
- `if()`

#### Business pravidla

- Validace vstupních dat
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### PoziceKapitalProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/PoziceKapitalProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `DokladyModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `for()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### PoziceMenove

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/PoziceMenove.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`
- `getQueryString()`
- `getRequestURI()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Export dat do externích formátů
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### PoziceMenoveProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/PoziceMenoveProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `GenDate.after()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `ate()`

#### Business pravidla

- Export dat do externích formátů
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### PoziceMenoveRedir

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/PoziceMenoveRedir.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Databázové operace

- Čte data z KP_DAT_DOKLAD (TO_CHAR(, YYYY')

**Používané sloupce:**

- `KP_DAT_DOKLAD`: TO_CHAR(, YYYY'

#### Java integrace

**Volané metody:**
- `Calendar.getInstance()`
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `SubkonsModule.useApplicationModule()`

#### Business pravidla

- Transakční zpracování
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### PoziceUverove

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/PoziceUverove.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`
- `getQueryString()`
- `getRequestURI()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Export dat do externích formátů
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### PoziceUveroveProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/PoziceUveroveProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `DokladyModule.useApplicationModule()`
- `Formater.parseDate()`
- `GenDate.after()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`

#### Business pravidla

- Export dat do externích formátů
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### PoziceUveroveRedir

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/PoziceUveroveRedir.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Calendar.getInstance()`
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `equals()`

#### Business pravidla

- Transakční zpracování
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProjCis

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjCis.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `reload()`
- `set()`

---

### ProjCisFrekvence

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjCisFrekvence.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `reload()`
- `set()`

---

### ProjCisFrekvenceEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjCisFrekvenceEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `reload()`
- `set()`

---

### ProjCisInOutTyp

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjCisInOutTyp.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `reload()`
- `set()`

---

### ProjCisInOutTypEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjCisInOutTypEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `reload()`
- `set()`

---

### ProjCisJt

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjCisJt.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `reload()`
- `set()`

---

### ProjCisJtEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjCisJtEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `reload()`
- `set()`

---

### ProjCisKategorie

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjCisKategorie.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `reload()`
- `set()`

---

### ProjCisKategorieEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjCisKategorieEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `reload()`
- `set()`

---

### ProjCisSkup

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjCisSkup.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `reload()`
- `set()`

#### Business pravidla

- Transakční zpracování

---

### ProjCisSkupEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjCisSkupEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `reload()`
- `set()`

---

### ProjCisStatus

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjCisStatus.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `reload()`
- `set()`

---

### ProjCisStatusEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjCisStatusEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `reload()`
- `set()`

---

### ProjCisUver

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjCisUver.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `reload()`
- `set()`

---

### ProjCisUverEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjCisUverEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `reload()`
- `set()`

---

### ProjPrehled

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjPrehled.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProjektCashFlow

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjektCashFlow.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getAttribute()`
- `getElementById()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProjektCashFlowAllExcelProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjektCashFlowAllExcelProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `ate()`
- `getParameter()`
- `if()`

#### Business pravidla

- Export dat do externích formátů
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProjektCashFlowEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjektCashFlowEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getElementById()`
- `getParameter()`
- `rray()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProjektCashFlowEditProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjektCashFlowEditProcess.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `ProjektModule.useApplicationModule()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProjektCashFlowExcelProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjektCashFlowExcelProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `ate()`
- `getParameter()`

#### Business pravidla

- Export dat do externích formátů
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProjektCashFlowExportSetup

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjektCashFlowExportSetup.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `reload()`
- `set()`

#### Business pravidla

- Export dat do externích formátů
- Import dat z externích zdrojů

---

### ProjektCashFlowExportSetupProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjektCashFlowExportSetupProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `ProjektModule.useApplicationModule()`
- `getParameter()`
- `if()`

#### Business pravidla

- Export dat do externích formátů
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProjektEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjektEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProjektEditProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjektEditProcess.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `ProjektModule.useApplicationModule()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProjektHist

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjektHist.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `getAttribute()`
- `getParameter()`
- `reload()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProjektMISTransakce

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjektMISTransakce.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `getParameter()`
- `reload()`
- `set()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProjektMemorandum

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjektMemorandum.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `ProjektModule.useApplicationModule()`
- `getParameter()`
- `isUserInRole()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProjektMemorandumEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjektMemorandumEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Navigator.getInstance()`
- `ProjektModule.useApplicationModule()`
- `getParameter()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProjektMemorandumPrint

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjektMemorandumPrint.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProjektMemorandumProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjektMemorandumProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `ProjektModule.useApplicationModule()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProjektNaklady

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjektNaklady.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `ashMap()`
- `getParameter()`
- `set()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProjektNavrhEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjektNavrhEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `equals()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProjektNavrhEditProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjektNavrhEditProcess.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `ProjektModule.useApplicationModule()`
- `getParameter()`

#### Business pravidla

- Odesílání notifikací
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProjektNavrhView

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjektNavrhView.jsp`

**Primární funkce:** Zobrazení dat pouze pro čtení

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProjektNavrhy

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjektNavrhy.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Edit.submit()`
- `HtmlServices.getRequestParameters()`
- `getElementById()`
- `submitDelete()`
- `submitNavrh()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProjektSLDeveloper

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjektSLDeveloper.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `getAttribute()`
- `getParameter()`
- `reload()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProjektSLDeveloperExcelProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjektSLDeveloperExcelProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `ate()`
- `getParameter()`

#### Business pravidla

- Export dat do externích formátů
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProjektSetup

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjektSetup.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Formater.parseDouble()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `ProjektModule.useApplicationModule()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProjektTransakce

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjektTransakce.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `ashMap()`
- `getAttribute()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProjektTransakceAllExcelProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjektTransakceAllExcelProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `ate()`
- `getParameter()`
- `if()`

#### Business pravidla

- Export dat do externích formátů
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProjektTransakceExcelProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjektTransakceExcelProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `ProjektModule.useApplicationModule()`
- `ate()`

#### Business pravidla

- Export dat do externích formátů
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProjektUcSpol

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjektUcSpol.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProjektUcSpolProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjektUcSpolProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `ProjektModule.useApplicationModule()`
- `equalsIgnoreCase()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProjektUsers

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjektUsers.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `getAttribute()`
- `getParameter()`
- `reload()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### Projekty

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/Projekty.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getParameter()`
- `getRequestURI()`
- `if()`
- `isUserInRole()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProjektyBilance

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjektyBilance.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `Utils.getLastDateMB()`
- `Utils.getUserName()`
- `getParameter()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getLastDateMB
- Utility třída: Utils.getUserName
- Business služba: HtmlServices.getRequestParameters

---

### ProjektyBilanceDelete

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjektyBilanceDelete.jsp`

**Primární funkce:** Mazání záznamů

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Navigator.getInstance()`
- `ProjektModule.useApplicationModule()`
- `URLDecoder.decode()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProjektyBilanceProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjektyBilanceProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProjektyExcelProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjektyExcelProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `ProjektModule.useApplicationModule()`
- `ate()`
- `exportExcel()`

#### Business pravidla

- Export dat do externích formátů
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProjektyNew

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjektyNew.jsp`

**Primární funkce:** Vytváření nových záznamů

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`
- `getRequestURI()`
- `if()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProjektyPravaProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjektyPravaProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `ProjektModule.useApplicationModule()`
- `getAttribute()`
- `if()`
- `isUserInRole()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProtiGroup

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProtiGroup.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProtiGroup2

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProtiGroup2.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `URLDecoder.decode()`
- `getParameter()`
- `if()`
- `isUserInRole()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProtiGroup2Process

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProtiGroup2Process.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `URLEncoder.encode()`
- `equals()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProtiGroupCust

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProtiGroupCust.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `isUserInRole()`
- `reload()`
- `set()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

---

### ProtiGroupCustProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProtiGroupCustProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `ProtistranyModule.useApplicationModule()`
- `custGroup()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProtiGroupExcelProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProtiGroupExcelProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `ate()`
- `getCurrentLink()`

#### Business pravidla

- Export dat do externích formátů
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProtiGroupProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProtiGroupProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `ProtistranyModule.useApplicationModule()`
- `equals()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProtiOsoby

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProtiOsoby.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `ProtistranyModule.useApplicationModule()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProtiOsobyExcelProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProtiOsobyExcelProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `ProtistranyModule.useApplicationModule()`
- `ate()`

#### Business pravidla

- Export dat do externích formátů
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProtiOsobyProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProtiOsobyProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `ProtistranyModule.useApplicationModule()`
- `getParameter()`
- `if()`
- `reloadOsoby()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProtiPoziceParovani

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProtiPoziceParovani.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getParameter()`
- `indexOf()`
- `open()`
- `psWindow()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProtiPoziceParovaniProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProtiPoziceParovaniProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `ProtistranyModule.useApplicationModule()`
- `for()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProtiSkupinaIc

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProtiSkupinaIc.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `aDelete()`
- `aUpdate()`
- `getElementById()`
- `getParameter()`

#### Business pravidla

- Validace vstupních dat
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProtiSkupinaIcProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProtiSkupinaIcProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProtiSkupinaIcPs

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProtiSkupinaIcPs.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Param.length()`
- `close()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProtiVPrechodu

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProtiVPrechodu.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `aDelete()`
- `aUpdate()`
- `getElementById()`
- `if()`
- `indexOf()`

#### Business pravidla

- Validace vstupních dat
- Import dat z externích zdrojů

---

### ProtiVPrechoduProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProtiVPrechoduProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `ProtistranyModule.useApplicationModule()`
- `equalsIgnoreCase()`
- `for()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProtistranaEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProtistranaEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `aUpdate()`
- `getElementById()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProtistranaEditProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProtistranaEditProcess.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `ProtistranyModule.useApplicationModule()`
- `editSpolKodProti()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### Protistrany

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/Protistrany.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `alert()`
- `aol()`
- `open()`
- `openHistorie()`

#### Business pravidla

- Import dat z externích zdrojů

---

### ProtistranyDetailHist

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProtistranyDetailHist.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Do.length()`
- `HtmlServices.getRequestParameters()`
- `Od.length()`
- `Spol.length()`
- `Utils.getLastDateAsString()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getTodaysDate
- Utility třída: Utils.getLastDateAsString
- Business služba: HtmlServices.getRequestParameters

---

### ProtistranyDetailView

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProtistranyDetailView.jsp`

**Primární funkce:** Zobrazení dat pouze pro čtení

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProtistranyRedir

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProtistranyRedir.jsp`

**Primární funkce:** Obecná business funkcionalita

---

### ProtistranyUpdate

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProtistranyUpdate.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `ProtistranyModule.useApplicationModule()`
- `reloadProtistrany()`

---

### ProtistranyView

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProtistranyView.jsp`

**Primární funkce:** Zobrazení dat pouze pro čtení

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getElementById()`
- `getParameter()`
- `open()`
- `openHistorie()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProtistranyViewTest

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProtistranyViewTest.jsp`

**Primární funkce:** Zobrazení dat pouze pro čtení

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `alert()`
- `expression()`
- `getElementById()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### PwdChange

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/PwdChange.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `reload()`
- `set()`

---

### PwdChangeProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/PwdChangeProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Rep.equals()`
- `getParameter()`
- `if()`
- `indexOf()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### QProjektOdbor

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/QProjektOdbor.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### QProjektOdborProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/QProjektOdborProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `UcSkupModule.useApplicationModule()`
- `for()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### Report

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/Report.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `reeSet()`
- `reload()`
- `set()`

---

### ReportSpol

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ReportSpol.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `reload()`
- `set()`

---

### ReportSpolLCD

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ReportSpolLCD.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `getParameter()`
- `if()`
- `reload()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ReportSpolProblemMU

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ReportSpolProblemMU.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getElementById()`
- `getParameter()`
- `getWindowHeight()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ReportSpolProblemZUK

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ReportSpolProblemZUK.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `getElementById()`
- `getParameter()`
- `getWindowHeight()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ReportSpolTran

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ReportSpolTran.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getElementById()`
- `getParameter()`
- `getWindowHeight()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ReportZamekChyba

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ReportZamekChyba.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getElementById()`
- `getParameter()`
- `getWindowHeight()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ReportZamekOONe

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ReportZamekOONe.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `getElementById()`
- `getWindowHeight()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### SegmentPrehled

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/SegmentPrehled.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Module.currentUsersId()`
- `UcSkupModule.useApplicationModule()`
- `in()`

#### Business pravidla

- Import dat z externích zdrojů

---

### SegmentZastup

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/SegmentZastup.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Databázové operace

- Čte data z KP_DAT_MNGSEGMENTBOSS (ID_BOSS)

**Používané sloupce:**

- `KP_DAT_MNGSEGMENTBOSS`: ID_BOSS

#### Java integrace

**Volané metody:**
- `Del.submit()`
- `HtmlServices.getRequestParameters()`
- `IfrsModule.useApplicationModule()`
- `Integer.parseInt()`
- `Navigator.getInstance()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### SegmentZastupEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/SegmentZastupEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Utils.getTodaysDate()`
- `getParameter()`
- `length()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getTodaysDate
- Business služba: HtmlServices.getRequestParameters

---

### SegmentZastupEditProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/SegmentZastupEditProcess.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `IfrsModule.useApplicationModule()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Odesílání notifikací
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### Segmenty

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/Segmenty.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `equals()`
- `getParameter()`
- `length()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### SpolecnostPs

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/SpolecnostPs.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Param.length()`
- `close()`
- `getElementById()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### SpolecnostRadky

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/SpolecnostRadky.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `close()`
- `for()`
- `getElementById()`
- `getElementsByTagName()`

#### Business pravidla

- Validace vstupních dat
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### SpolecnostSubkons

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/SpolecnostSubkons.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Spol.getAttribute()`
- `close()`
- `f_jeSpolecnostClenKonsolidace()`
- `for()`

#### Business pravidla

- Validace vstupních dat
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### SpolecnostTT

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/SpolecnostTT.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `for()`
- `getElementsByTagName()`
- `getParameter()`
- `if()`

#### Business pravidla

- Validace vstupních dat
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### SubPravidla

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/SubPravidla.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Utils.getLastDateAsString()`
- `equals()`
- `getElementsByName()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getLastDateAsString
- Business služba: HtmlServices.getRequestParameters

---

### SubPravidlaDetail

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/SubPravidlaDetail.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `equals()`
- `f_jeSpolecnostClenSkupiny()`
- `getParameter()`
- `if()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### SubPravidlaDetailProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/SubPravidlaDetailProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `SubkonsModule.useApplicationModule()`
- `equalsIgnoreCase()`
- `for()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### SubPravidlaProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/SubPravidlaProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `ate()`
- `getParameter()`
- `if()`

#### Business pravidla

- Export dat do externích formátů
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### SubPravidlaSpol

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/SubPravidlaSpol.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `equals()`
- `getParameter()`
- `if()`
- `isUserInRole()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### SubPravidlaSpolProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/SubPravidlaSpolProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### SubPravidlaTyp

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/SubPravidlaTyp.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### SubPravidlaTypDetail

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/SubPravidlaTypDetail.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### SubPravidlaTypDetailProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/SubPravidlaTypDetailProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### SubPravidlaTypProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/SubPravidlaTypProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `DtTyp.length()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `SubkonsModule.useApplicationModule()`
- `equalsIgnoreCase()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### SubView

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/SubView.jsp`

**Primární funkce:** Zobrazení dat pouze pro čtení

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### Subkons

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/Subkons.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Del.submit()`
- `Edit.submit()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Navigator.getInstance()`

#### Business pravidla

- Validace vstupních dat
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### SubkonsClenRP

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/SubkonsClenRP.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `equals()`
- `getParameter()`
- `if()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### SubkonsClenRPProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/SubkonsClenRPProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### SubkonsEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/SubkonsEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Num.intValue()`
- `equals()`
- `getAttribute()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### SubkonsEditHistorie

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/SubkonsEditHistorie.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `getParameter()`
- `reload()`
- `set()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### SubkonsEditProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/SubkonsEditProcess.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `SubkonsModule.useApplicationModule()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### SubkonsExcelProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/SubkonsExcelProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `ate()`
- `getParameter()`

#### Business pravidla

- Export dat do externích formátů
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### SubkonsHistorie

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/SubkonsHistorie.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `getParameter()`
- `reload()`
- `set()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### SubkonsOO

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/SubkonsOO.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Navigator.getInstance()`
- `getParameter()`
- `reload()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### SubkonsOOProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/SubkonsOOProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `SubkonsModule.useApplicationModule()`
- `for()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### SubkonsPrehled

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/SubkonsPrehled.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `getParameter()`
- `reload()`
- `rgb()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### SubkonsVazbyCheck

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/SubkonsVazbyCheck.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getName()`
- `getParameter()`
- `getUserPrincipal()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### Test2

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/Test2.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Param.length()`
- `Skupina.length()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### TestConn

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/TestConn.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Databázové operace

- Čte data z DUAL ('SESSIONID'), SYS_CONTEXT('USERENV')
- Čte data z KP_KTG_UCETNISPOLECNOST

**Používané sloupce:**

- `DUAL`: 'SESSIONID'), SYS_CONTEXT('USERENV'

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `close()`
- `createStatement()`
- `executeQuery()`
- `getConnection()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UcSkup

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSkup.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Nazev.length()`
- `for()`
- `getParameter()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UcSkupEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSkupEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`
- `isUserInRole()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UcSkupEditLink

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSkupEditLink.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `UcSkupModule.useApplicationModule()`
- `getParameter()`
- `if()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UcSkupEditLinkEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSkupEditLinkEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `UcSkupModule.useApplicationModule()`
- `getParameter()`
- `if()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UcSkupEditLinkEditProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSkupEditLinkEditProcess.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `equalsIgnoreCase()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UcSkupEditProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSkupEditProcess.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `UcSkupModule.useApplicationModule()`
- `equals()`
- `getParameter()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UcSkupExcelProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSkupExcelProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `ate()`
- `getParameter()`
- `if()`

#### Business pravidla

- Export dat do externích formátů
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UcSkupHistorie

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSkupHistorie.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UcSkupNastaveni

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSkupNastaveni.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `alert()`
- `getElementById()`
- `reload()`
- `set()`

#### Business pravidla

- Validace vstupních dat
- Export dat do externích formátů
- Import dat z externích zdrojů

---

### UcSkupNastaveniProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSkupNastaveniProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `alert()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UcSkupPravaProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSkupPravaProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Module.setAllUcOdAd()`
- `UsersModule.useApplicationModule()`
- `if()`
- `isUserInRole()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UcSkupPredavani

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSkupPredavani.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getElementById()`
- `getParameter()`
- `if()`

#### Business pravidla

- Validace vstupních dat
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UcSkupPredavaniExcelProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSkupPredavaniExcelProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `ate()`
- `getParameter()`
- `if()`

#### Business pravidla

- Export dat do externích formátů
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UcSkupPredavaniProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSkupPredavaniProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `equals()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UcSkupProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSkupProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Module.deleteUcSkup()`
- `Module.insertUcSkup()`
- `UcSkupModule.useApplicationModule()`

#### Business pravidla

- Transakční zpracování
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UcSkupSub

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSkupSub.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `for()`
- `isUserInRole()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

---

### UcSpol

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSpol.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `UcSkupModule.useApplicationModule()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UcSpolKarta

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSpolKarta.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Del.submit()`
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `getElementById()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UcSpolKartaDatum

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSpolKartaDatum.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `reload()`
- `set()`

#### Business pravidla

- Import dat z externích zdrojů

---

### UcSpolKartaDatumProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSpolKartaDatumProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `UcSkupModule.useApplicationModule()`
- `for()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UcSpolKartaEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSpolKartaEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `equals()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UcSpolKartaEditProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSpolKartaEditProcess.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `UcSkupModule.useApplicationModule()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UcSpolKartaPravidla

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSpolKartaPravidla.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `UcSkupModule.useApplicationModule()`
- `getParameter()`
- `isUserInRole()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UcSpolKartaPravidlaEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSpolKartaPravidlaEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UcSpolKartaPravidlaEditProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSpolKartaPravidlaEditProcess.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `UcSkupModule.useApplicationModule()`
- `equalsIgnoreCase()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UcSpolKartaTyp

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSpolKartaTyp.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `deleteId()`
- `getElementById()`
- `submit()`
- `submitId()`

#### Business pravidla

- Import dat z externích zdrojů

---

### UcSpolKartaTypEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSpolKartaTypEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `equals()`
- `getAttribute()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UcSpolKartaTypEditProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSpolKartaTypEditProcess.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `UcSkupModule.useApplicationModule()`
- `getParameter()`
- `kartaTyp()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UcSpolKartaVyjimky

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSpolKartaVyjimky.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`
- `isUserInRole()`
- `submitId()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UcSpolKartaVyjimkyEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSpolKartaVyjimkyEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UcSpolKartaVyjimkyEditProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSpolKartaVyjimkyEditProcess.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `UcSkupModule.useApplicationModule()`
- `equalsIgnoreCase()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UcSpolKartaZpravy

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSpolKartaZpravy.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UcSpolKartaZpravyEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSpolKartaZpravyEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UcSpolKartaZpravyProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSpolKartaZpravyProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `UcSkupModule.useApplicationModule()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UcSpolLocale

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSpolLocale.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UcSpolNadrizena

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSpolNadrizena.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `equals()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UcSpolOnline

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSpolOnline.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `equals()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UcSpolOnlineHistorie

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSpolOnlineHistorie.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `getParameter()`
- `reload()`
- `set()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UcSpolPrehled

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSpolPrehled.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Databázové operace

- Čte data z KP_REL_SUBKONSOLIDACECLEN (NULL)

**Používané sloupce:**

- `KP_REL_SUBKONSOLIDACECLEN`: NULL

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Id.length()`
- `Integer.parseInt()`
- `and()`
- `exists()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UcSpolPrehled2

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSpolPrehled2.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UcSpolProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSpolProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `Formater.parseDouble()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Module.setDanovaZtrata()`
- `UcSkupModule.useApplicationModule()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UcSpolProcess2

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSpolProcess2.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `Formater.parseDouble()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UcSpolSpravce

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSpolSpravce.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UcSpolUsers

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSpolUsers.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `getParameter()`
- `reload()`
- `set()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UcSpolZustatky

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSpolZustatky.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `getParameter()`
- `if()`
- `reload()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UsersBoss

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UsersBoss.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `reload()`
- `set()`

#### Business pravidla

- Import dat z externích zdrojů

---

### UsersBossProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UsersBossProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `UsersModule.useApplicationModule()`
- `for()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UsersEditor

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UsersEditor.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `reload()`
- `set()`

#### Business pravidla

- Transakční zpracování

---

### UsersEditorEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UsersEditorEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `reload()`
- `set()`

---

### UsersExtEditor

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UsersExtEditor.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `reload()`
- `set()`

#### Business pravidla

- Transakční zpracování

---

### UsersExtEditorEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UsersExtEditorEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `reload()`
- `set()`

---

### UsersProjekt

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UsersProjekt.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getElementById()`
- `getParameter()`
- `submitNacist()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UsersProjektProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UsersProjektProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UsersSkupina

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UsersSkupina.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `getElementById()`
- `reload()`
- `set()`
- `setDeleteUser()`

---

### UsersSkupinaProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UsersSkupinaProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `UsersModule.useApplicationModule()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UsersSpol

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UsersSpol.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `getElementById()`
- `getParameter()`
- `reload()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UsersSpolProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UsersSpolProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UsersToTable

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UsersToTable.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `UsersModule.useApplicationModule()`
- `usersJaznXmlToDb()`

#### Business pravidla

- Export dat do externích formátů

---

### UsersVyjimky

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UsersVyjimky.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `getElementById()`
- `if()`
- `set()`
- `setCbUser()`

#### Business pravidla

- Validace vstupních dat

---

### UsersVyjimkyProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UsersVyjimkyProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `UsersModule.useApplicationModule()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### Uvery

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/Uvery.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `ProjektModule.useApplicationModule()`
- `Utils.getLastDateMB()`
- `Utils.getLastDateMBAsDate()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getLastDateMBAsDate
- Utility třída: Utils.getLastDateMB
- Business služba: HtmlServices.getRequestParameters

---

### UveryDetail

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UveryDetail.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Formater.formatDate()`
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `getAttribute()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UveryExcelProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UveryExcelProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `ate()`
- `getCurrentLink()`

#### Business pravidla

- Export dat do externích formátů
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UveryProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UveryProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `ProjektModule.useApplicationModule()`
- `getParameter()`
- `uverTypKategorie()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UveryProjekt

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UveryProjekt.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UverySplatky

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UverySplatky.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `getParameter()`
- `getRequestURI()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### VyjimkyMajUc

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/VyjimkyMajUc.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `aDelete()`
- `aUpdate()`
- `getElementById()`
- `getParameter()`

#### Business pravidla

- Validace vstupních dat
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### VyjimkyMajUcProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/VyjimkyMajUcProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `MajetekModule.useApplicationModule()`
- `for()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### VyjimkyProtokolu

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/VyjimkyProtokolu.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `changePeriod()`
- `getElementById()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

---

### VyjimkyProtokoluProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/VyjimkyProtokoluProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `DokladyModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Module.deleteVyjimkyProtokolu()`
- `for()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### VyjimkyProtokoluTop

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/VyjimkyProtokoluTop.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `editovat()`
- `getElementById()`
- `if()`

#### Business pravidla

- Validace vstupních dat
- Import dat z externích zdrojů

---

### VyjimkyProtokoluTopProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/VyjimkyProtokoluTopProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `DokladyModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `for()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### VyjimkyPs

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/VyjimkyPs.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `aDelete()`
- `aUpdate()`
- `getElementById()`

#### Business pravidla

- Validace vstupních dat
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### VyjimkyPsProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/VyjimkyPsProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `ProtistranyModule.useApplicationModule()`
- `for()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### VyjimkySubVazby

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/VyjimkySubVazby.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `aDelete()`
- `aUpdate()`
- `addLine()`
- `createElement()`
- `getElementById()`

#### Business pravidla

- Validace vstupních dat
- Import dat z externích zdrojů

---

### VyjimkySubVazbyProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/VyjimkySubVazbyProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `SubkonsModule.useApplicationModule()`
- `equalsIgnoreCase()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ZamekProtokol

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ZamekProtokol.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `Utils.getLastDateMB()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getLastDateMB
- Business služba: HtmlServices.getRequestParameters

---

### ZamekProtokolExport

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ZamekProtokolExport.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `DokladyModule.useApplicationModule()`
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `equals()`
- `excelOutput()`

#### Business pravidla

- Export dat do externích formátů
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### cookies

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/cookies.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `for()`
- `getCookies()`
- `getName()`
- `getValue()`
- `if()`

---

### errorpage

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/errorpage.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Logger.getLogger()`
- `Runtime.getRuntime()`
- `availableProcessors()`
- `createLog()`
- `flush()`

#### Business pravidla

- Import dat z externích zdrojů

---

### errortest

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/errortest.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `println()`
- `toString()`

---

### header

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/header.jsp`

**Primární funkce:** Obecná business funkcionalita

---

### idm

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/idm.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Calendar.getInstance()`
- `Cipher.getInstance()`
- `User.indexOf()`
- `User.length()`
- `User.substring()`

---

### index

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/index.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Constants.getHostName()`
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `equals()`
- `getCurrentLink()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### index2

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/index2.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Constants.getHostName()`
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `equals()`
- `getCurrentLink()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### index2ref

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/index2ref.jsp`

**Primární funkce:** Obecná business funkcionalita

---

### jdbc

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/jdbc.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `URL.getConnection()`
- `catch()`
- `close()`
- `getDriverMajorVersion()`
- `getDriverMinorVersion()`

#### Business pravidla

- Import dat z externích zdrojů

---

### login

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/login.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `invalidate()`
- `sendError()`
- `setHeader()`

#### Business pravidla

- Validace vstupních dat

---

### logout

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/logout.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `getContextPath()`
- `getUserPrincipal()`
- `invalidate()`
- `sendError()`
- `setHeader()`

#### Business pravidla

- Validace vstupních dat

---

### lovcomp

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/lovcomp.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Type.length()`
- `close()`
- `doCancel()`
- `getParameter()`
- `getRequestURI()`

#### Business pravidla

- Import dat z externích zdrojů

---

### menu

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/menu/menu.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Constants.getHostName()`
- `makeCM()`
- `opyright()`
- `rattli()`

#### Business pravidla

- Validace vstupních dat

---

### restart_alert

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/restart_alert.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### test

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/test.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getConnection()`
- `lookup()`
- `nitialContext()`
- `println()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

## Projektový budget

*Celkem 15 stránek v této doméně*

### PbCis

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/PbCis.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `reload()`
- `set()`

---

### PbCisKategorieKlienta

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/PbCisKategorieKlienta.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `reload()`
- `set()`

#### Business pravidla

- Transakční zpracování

---

### PbCisKategorieKlientaEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/PbCisKategorieKlientaEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `reload()`
- `set()`

---

### PbCisSkupina

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/PbCisSkupina.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `deleteId()`
- `getElementById()`
- `submit()`
- `submitId()`

#### Business pravidla

- Import dat z externích zdrojů

---

### PbCisSkupinaEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/PbCisSkupinaEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getAttribute()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### PbCisSkupinaEditProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/PbCisSkupinaEditProcess.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `PbModule.useApplicationModule()`
- `getParameter()`
- `skupina()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### PbCisStatusKlienta

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/PbCisStatusKlienta.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `reload()`
- `set()`

---

### PbCisStatusKlientaEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/PbCisStatusKlientaEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `reload()`
- `set()`

---

### PbKlientiA

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/PbKlientiA.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `PbModule.useApplicationModule()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### PbKlientiAExcelProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/PbKlientiAExcelProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `ate()`
- `getCurrentLink()`

#### Business pravidla

- Export dat do externích formátů
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### PbSpolecnost

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/PbSpolecnost.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Id.length()`
- `PbModule.useApplicationModule()`
- `getParameter()`
- `if()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### PbSpolecnostZalozeniEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/PbSpolecnostZalozeniEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `PbModule.useApplicationModule()`
- `getParameter()`
- `isCurrentUserAdministrator()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### PbSpolecnostZalozeniProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/PbSpolecnostZalozeniProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `SpolStr.length()`
- `getParameter()`
- `length()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ProjCisPb

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjCisPb.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `reload()`
- `set()`

---

### ProjCisPbEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ProjCisPbEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `reload()`
- `set()`

---

## Správa dokladů

*Celkem 68 stránek v této doméně*

### DokladMPOPExport

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladMPOPExport.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `DokladyModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `excelOutput()`
- `getExportRedir()`
- `getParameter()`

#### Business pravidla

- Export dat do externích formátů
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DokladManProjektOdbor

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladManProjektOdbor.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `aDelete()`
- `getElementById()`
- `getParameter()`
- `if()`

#### Business pravidla

- Validace vstupních dat
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DokladManProjektOdbor2

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladManProjektOdbor2.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `aDelete()`
- `aUpdate()`
- `addLine()`
- `createElement()`

#### Business pravidla

- Validace vstupních dat
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DokladManProjektOdborProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladManProjektOdborProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DokladVazbyParovani

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladVazbyParovani.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Locale.length()`
- `Utils.getLocale()`
- `Utils.setLocale()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getLocale
- Utility třída: Utils.setLocale
- Business služba: HtmlServices.getRequestParameters

---

### DokladVazbyParovani2

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladVazbyParovani2.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Locale.length()`
- `Utils.getLocale()`
- `Utils.setLocale()`
- `Zamek.submit()`

#### Business pravidla

- Validace vstupních dat
- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getLocale
- Utility třída: Utils.setLocale
- Business služba: HtmlServices.getRequestParameters

---

### DokladVazbyParovani2Process

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladVazbyParovani2Process.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `DokladyModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DokladVazbyParovaniDetail

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladVazbyParovaniDetail.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Locale.length()`
- `Utils.getLocale()`
- `Utils.setLocale()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getLocale
- Utility třída: Utils.setLocale
- Business služba: HtmlServices.getRequestParameters

---

### DokladVazbyParovaniDetailProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladVazbyParovaniDetailProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `DokladyModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DokladVazbyParovaniOrig

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladVazbyParovaniOrig.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Locale.length()`
- `Utils.getLocale()`
- `Utils.setLocale()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getLocale
- Utility třída: Utils.setLocale
- Business služba: HtmlServices.getRequestParameters

---

### DokladVazbyParovaniProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladVazbyParovaniProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `DokladyModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DokladVazbyParovaniZamek

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladVazbyParovaniZamek.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Doklad.getAttribute()`
- `DokladyModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Spol.intValue()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### Doklady

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/Doklady.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getAttribute()`
- `getParameter()`
- `getQueryString()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DokladyAll

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladyAll.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `eval()`
- `getQueryString()`
- `getRequestURI()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DokladyArchivni

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladyArchivni.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `eval()`
- `getRequestURI()`
- `if()`
- `removeAttribute()`

#### Business pravidla

- Validace vstupních dat
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DokladyCZS

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladyCZS.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `equals()`
- `getParameter()`
- `getQueryString()`
- `getRequestURI()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DokladyDelete

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladyDelete.jsp`

**Primární funkce:** Mazání záznamů

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Utils.getUserName()`
- `getAttribute()`
- `getParameter()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getUserName
- Business služba: HtmlServices.getRequestParameters

---

### DokladyDetail

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladyDetail.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Utils.getLastDateMB()`
- `equals()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getLastDateMB
- Business služba: HtmlServices.getRequestParameters

---

### DokladyDetailExport

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladyDetailExport.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `DokladyModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `excelOutput()`
- `getExportRedir()`
- `getParameter()`

#### Business pravidla

- Export dat do externích formátů
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DokladyDetailInclude

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladyDetailInclude.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Utils.getLastDateMB()`
- `equals()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getLastDateMB
- Business služba: HtmlServices.getRequestParameters

---

### DokladyDetailSpol

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladyDetailSpol.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Utils.getUserName()`
- `if()`
- `isUserInRole()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getUserName

---

### DokladyDetailSpolDetail

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladyDetailSpolDetail.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `aDelete()`
- `getElementById()`
- `getParameter()`
- `indexOf()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DokladyDetailSpolDetailProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladyDetailSpolDetailProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DokladyDetailSpolProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladyDetailSpolProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DokladyDetailSum

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladyDetailSum.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Utils.getLastDateMB()`
- `equals()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getLastDateMB
- Business služba: HtmlServices.getRequestParameters

---

### DokladyGenDelete

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladyGenDelete.jsp`

**Primární funkce:** Mazání záznamů

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DokladyGenSub

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladyGenSub.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DokladyMan

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladyMan.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`
- `getQueryString()`
- `getRequestURI()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DokladyManDef

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladyManDef.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `ListPor.submit()`
- `Navigator.getInstance()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DokladyManDefDetail

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladyManDefDetail.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DokladyManDefDetailProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladyManDefDetailProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `DokladyModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `equals()`
- `for()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DokladyManDefProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladyManDefProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DokladyManDefPs

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladyManDefPs.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Param.length()`
- `close()`
- `getElementById()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DokladyManFileUpload

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladyManFileUpload.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `DokladyModule.useApplicationModule()`
- `Formater.parseDate()`
- `GenDate.before()`
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DokladyManFileUploadProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladyManFileUploadProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `Type.indexOf()`
- `ataInputStream()`
- `getContentLength()`
- `getContentType()`
- `getInputStream()`

#### Business pravidla

- Import dat z externích zdrojů

---

### DokladyManFileUploadProcess2

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladyManFileUploadProcess2.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `DokladyModule.useApplicationModule()`
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `IdSub.length()`
- `Integer.parseInt()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DokladyManPoziceUpload

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladyManPoziceUpload.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `DokladyModule.useApplicationModule()`
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `getParameter()`
- `if()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DokladyManPoziceUploadProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladyManPoziceUploadProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `Type.indexOf()`
- `ataInputStream()`
- `getContentLength()`
- `getContentType()`
- `getInputStream()`

#### Business pravidla

- Import dat z externích zdrojů

---

### DokladyManPoziceUploadProcess2

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladyManPoziceUploadProcess2.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `URLDecoder.decode()`
- `getParameter()`
- `getPreviousLink()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DokladyManProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladyManProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DokladyManStarsi

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladyManStarsi.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`
- `getQueryString()`
- `getRequestURI()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DokladyOLAP

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladyOLAP.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `getElementById()`
- `getWindowHeight()`
- `if()`
- `rebuildWindow()`

#### Business pravidla

- Export dat do externích formátů
- Import dat z externích zdrojů

---

### DokladyProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladyProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `equals()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DokladyProcessGen

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladyProcessGen.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DokladySKS

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladySKS.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `equals()`
- `getParameter()`
- `getQueryString()`
- `getRequestURI()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DokladySpecific

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladySpecific.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `eval()`
- `getQueryString()`
- `getRequestURI()`
- `removeAttribute()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DokladySpol

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladySpol.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `equals()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DokladySubKon

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladySubKon.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Do.lastIndexOf()`
- `Do.substring()`
- `HtmlServices.getRequestParameters()`
- `Utils.getTodaysDate()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getTodaysDate
- Business služba: HtmlServices.getRequestParameters

---

### DokladySubKonProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladySubKonProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`
- `if()`
- `isException()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DokladySubkons

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladySubkons.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `equals()`
- `getParameter()`
- `getQueryString()`
- `getRequestURI()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DokladySubkonsDelete

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladySubkonsDelete.jsp`

**Primární funkce:** Mazání záznamů

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getAttribute()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DokladySubkonsFronta

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladySubkonsFronta.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Fronta.getRowSet()`
- `HtmlServices.getRequestParameters()`
- `getParameter()`
- `getRowCount()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DokladySubkonsFrontaDelete

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladySubkonsFrontaDelete.jsp`

**Primární funkce:** Mazání záznamů

#### Databázové operace

- Maže záznamy z KP_DAT_KONSOLIDACEFRONTA

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `getCurrentLink()`
- `getParameter()`

#### Business pravidla

- Transakční zpracování
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DokladySubkonsProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladySubkonsProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `equals()`
- `getParameter()`
- `if()`
- `startsWith()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DokladySubkonsProcessGen

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladySubkonsProcessGen.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `equals()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DokladySubkonsStarsi

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladySubkonsStarsi.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `equals()`
- `getParameter()`
- `getQueryString()`
- `getRequestURI()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DokladyZS

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladyZS.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `equals()`
- `getParameter()`
- `getQueryString()`
- `getRequestURI()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DokladyZamekHistorie

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladyZamekHistorie.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `getParameter()`
- `reload()`
- `set()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DokladyZdrojDatMan

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladyZdrojDatMan.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `equals()`
- `getParameter()`
- `if()`
- `length()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### DokladyZdrojDatManProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladyZdrojDatManProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `DokladyModule.useApplicationModule()`
- `Formater.parseDate()`
- `Formater.parseDouble()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### KapAgregujDoklad

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapAgregujDoklad.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Utils.getLastDateMB()`
- `if()`
- `isUserInRole()`
- `sendError()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getLastDateMB

---

### KapAgregujDokladProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapAgregujDokladProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `KapitalModule.useApplicationModule()`
- `Spol.length()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### LogsDokladDetail

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/LogsDokladDetail.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `getAttribute()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### LogsDokladDetailUnif

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/LogsDokladDetailUnif.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Databázové operace

- Čte data z KP_DAT_DOKLAD (ID)

**Používané sloupce:**

- `KP_DAT_DOKLAD`: ID

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getParameter()`
- `in()`
- `to_date()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### LogsDokladLast

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/LogsDokladLast.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Business pravidla

- Import dat z externích zdrojů

---

### LogsProjektDoklad

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/LogsProjektDoklad.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `getAttribute()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UcSpolObaDoklady

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSpolObaDoklady.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UcSpolObaDokladyProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcSpolObaDokladyProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

## Účetnictví

*Celkem 21 stránek v této doméně*

### CisUcet2Ucet

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/CisUcet2Ucet.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getParameter()`
- `if()`
- `isUserInRole()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### CisUcet2UcetProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/CisUcet2UcetProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `MustkyModule.useApplicationModule()`
- `for()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### FlowUcetUnif

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/FlowUcetUnif.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Locale.length()`
- `Utils.getLocale()`
- `Utils.setLocale()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getLocale
- Utility třída: Utils.setLocale
- Business služba: HtmlServices.getRequestParameters

---

### FlowUcetUnifProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/FlowUcetUnifProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### KamilPrehledUcet

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KamilPrehledUcet.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getElementById()`
- `getParameter()`
- `if()`
- `length()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### KapCisUcetPortfolio

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapCisUcetPortfolio.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `Navigator.getInstance()`
- `deleteId()`
- `getElementById()`
- `reload()`
- `set()`

#### Business pravidla

- Import dat z externích zdrojů

---

### KapCisUcetPortfolioEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapCisUcetPortfolioEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Databázové operace

- Čte data z KP_REL_SUBKONSOLIDACECLEN (NULL"+)

**Používané sloupce:**

- `KP_REL_SUBKONSOLIDACECLEN`: NULL"+

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `exists()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### KapCisUcetPortfolioEditProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapCisUcetPortfolioEditProcess.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `Formater.parseDate()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `equalsIgnoreCase()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### KapDefUcetBus

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapDefUcetBus.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `deleteId()`
- `getElementById()`
- `submit()`
- `submitId()`

#### Business pravidla

- Import dat z externích zdrojů

---

### KapDefUcetBusEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapDefUcetBusEdit.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### KapDefUcetBusEditProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapDefUcetBusEditProcess.jsp`

**Primární funkce:** Zobrazení a úprava dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `KapitalModule.useApplicationModule()`
- `equalsIgnoreCase()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### KapVyjimkyUcetProdukt

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapVyjimkyUcetProdukt.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `aDelete()`
- `aUpdate()`
- `getElementById()`
- `getParameter()`

#### Business pravidla

- Validace vstupních dat
- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### KapVyjimkyUcetProduktProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapVyjimkyUcetProduktProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `KapitalModule.useApplicationModule()`
- `for()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ManualSelectUcetSpol

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ManualSelectUcetSpol.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Param.length()`
- `Skupina.length()`
- `alert()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### MilosPrehledUcet

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/MilosPrehledUcet.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `getElementById()`
- `getParameter()`
- `if()`
- `length()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### MustkyUcet2Ucet

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/MustkyUcet2Ucet.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `Locale.length()`
- `Utils.getLocale()`
- `Utils.getTodaysDate()`

#### Business pravidla

- Kontrola oprávnění uživatele
- Export dat do externích formátů
- Import dat z externích zdrojů

#### Integrační body

- Utility třída: Utils.getTodaysDate
- Utility třída: Utils.getLocale
- Utility třída: Utils.setLocale

---

### MustkyUcet2UcetProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/MustkyUcet2UcetProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ReportSpolUcetniMax

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ReportSpolUcetniMax.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `getParameter()`
- `if()`
- `reload()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### ReportZamekUcetniNe

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/ReportZamekUcetniNe.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Navigator.getInstance()`
- `getElementById()`
- `getWindowHeight()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UcetVazbaUcet

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcetVazbaUcet.jsp`

**Primární funkce:** Obecná business funkcionalita

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`
- `length()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

### UcetVazbaUcetProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/UcetVazbaUcetProcess.jsp`

**Primární funkce:** Procesní zpracování dat

#### Java integrace

**Volané metody:**
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

#### Integrační body

- Business služba: HtmlServices.getRequestParameters

---

