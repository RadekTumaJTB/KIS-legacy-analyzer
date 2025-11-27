# Proces začínající v ProtistranyRedir

**Vstupní bod:** ProtistranyRedir

## Přehled procesu

Tento business proces začíná na stránce **ProtistranyRedir** a pokračuje přes 3 dalších kroků.

## Business Process Flow

```mermaid
flowchart TD
    Start([Začátek procesu]) --> Entry
    Entry[ProtistranyRedir]
    Step1[Protistrany]
    Step2[Detailní zobrazení]
    Step3[ProtistranyUpdate]
    Entry --> Step3
    Step3 --> Step1
    Step1 --> Step2
    Step2 --> End
    End([Konec procesu])

    style Start fill:#90EE90
    style End fill:#FFB6C1
    style Entry fill:#87CEEB
```

## Kroky procesu

### Krok 1: ProtistranyRedir

- **Stránka:** `ProtistranyRedir`
- **Typ:** Vstupní bod procesu

### Krok 2: ProtistranyUpdate

- **Stránka:** `ProtistranyUpdate`
- **Typ:** Procesní krok

### Krok 3: Protistrany

- **Stránka:** `Protistrany`
- **Typ:** Procesní krok

### Krok 4: Detailní zobrazení

- **Stránka:** `ProtistranyDetailHist`
- **Typ:** Konečný krok

