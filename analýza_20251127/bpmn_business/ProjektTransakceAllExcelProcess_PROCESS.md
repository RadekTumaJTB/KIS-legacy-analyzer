# Proces: ProjektTransakceAllExcelProcess

**Vstupní bod:** ProjektTransakceAllExcelProcess

## Přehled procesu

Tento business proces začíná na stránce **ProjektTransakceAllExcelProcess** a pokračuje přes 6 dalších kroků.

## Business Process Flow

```mermaid
flowchart TD
    Start([Začátek procesu]) --> Entry
    Entry[Zpracování]
    Step1[Projekty]
    Step2[UcSpol]
    Step3[Uvery]
    Step4[Detailní zobrazení]
    Step5[scriptlet_110]
    Step6[scriptlet_74]
    Entry --> Step1
    Step1 --> Step5
    Step5 --> Step3
    Step3 --> End
    End([Konec procesu])

    style Start fill:#90EE90
    style End fill:#FFB6C1
    style Entry fill:#87CEEB
```

## Kroky procesu

### Krok 1: Zpracování

- **Stránka:** `ProjektTransakceAllExcelProcess`
- **Typ:** Vstupní bod procesu

### Krok 2: Projekty

- **Stránka:** `Projekty`
- **Typ:** Procesní krok

### Krok 3: scriptlet_110

- **Stránka:** `scriptlet_110`
- **Typ:** Procesní krok

### Krok 4: Uvery

- **Stránka:** `Uvery`
- **Typ:** Konečný krok

## Alternativní flow

Proces má 4 různých variant flow:

1. Zpracování → Projekty → scriptlet_110 → Uvery
2. Zpracování → Projekty → scriptlet_110 → Detailní zobrazení
3. Zpracování → Projekty → scriptlet_110 → Uvery
4. Zpracování → Projekty → scriptlet_74 → UcSpol
