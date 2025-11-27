# Business proces: CisSegment

**Vstupní stránka:** CisSegment

## Přehled procesu

**Počet kroků:** 6
**Počet variant flow:** 3

## Procesní diagram

```mermaid
flowchart TD
    Start([Začátek procesu]) --> Entry
    Entry["Obecná business funkcionalita"]
    Step1["Obecná business funkcionalita"]
    Step2["Obecná business funkcionalita"]
    Step3["Zobrazení a úprava dat"]
    Step4["Zobrazení a úprava dat"]
    Step5["Zobrazení a úprava dat"]
    Entry --> Step1
    Step1 --> Step4
    Step4 --> Step5
    Step5 --> End
    End([Konec procesu])

    style Start fill:#90EE90
    style End fill:#FFB6C1
    style Entry fill:#87CEEB
```

## Detailní analýza kroků

### Krok 1: CisSegment

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/CisSegment.jsp`

**Funkce v procesu:** Obecná business funkcionalita

#### Volané Java metody

- `Navigator.getInstance()`
- `reload()`
- `set()`

---

### Krok 2: CisMngSegment

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/CisMngSegment.jsp`

**Funkce v procesu:** Obecná business funkcionalita

#### Volané Java metody

- `decode()`
- `deleteId()`
- `getElementById()`
- `submitId()`

#### Business pravidla

- Import dat z externích zdrojů

---

### Krok 3: CisMngSegmentEdit

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/CisMngSegmentEdit.jsp`

**Funkce v procesu:** Zobrazení a úprava dat

#### Volané Java metody

- `HtmlServices.getRequestParameters()`
- `Integer.parseInt()`
- `getAttribute()`
- `getParameter()`
- `if()`

#### Business pravidla

- Import dat z externích zdrojů

---

### Krok 4: CisMngSegmentEditProcess

**Soubor:** `/Users/radektuma/DEV/KIS/sources/JSP/CisMngSegmentEditProcess.jsp`

**Funkce v procesu:** Zobrazení a úprava dat

#### Volané Java metody

- `HtmlServices.getRequestParameters()`
- `IfrsModule.useApplicationModule()`
- `Integer.parseInt()`
- `equalsIgnoreCase()`
- `getParameter()`

#### Business pravidla

- Import dat z externích zdrojů

---

## Alternativní procesní cesty

1. CisSegment → CisMngSegment → CisMngSegmentEdit → CisMngSegmentEditProcess
2. CisSegment → CisMngSegment → CisMngSegmentEdit
3. CisSegment → CisMngSegmentBoss → CisMngSegmentBossEdit
