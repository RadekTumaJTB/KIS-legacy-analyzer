# Business proces: DokladyManDefProcess

**Vstupní stránka:** DokladyManDefProcess

## Přehled procesu

**Počet kroků:** 6
**Počet variant flow:** 4

## Procesní diagram

```mermaid
flowchart TD
    Start([Začátek procesu]) --> Entry
    Entry["Procesní zpracování dat"]
    Step1["Obecná business funkcionalita"]
    Step2["Obecná business funkcionalita"]
    Step3["Obecná business funkcionalita"]
    Step4["Procesní zpracování dat"]
    Step5["Obecná business funkcionalita"]
    Entry --> Step4
    Step4 --> Step4
    Step4 --> Step1
    Step1 --> End
    End([Konec procesu])

    style Start fill:#90EE90
    style End fill:#FFB6C1
    style Entry fill:#87CEEB
```

## Detailní analýza kroků

### Krok 1: DokladyManDefProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladyManDefProcess.jsp`

**Funkce v procesu:** Procesní zpracování dat

#### Volané Java metody

- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

---

### Krok 2: DokladyProcessGen

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladyProcessGen.jsp`

**Funkce v procesu:** Procesní zpracování dat

#### Volané Java metody

- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

---

### Krok 3: DokladyProcessGen

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladyProcessGen.jsp`

**Funkce v procesu:** Procesní zpracování dat

#### Volané Java metody

- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

---

### Krok 4: Doklady

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/Doklady.jsp`

**Funkce v procesu:** Obecná business funkcionalita

#### Volané Java metody

- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getAttribute()`
- `getParameter()`
- `getQueryString()`

#### Business pravidla

- Import dat z externích zdrojů

---

## Alternativní procesní cesty

1. DokladyManDefProcess → DokladyProcessGen → DokladyProcessGen → Doklady
2. DokladyManDefProcess → DokladyProcessGen → DokladyProcessGen → DokladyManStarsi
3. DokladyManDefProcess → DokladyProcessGen → DokladyProcessGen → DokladyMan
4. DokladyManDefProcess → DokladyProcessGen → DokladyProcessGen → DokladySpol
