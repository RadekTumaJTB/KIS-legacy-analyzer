# Quick Start Guide - KIS Legacy Code Analyzer

## 🚀 Rychlé spuštění za 5 minut

### 1. Spusťte databáze (Docker)

```bash
# V root adresáři projektu KIS
docker-compose up -d

# Ověřte, že běží
docker ps
```

Měli byste vidět dva kontejnery:
- `kis-qdrant` (ports 6333, 6334)
- `kis-neo4j` (ports 7474, 7687)

### 2. Nastavte Python prostředí

```bash
cd code_analyzer

# Vytvořte virtuální prostředí
python3 -m venv venv

# Aktivujte
source venv/bin/activate  # Linux/Mac
# nebo
venv\Scripts\activate     # Windows

# Nainstalujte závislosti
pip install -r requirements.txt
```

**Poznámka**: První instalace může trvat několik minut (stahují se ML modely).

### 3. Spusťte analýzu

```bash
# Spusťte kompletní analýzu
python run_analysis.py
```

Proces bude trvat cca 10-30 minut v závislosti na velikosti codebase.

### 4. Prozkoumejte výsledky

#### A) Markdown Summary (nejrychlejší přehled)

```bash
cat ../analysis_output/reports/MIGRATION_SUMMARY.md
```

#### B) Neo4j Browser (vizuální graf)

1. Otevřete: http://localhost:7474
2. Přihlaste se:
   - Username: `neo4j`
   - Password: `kis_password_2024`

3. Vyzkoušejte dotazy:

```cypher
// Zobrazit všechny chunky
MATCH (c:CodeChunk)
RETURN c
LIMIT 25

// Nejvíce propojené komponenty
MATCH (c:CodeChunk)
OPTIONAL MATCH (c)-[r:DEPENDS_ON]-()
RETURN c.name, c.chunk_type, count(r) as connections
ORDER BY connections DESC
LIMIT 20

// Závislosti konkrétní třídy
MATCH (c:CodeChunk {name: "AdminModuleImpl"})
MATCH path = (c)-[:DEPENDS_ON*1..2]->(dep)
RETURN path
```

#### C) Qdrant Dashboard (vektorové vyhledávání)

1. Otevřete: http://localhost:6333/dashboard
2. Můžete procházet kolekci `code_chunks`

## 📊 Co očekávat

### Výstupní soubory

Po dokončení najdete v `analysis_output/`:

```
analysis_output/
├── chunks.json                    # Všechny chunky (JSON)
└── reports/
    ├── migration_analysis_report.json  # Detailní analýza
    └── MIGRATION_SUMMARY.md            # Lidsky čitelný souhrn
```

### Typické statistiky

Pro KIS codebase (1043 Java + 644 JSP + 2712 SQL):

- **Total Chunks**: ~8,000-12,000
- **Dependencies**: ~15,000-25,000
- **Processing Time**: 15-30 minut

## 🎯 Co dělat dál?

### 1. Identifikujte critical components

```cypher
// V Neo4j Browser
MATCH (c:CodeChunk)
OPTIONAL MATCH (c)-[r:DEPENDS_ON]-()
WITH c, count(r) as connections
WHERE connections > 10
RETURN c.name, c.package, connections
ORDER BY connections DESC
```

### 2. Najděte migration clusters

```cypher
MATCH (c:CodeChunk)
WHERE c.package CONTAINS 'admin'
MATCH path = (c)-[:DEPENDS_ON*1..2]-(related)
RETURN path
LIMIT 100
```

### 3. Hledejte podobný kód (v Pythonu)

```python
from code_analyzer.storage import QdrantStore

qdrant = QdrantStore()

# Najít kód související s autentizací
results = qdrant.search_similar(
    query="user authentication and login",
    limit=10
)

for r in results:
    print(f"{r['name']} ({r['chunk_type']}) - Score: {r['score']:.3f}")
    print(f"File: {r['file_path']}\n")
```

### 4. Exportujte data pro další analýzu

```python
import json
from code_analyzer.storage import Neo4jStore

neo4j = Neo4jStore()

# Získat analýzu
analysis = neo4j.analyze_dependencies()

# Uložit do souboru
with open('dependency_analysis.json', 'w') as f:
    json.dump(analysis, f, indent=2)
```

## ⚠️ Řešení problémů

### Docker kontejnery neběží

```bash
# Zkontrolovat logy
docker-compose logs qdrant
docker-compose logs neo4j

# Restartovat
docker-compose restart
```

### Connection error k databázím

```bash
# Test Qdrant
curl http://localhost:6333/

# Test Neo4j
curl http://localhost:7474/
```

### Python ModuleNotFoundError

```bash
# Ujistěte se, že jste v správném adresáři
cd code_analyzer

# Ujistěte se, že máte aktivované venv
source venv/bin/activate

# Reinstalujte závislosti
pip install -r requirements.txt
```

### Out of Memory

Pokud máte málo RAM, upravte `config.py`:

```python
# Menší batch size
CHUNK_STRATEGIES = {
    "java": {
        "max_lines": 100,  # Původně 200
        ...
    }
}
```

## 📞 Další pomoc

Podívejte se do `code_analyzer/README.md` pro detailní dokumentaci.

---

**Tip**: Při prvním spuštění doporučuji nechat analýzu běžet přes noc, aby měl počítač dost času na zpracování všech souborů a embedování kódu.
