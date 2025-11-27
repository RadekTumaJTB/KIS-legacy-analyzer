# Proces začínající v DokladySubkonsFronta

**Vstupní bod:** DokladySubkonsFronta

## Přehled procesu

Tento business proces začíná na stránce **DokladySubkonsFronta** a pokračuje přes 4 dalších kroků.

## Business Process Flow

```mermaid
flowchart TD
    Start([Začátek procesu]) --> Entry
    Entry[DokladySubkonsFronta]
    Step1[DokladySubkons]
    Step2[DokladySubkonsStarsi]
    Step3[scriptlet_3]
    Step4[scriptlet_48]
    Entry --> Entry
    Entry --> Step3
    Step3 --> Step2
    Step2 --> End
    End([Konec procesu])

    style Start fill:#90EE90
    style End fill:#FFB6C1
    style Entry fill:#87CEEB
```

## Kroky procesu

### Krok 1: DokladySubkonsFronta

- **Stránka:** `DokladySubkonsFronta`
- **Typ:** Vstupní bod procesu

### Krok 2: DokladySubkonsFronta

- **Stránka:** `DokladySubkonsFronta`
- **Typ:** Procesní krok

### Krok 3: scriptlet_3

- **Stránka:** `scriptlet_3`
- **Typ:** Procesní krok

### Krok 4: DokladySubkonsStarsi

- **Stránka:** `DokladySubkonsStarsi`
- **Typ:** Konečný krok

## Alternativní flow

Proces má 5 různých variant flow:

1. DokladySubkonsFronta → DokladySubkonsFronta → scriptlet_3 → DokladySubkonsStarsi
2. DokladySubkonsFronta → scriptlet_3 → DokladySubkonsFronta → DokladySubkons
3. DokladySubkonsFronta → scriptlet_3 → DokladySubkonsFronta → DokladySubkonsStarsi
4. DokladySubkonsFronta → DokladySubkonsFronta → scriptlet_3 → DokladySubkons
5. DokladySubkonsFronta → DokladySubkons → scriptlet_48 → DokladySubkons
