# Business proces: DokladyGenDelete

**Vstupní stránka:** DokladyGenDelete

## Přehled procesu

**Počet kroků:** 6
**Počet variant flow:** 4

## Procesní diagram

```mermaid
flowchart TD
    Start([Začátek procesu]) --> Entry
    Entry["Mazání záznamů"]
    Step1["Obecná business funkcionalita"]
    Step2["Obecná business funkcionalita"]
    Step3["Obecná business funkcionalita"]
    Step4["Procesní zpracování dat"]
    Step5["Obecná business funkcionalita"]
    Entry --> Step4
    Step4 --> Step4
    Step4 --> Step3
    Step3 --> End
    End([Konec procesu])

    style Start fill:#90EE90
    style End fill:#FFB6C1
    style Entry fill:#87CEEB
```

## Detailní analýza kroků

### Krok 1: DokladyGenDelete

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladyGenDelete.jsp`

**Funkce v procesu:** Mazání záznamů

#### Volané Java metody

- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`
- `if()`

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

### Krok 4: DokladyManStarsi

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/DokladyManStarsi.jsp`

**Funkce v procesu:** Obecná business funkcionalita

#### Volané Java metody

- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getParameter()`
- `getQueryString()`
- `getRequestURI()`

#### Business pravidla

- Import dat z externích zdrojů

---

## Alternativní procesní cesty

1. DokladyGenDelete → DokladyProcessGen → DokladyProcessGen → DokladyManStarsi
2. DokladyGenDelete → DokladyProcessGen → DokladyProcessGen → Doklady
3. DokladyGenDelete → DokladyProcessGen → DokladyProcessGen → DokladySpol
4. DokladyGenDelete → DokladyProcessGen → DokladyProcessGen → DokladyMan
