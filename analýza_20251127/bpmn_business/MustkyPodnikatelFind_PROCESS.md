# Proces začínající v MustkyPodnikatelFind

**Vstupní bod:** MustkyPodnikatelFind

## Přehled procesu

Tento business proces začíná na stránce **MustkyPodnikatelFind** a pokračuje přes 4 dalších kroků.

## Business Process Flow

```mermaid
flowchart TD
    Start([Začátek procesu]) --> Entry
    Entry[MustkyPodnikatelFind]
    Step1[MustkyBunkaHist]
    Step2[Zpracování]
    Step3[scriptlet_38]
    Step4[scriptlet_49]
    Entry --> Step4
    Step4 --> Step1
    Step1 --> Step2
    Step2 --> End
    End([Konec procesu])

    style Start fill:#90EE90
    style End fill:#FFB6C1
    style Entry fill:#87CEEB
```

## Kroky procesu

### Krok 1: MustkyPodnikatelFind

- **Stránka:** `MustkyPodnikatelFind`
- **Typ:** Vstupní bod procesu

### Krok 2: scriptlet_49

- **Stránka:** `scriptlet_49`
- **Typ:** Procesní krok

### Krok 3: MustkyBunkaHist

- **Stránka:** `MustkyBunkaHist`
- **Typ:** Procesní krok

### Krok 4: Zpracování

- **Stránka:** `MustkyBunkaHistProcess`
- **Typ:** Konečný krok

## Alternativní flow

Proces má 2 různých variant flow:

1. MustkyPodnikatelFind → scriptlet_49 → MustkyBunkaHist → Zpracování
2. MustkyPodnikatelFind → scriptlet_38 → MustkyBunkaHist → Zpracování
