# Proces začínající v DocCompBudgetDefine

**Vstupní bod:** DocCompBudgetDefine

## Přehled procesu

Tento business proces začíná na stránce **DocCompBudgetDefine** a pokračuje přes 3 dalších kroků.

## Business Process Flow

```mermaid
flowchart TD
    Start([Začátek procesu]) --> Entry
    Entry[DocCompBudgetDefine]
    Step1[Zpracování]
    Step2[Editace dat]
    Step3[scriptlet_6]
    Entry --> Step1
    Step1 --> Step3
    Step3 --> Step2
    Step2 --> End
    End([Konec procesu])

    style Start fill:#90EE90
    style End fill:#FFB6C1
    style Entry fill:#87CEEB
```

## Kroky procesu

### Krok 1: DocCompBudgetDefine

- **Stránka:** `DocCompBudgetDefine`
- **Typ:** Vstupní bod procesu

### Krok 2: Zpracování

- **Stránka:** `DocCompBudgetDefineProcess`
- **Typ:** Procesní krok

### Krok 3: scriptlet_6

- **Stránka:** `scriptlet_6`
- **Typ:** Procesní krok

### Krok 4: Editace dat

- **Stránka:** `DocCompBudgetEdit`
- **Typ:** Konečný krok

