# Proces: KapCisKategorieProtistranEditProcess

**Vstupní bod:** KapCisKategorieProtistranEditProcess

## Přehled procesu

Tento business proces začíná na stránce **KapCisKategorieProtistranEditProcess** a pokračuje přes 3 dalších kroků.

## Business Process Flow

```mermaid
flowchart TD
    Start([Začátek procesu]) --> Entry
    Entry[Editace dat]
    Step1[KapCisKategorieProtistran]
    Step2[Editace dat]
    Step3[SpolecnostPs]
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

### Krok 1: Editace dat

- **Stránka:** `KapCisKategorieProtistranEditProcess`
- **Typ:** Vstupní bod procesu

### Krok 2: KapCisKategorieProtistran

- **Stránka:** `KapCisKategorieProtistran`
- **Typ:** Procesní krok

### Krok 3: Editace dat

- **Stránka:** `KapCisKategorieProtistranEdit`
- **Typ:** Procesní krok

### Krok 4: SpolecnostPs

- **Stránka:** `SpolecnostPs`
- **Typ:** Konečný krok

