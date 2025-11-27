# Proces: DocCisNewUsekEditProcess

**Vstupní bod:** DocCisNewUsekEditProcess

## Přehled procesu

Tento business proces začíná na stránce **DocCisNewUsekEditProcess** a pokračuje přes 3 dalších kroků.

## Business Process Flow

```mermaid
flowchart TD
    Start([Začátek procesu]) --> Entry
    Entry[Editace dat]
    Step1[DocCisNewUsek]
    Step2[Editace dat]
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

### Krok 1: Editace dat

- **Stránka:** `DocCisNewUsekEditProcess`
- **Typ:** Vstupní bod procesu

### Krok 2: scriptlet_5

- **Stránka:** `scriptlet_5`
- **Typ:** Procesní krok

### Krok 3: DocCisNewUsek

- **Stránka:** `DocCisNewUsek`
- **Typ:** Procesní krok

### Krok 4: Editace dat

- **Stránka:** `DocCisNewUsekEdit`
- **Typ:** Konečný krok

