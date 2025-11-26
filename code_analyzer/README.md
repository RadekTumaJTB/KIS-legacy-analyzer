# Code Analyzer - Legacy Migration Tool

Nástroj pro analýzu legacy kódu a přípravu na migraci do moderních technologií.

## 🎯 Účel

Tento nástroj analyzuje legacy aplikaci (Java, JSP, SQL) a připravuje ji pro migraci:

1. **Rozdělí kód na logické chunky** podle tříd, metod, SQL objektů
2. **Zvektorizuje chunky** pomocí embeddings pro sémantické vyhledávání
3. **Nahraje do QdrantDB** pro rychlé vyhledávání podobného kódu
4. **Vytvoří graf závislostí v Neo4j** pro vizualizaci a analýzu
5. **Vygeneruje migration reports** s doporučeními

## 📋 Požadavky

### Software

- Python 3.8+
- Docker a Docker Compose (pro databáze)
- Git

### Python balíčky

Viz `requirements.txt`

## 🚀 Instalace

### 1. Nastavte prostředí

```bash
# Vytvořte virtuální prostředí
python3 -m venv venv
source venv/bin/activate  # Linux/Mac
# nebo
venv\Scripts\activate  # Windows

# Nainstalujte závislosti
pip install -r requirements.txt
```

### 2. Spusťte databáze pomocí Docker

Vytvořte `docker-compose.yml`:

```yaml
version: '3.8'

services:
  qdrant:
    image: qdrant/qdrant:latest
    ports:
      - "6333:6333"
      - "6334:6334"
    volumes:
      - qdrant_storage:/qdrant/storage

  neo4j:
    image: neo4j:5.14
    ports:
      - "7474:7474"  # HTTP
      - "7687:7687"  # Bolt
    environment:
      - NEO4J_AUTH=neo4j/your_password_here
      - NEO4J_PLUGINS=["apoc"]
    volumes:
      - neo4j_data:/data
      - neo4j_logs:/logs

volumes:
  qdrant_storage:
  neo4j_data:
  neo4j_logs:
```

Spusťte databáze:

```bash
docker-compose up -d
```

### 3. Nakonfigurujte prostředí

Vytvořte `.env` soubor:

```bash
cp .env.example .env
```

Upravte `.env`:

```env
# Qdrant Configuration
QDRANT_HOST=localhost
QDRANT_PORT=6333

# Neo4j Configuration
NEO4J_URI=bolt://localhost:7687
NEO4J_USER=neo4j
NEO4J_PASSWORD=your_password_here

# Embedding Model
EMBEDDING_MODEL=sentence-transformers/all-MiniLM-L6-v2
```

## 🎬 Použití

### Základní spuštění

```bash
# Spusťte kompletní analýzu
python run_analysis.py
```

### Pokročilé možnosti

```bash
# Použít existující data (bez reinicializace databází)
python run_analysis.py --no-init-dbs

# Pouze vygenerovat reporty z existujících dat
python run_analysis.py --report-only
```

### Programatické použití

```python
from code_analyzer import CodeAnalyzerPipeline

pipeline = CodeAnalyzerPipeline()
result = pipeline.run(initialize_dbs=True)

print(f"Analyzed {result['total_chunks']} code chunks")
print(f"Found {result['total_dependencies']} dependencies")
```

## 📊 Výstupy

### 1. JSON soubory

- `analysis_output/chunks.json` - Všechny chunky s metadaty
- `analysis_output/reports/migration_analysis_report.json` - Detailní analýza

### 2. Markdown reporty

- `analysis_output/reports/MIGRATION_SUMMARY.md` - Lidsky čitelný souhrn

### 3. Databáze

#### QdrantDB (Vector Database)

- URL: http://localhost:6333/dashboard
- Obsahuje zvektorizované chunky kódu
- Umožňuje sémantické vyhledávání

**Příklad dotazu:**

```python
from code_analyzer.storage import QdrantStore

qdrant = QdrantStore()
results = qdrant.search_similar(
    query="user authentication logic",
    limit=10
)

for result in results:
    print(f"{result['name']} - Score: {result['score']}")
```

#### Neo4j (Graph Database)

- URL: http://localhost:7474 (Neo4j Browser)
- Obsahuje graf závislostí mezi chunky
- Umožňuje vizualizaci a Cypher dotazy

**Příklady Cypher dotazů:**

```cypher
// Najít nejvíce propojené komponenty
MATCH (c:CodeChunk)
OPTIONAL MATCH (c)-[r:DEPENDS_ON]-()
RETURN c.name, c.chunk_type, count(r) as connections
ORDER BY connections DESC
LIMIT 20

// Najít circular dependencies
MATCH path = (c:CodeChunk)-[:DEPENDS_ON*2..5]->(c)
RETURN c.name, length(path) as cycle_length
LIMIT 10

// Najít všechny závislosti konkrétní třídy
MATCH (c:CodeChunk {name: "AdminModuleImpl"})
MATCH path = (c)-[:DEPENDS_ON*1..3]->(dep)
RETURN path
```

## 🏗️ Architektura

```
code_analyzer/
├── config.py              # Konfigurace
├── models.py              # Datové modely
├── chunkers/              # Rozdělování kódu
│   ├── java_chunker.py
│   ├── jsp_chunker.py
│   └── sql_chunker.py
├── analyzers/             # Analýza závislostí
│   └── dependency_analyzer.py
├── storage/               # Databázové konektory
│   ├── qdrant_store.py
│   └── neo4j_store.py
└── main.py               # Hlavní pipeline
```

## 🔍 Chunking strategie

### Java
- **Class chunks**: Hlavička třídy s poli
- **Method chunks**: Jednotlivé metody
- Zachovává: imports, extends, implements, modifikátory

### JSP
- **Page chunk**: Celková struktura stránky
- **Scriptlet chunks**: Java kód v JSP
- Zachovává: includes, taglibs, direktivy

### SQL
- **Object chunks**: Tables, Views, Procedures, Functions
- Rozdělení po SQL statementech
- Zachovává: referenční integrity, JOINy

## 🎯 Migration workflow

1. **Spusťte analýzu**: `python run_analysis.py`
2. **Prostudujte reports**: Začněte s `MIGRATION_SUMMARY.md`
3. **Analyzujte graf v Neo4j**: Najděte kritické komponenty
4. **Hledejte podobný kód v Qdrant**: Identifikujte duplicity
5. **Naplánujte migraci po clusterech**: Použijte migration clusters

## 🛠️ Troubleshooting

### Qdrant connection error

```bash
# Ověřte, že Qdrant běží
curl http://localhost:6333/
```

### Neo4j connection error

```bash
# Zkontrolujte Neo4j logy
docker logs <neo4j-container-id>

# Ověřte credentials v .env
```

### Out of memory při embedování

```python
# V config.py snižte batch_size
CHUNK_STRATEGIES = {
    "java": {
        "max_lines": 100,  # Menší chunky
        ...
    }
}
```

## 📝 License

MIT License

## 👥 Autoři

Vytvořeno pro migraci legacy banking aplikace KIS.
