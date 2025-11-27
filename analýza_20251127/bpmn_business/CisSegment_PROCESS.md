# Proces začínající v CisSegment

**Vstupní bod:** CisSegment

## Přehled procesu

Tento business proces začíná na stránce **CisSegment** a pokračuje přes 3 dalších kroků.

## Business Process Flow

```mermaid
flowchart TD
    Start([Začátek procesu]) --> Entry
    Entry[CisSegment]
    Step1[CisMngSegment]
    Step2[Editace dat]
    Step3[Editace dat]
    Entry --> Step1
    Step1 --> Step2
    Step2 --> Step3
    Step3 --> End
    End([Konec procesu])

    style Start fill:#90EE90
    style End fill:#FFB6C1
    style Entry fill:#87CEEB
```

## Kroky procesu

### Krok 1: CisSegment

- **Stránka:** `CisSegment`
- **Typ:** Vstupní bod procesu

### Krok 2: CisMngSegment

- **Stránka:** `CisMngSegment`
- **Typ:** Procesní krok

### Krok 3: Editace dat

- **Stránka:** `CisMngSegmentEdit`
- **Typ:** Procesní krok

### Krok 4: Editace dat

- **Stránka:** `CisMngSegmentEditProcess`
- **Typ:** Konečný krok

