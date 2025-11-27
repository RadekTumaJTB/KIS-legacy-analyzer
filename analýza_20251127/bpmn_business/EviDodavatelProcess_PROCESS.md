# Proces: EviDodavatelProcess

**Vstupní bod:** EviDodavatelProcess

## Přehled procesu

Tento business proces začíná na stránce **EviDodavatelProcess** a pokračuje přes 2 dalších kroků.

## Business Process Flow

```mermaid
flowchart TD
    Start([Začátek procesu]) --> Entry
    Entry[Zpracování]
    Step1[EviDodavatel]
    Step2[SpolecnostPs]
    Entry --> Step1
    Step1 --> Step2
    Step2 --> Step2
    Step2 --> End
    End([Konec procesu])

    style Start fill:#90EE90
    style End fill:#FFB6C1
    style Entry fill:#87CEEB
```

## Kroky procesu

### Krok 1: Zpracování

- **Stránka:** `EviDodavatelProcess`
- **Typ:** Vstupní bod procesu

### Krok 2: EviDodavatel

- **Stránka:** `EviDodavatel`
- **Typ:** Procesní krok

### Krok 3: SpolecnostPs

- **Stránka:** `SpolecnostPs`
- **Typ:** Procesní krok

### Krok 4: SpolecnostPs

- **Stránka:** `SpolecnostPs`
- **Typ:** Konečný krok

