# Proces: DocFilterSpolProcess

**Vstupní bod:** DocFilterSpolProcess

## Přehled procesu

Tento business proces začíná na stránce **DocFilterSpolProcess** a pokračuje přes 2 dalších kroků.

## Business Process Flow

```mermaid
flowchart TD
    Start([Začátek procesu]) --> Entry
    Entry[Zpracování]
    Step1[DocFilterSpol]
    Step2[Intro]
    Entry --> Step1
    Step1 --> Step1
    Step1 --> Step2
    Step2 --> End
    End([Konec procesu])

    style Start fill:#90EE90
    style End fill:#FFB6C1
    style Entry fill:#87CEEB
```

## Kroky procesu

### Krok 1: Zpracování

- **Stránka:** `DocFilterSpolProcess`
- **Typ:** Vstupní bod procesu

### Krok 2: DocFilterSpol

- **Stránka:** `DocFilterSpol`
- **Typ:** Procesní krok

### Krok 3: DocFilterSpol

- **Stránka:** `DocFilterSpol`
- **Typ:** Procesní krok

### Krok 4: Intro

- **Stránka:** `Intro`
- **Typ:** Konečný krok

## Alternativní flow

Proces má 3 různých variant flow:

1. Zpracování → DocFilterSpol → DocFilterSpol → Intro
2. Zpracování → DocFilterSpol → DocFilterSpol
3. Zpracování → DocFilterSpol → Intro
