# Proces: MustkyPodnikatelProcess

**Vstupní bod:** MustkyPodnikatelProcess

## Přehled procesu

Tento business proces začíná na stránce **MustkyPodnikatelProcess** a pokračuje přes 7 dalších kroků.

## Business Process Flow

```mermaid
flowchart TD
    Start([Začátek procesu]) --> Entry
    Entry[Zpracování]
    Step1[MustkyBunkaHist]
    Step2[MustkyPodnikatel]
    Step3[MustkyPodnikatelFind]
    Step4[scriptlet_38]
    Step5[scriptlet_49]
    Step6[scriptlet_61]
    Step7[scriptlet_73]
    Entry --> Step2
    Step2 --> Step6
    Step6 --> Step1
    Step1 --> End
    End([Konec procesu])

    style Start fill:#90EE90
    style End fill:#FFB6C1
    style Entry fill:#87CEEB
```

## Kroky procesu

### Krok 1: Zpracování

- **Stránka:** `MustkyPodnikatelProcess`
- **Typ:** Vstupní bod procesu

### Krok 2: MustkyPodnikatel

- **Stránka:** `MustkyPodnikatel`
- **Typ:** Procesní krok

### Krok 3: scriptlet_61

- **Stránka:** `scriptlet_61`
- **Typ:** Procesní krok

### Krok 4: MustkyBunkaHist

- **Stránka:** `MustkyBunkaHist`
- **Typ:** Konečný krok

## Alternativní flow

Proces má 4 různých variant flow:

1. Zpracování → MustkyPodnikatel → scriptlet_61 → MustkyBunkaHist
2. Zpracování → MustkyPodnikatelFind → scriptlet_49 → MustkyBunkaHist
3. Zpracování → MustkyPodnikatelFind → scriptlet_38 → MustkyBunkaHist
4. Zpracování → MustkyPodnikatel → scriptlet_73 → MustkyBunkaHist
