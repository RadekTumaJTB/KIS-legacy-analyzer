# Proces: SubPravidlaProcess

**Vstupní bod:** SubPravidlaProcess

## Přehled procesu

Tento business proces začíná na stránce **SubPravidlaProcess** a pokračuje přes 2 dalších kroků.

## Business Process Flow

```mermaid
flowchart TD
    Start([Začátek procesu]) --> Entry
    Entry[Zpracování]
    Step1[SubPravidla]
    Step2[scriptlet_22]
    Entry --> Step1
    Step1 --> Step2
    Step2 --> Step1
    Step1 --> End
    End([Konec procesu])

    style Start fill:#90EE90
    style End fill:#FFB6C1
    style Entry fill:#87CEEB
```

## Kroky procesu

### Krok 1: Zpracování

- **Stránka:** `SubPravidlaProcess`
- **Typ:** Vstupní bod procesu

### Krok 2: SubPravidla

- **Stránka:** `SubPravidla`
- **Typ:** Procesní krok

### Krok 3: scriptlet_22

- **Stránka:** `scriptlet_22`
- **Typ:** Procesní krok

### Krok 4: SubPravidla

- **Stránka:** `SubPravidla`
- **Typ:** Konečný krok

