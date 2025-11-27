# Proces začínající v ProtiPoziceParovani

**Vstupní bod:** ProtiPoziceParovani

## Přehled procesu

Tento business proces začíná na stránce **ProtiPoziceParovani** a pokračuje přes 2 dalších kroků.

## Business Process Flow

```mermaid
flowchart TD
    Start([Začátek procesu]) --> Entry
    Entry[Párování záznamů]
    Step1[DokladyManDefPs]
    Step2[Párování záznamů]
    Entry --> Step2
    Step2 --> Entry
    Entry --> Step1
    Step1 --> End
    End([Konec procesu])

    style Start fill:#90EE90
    style End fill:#FFB6C1
    style Entry fill:#87CEEB
```

## Kroky procesu

### Krok 1: Párování záznamů

- **Stránka:** `ProtiPoziceParovani`
- **Typ:** Vstupní bod procesu

### Krok 2: Párování záznamů

- **Stránka:** `ProtiPoziceParovaniProcess`
- **Typ:** Procesní krok

### Krok 3: Párování záznamů

- **Stránka:** `ProtiPoziceParovani`
- **Typ:** Procesní krok

### Krok 4: DokladyManDefPs

- **Stránka:** `DokladyManDefPs`
- **Typ:** Konečný krok

