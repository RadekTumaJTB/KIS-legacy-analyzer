# Proces začínající v DokladyGenDelete

**Vstupní bod:** DokladyGenDelete

## Přehled procesu

Tento business proces začíná na stránce **DokladyGenDelete** a pokračuje přes 5 dalších kroků.

## Business Process Flow

```mermaid
flowchart TD
    Start([Začátek procesu]) --> Entry
    Entry[DokladyGenDelete]
    Step1[Doklady]
    Step2[DokladyMan]
    Step3[DokladyManStarsi]
    Step4[Zpracování]
    Step5[DokladySpol]
    Entry --> Step4
    Step4 --> Step4
    Step4 --> Step2
    Step2 --> End
    End([Konec procesu])

    style Start fill:#90EE90
    style End fill:#FFB6C1
    style Entry fill:#87CEEB
```

## Kroky procesu

### Krok 1: DokladyGenDelete

- **Stránka:** `DokladyGenDelete`
- **Typ:** Vstupní bod procesu

### Krok 2: Zpracování

- **Stránka:** `DokladyProcessGen`
- **Typ:** Procesní krok

### Krok 3: Zpracování

- **Stránka:** `DokladyProcessGen`
- **Typ:** Procesní krok

### Krok 4: DokladyMan

- **Stránka:** `DokladyMan`
- **Typ:** Konečný krok

## Alternativní flow

Proces má 4 různých variant flow:

1. DokladyGenDelete → Zpracování → Zpracování → DokladyMan
2. DokladyGenDelete → Zpracování → Zpracování → DokladySpol
3. DokladyGenDelete → Zpracování → Zpracování → Doklady
4. DokladyGenDelete → Zpracování → Zpracování → DokladyManStarsi
