# Business proces: KapCisKategorieProtistran

**Vstupní stránka:** KapCisKategorieProtistran

## Přehled procesu

**Počet kroků:** 3
**Počet variant flow:** 1

## Procesní diagram

```mermaid
flowchart TD
    Start([Začátek procesu]) --> Entry
    Entry["Obecná business funkcionalita"]
    Step1["Zobrazení a úprava dat"]
    Step2["Obecná business funkcionalita"]
    Entry --> Step1
    Step1 --> Step2
    Step2 --> Step2
    Step2 --> End
    End([Konec procesu])

    style Start fill:#90EE90
    style End fill:#FFB6C1
    style Entry fill:#87CEEB
```

## Detailní analýza kroků

### Krok 1: KapCisKategorieProtistran

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapCisKategorieProtistran.jsp`

**Funkce v procesu:** Obecná business funkcionalita

#### Volané Java metody

- `deleteId()`
- `getElementById()`
- `submit()`
- `submitId()`

#### Business pravidla

- Import dat z externích zdrojů

---

### Krok 2: KapCisKategorieProtistranEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/KapCisKategorieProtistranEdit.jsp`

**Funkce v procesu:** Zobrazení a úprava dat

#### Volané Java metody

- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `equals()`
- `getAttribute()`
- `getParameter()`

#### Business pravidla

- Validace vstupních dat
- Import dat z externích zdrojů

---

### Krok 3: SpolecnostPs

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/SpolecnostPs.jsp`

**Funkce v procesu:** Obecná business funkcionalita

#### Volané Java metody

- `HtmlServices.getRequestParameters()`
- `Param.length()`
- `close()`
- `getElementById()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

---

### Krok 4: SpolecnostPs

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/SpolecnostPs.jsp`

**Funkce v procesu:** Obecná business funkcionalita

#### Volané Java metody

- `HtmlServices.getRequestParameters()`
- `Param.length()`
- `close()`
- `getElementById()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

---

