# Proces začínající v Intro3

**Vstupní bod:** Intro3

## Přehled procesu

Tento business proces začíná na stránce **Intro3** a pokračuje přes 5 dalších kroků.

## Business Process Flow

```mermaid
flowchart TD
    Start([Začátek procesu]) --> Entry
    Entry[Intro3]
    Step1[DocPrijateFast]
    Step2[DocPrijatePuvodni]
    Step3[Intro]
    Step4[PwdChange]
    Step5[scriptlet_5]
    Entry --> Step3
    Step3 --> Step5
    Step5 --> Step1
    Step1 --> End
    End([Konec procesu])

    style Start fill:#90EE90
    style End fill:#FFB6C1
    style Entry fill:#87CEEB
```

## Kroky procesu

### Krok 1: Intro3

- **Stránka:** `Intro3`
- **Typ:** Vstupní bod procesu

### Krok 2: Intro

- **Stránka:** `Intro`
- **Typ:** Procesní krok

### Krok 3: scriptlet_5

- **Stránka:** `scriptlet_5`
- **Typ:** Procesní krok

### Krok 4: DocPrijateFast

- **Stránka:** `DocPrijateFast`
- **Typ:** Konečný krok

## Alternativní flow

Proces má 3 různých variant flow:

1. Intro3 → Intro → scriptlet_5 → DocPrijateFast
2. Intro3 → Intro → scriptlet_5 → PwdChange
3. Intro3 → Intro → scriptlet_5 → DocPrijatePuvodni
