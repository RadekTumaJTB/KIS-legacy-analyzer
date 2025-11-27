# Proces začínající v Intro4

**Vstupní bod:** Intro4

## Přehled procesu

Tento business proces začíná na stránce **Intro4** a pokračuje přes 5 dalších kroků.

## Business Process Flow

```mermaid
flowchart TD
    Start([Začátek procesu]) --> Entry
    Entry[Intro4]
    Step1[DocPrijateFast]
    Step2[DocPrijatePuvodni]
    Step3[Intro]
    Step4[PwdChange]
    Step5[scriptlet_5]
    Entry --> Step3
    Step3 --> Step5
    Step5 --> Step2
    Step2 --> End
    End([Konec procesu])

    style Start fill:#90EE90
    style End fill:#FFB6C1
    style Entry fill:#87CEEB
```

## Kroky procesu

### Krok 1: Intro4

- **Stránka:** `Intro4`
- **Typ:** Vstupní bod procesu

### Krok 2: Intro

- **Stránka:** `Intro`
- **Typ:** Procesní krok

### Krok 3: scriptlet_5

- **Stránka:** `scriptlet_5`
- **Typ:** Procesní krok

### Krok 4: DocPrijatePuvodni

- **Stránka:** `DocPrijatePuvodni`
- **Typ:** Konečný krok

## Alternativní flow

Proces má 3 různých variant flow:

1. Intro4 → Intro → scriptlet_5 → DocPrijatePuvodni
2. Intro4 → Intro → scriptlet_5 → PwdChange
3. Intro4 → Intro → scriptlet_5 → DocPrijateFast
