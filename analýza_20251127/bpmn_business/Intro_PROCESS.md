# Proces začínající v Intro

**Vstupní bod:** Intro

## Přehled procesu

Tento business proces začíná na stránce **Intro** a pokračuje přes 3 dalších kroků.

## Business Process Flow

```mermaid
flowchart TD
    Start([Začátek procesu]) --> Entry
    Entry[Intro]
    Step1[PwdChange]
    Step2[Zpracování]
    Step3[scriptlet_5]
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

### Krok 1: Intro

- **Stránka:** `Intro`
- **Typ:** Vstupní bod procesu

### Krok 2: scriptlet_5

- **Stránka:** `scriptlet_5`
- **Typ:** Procesní krok

### Krok 3: PwdChange

- **Stránka:** `PwdChange`
- **Typ:** Procesní krok

### Krok 4: Zpracování

- **Stránka:** `PwdChangeProcess`
- **Typ:** Konečný krok

