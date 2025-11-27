# Business proces: EviDodavatelProcess

**Vstupní stránka:** EviDodavatelProcess

## Přehled procesu

**Počet kroků:** 3
**Počet variant flow:** 1

## Procesní diagram

```mermaid
flowchart TD
    Start([Začátek procesu]) --> Entry
    Entry["Procesní zpracování dat"]
    Step1["Obecná business funkcionalita"]
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

### Krok 1: EviDodavatelProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/EviDodavatelProcess.jsp`

**Funkce v procesu:** Procesní zpracování dat

#### Volané Java metody

- `EviModule.useApplicationModule()`
- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `equals()`
- `eviExtDodavatel()`

#### Business pravidla

- Import dat z externích zdrojů

---

### Krok 2: EviDodavatel

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/EviDodavatel.jsp`

**Funkce v procesu:** Obecná business funkcionalita

#### Volané Java metody

- `confirm()`
- `deleteIt()`
- `if()`
- `isUserInRole()`

#### Business pravidla

- Kontrola oprávnění uživatele
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

