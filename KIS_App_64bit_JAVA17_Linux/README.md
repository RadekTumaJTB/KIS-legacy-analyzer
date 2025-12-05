# KIS Application - Java 17 Migration
## Windows 2003 (32-bit) → Linux UBI-base10 (64-bit)

**Status:** 🚧 In Progress - Infrastructure & Analysis Complete
**Datum:** 5. prosince 2025
**Target:** Java 17 LTS + Spring Boot 3.2.1 + Oracle 23c Free

---

## 📋 Projekt Overview

Tento projekt obsahuje migrovanou verzi KIS Banking Application z:
- **Původní:** Windows Server 2003 (32-bit), Java 1.4, Oracle ADF
- **Cílová:** Linux UBI-base10 (64-bit), Java 17 LTS, Spring Boot 3.2.1

### Klíčové Změny
- ✅ Platform-independent file paths
- ✅ UTF-8 encoding všude
- ✅ Modern logging (SLF4J + Logback místo Log4j 1.x)
- ✅ Jakarta EE místo javax.*
- ✅ Try-with-resources místo manual close()
- ✅ Generics místo raw types
- ✅ DateTimeFormatter místo SimpleDateFormat
- ✅ Apache POI XSSF (.xlsx) místo HSSF (.xls)

---

## 📂 Struktura Projektu

```
KIS_App_64bit_JAVA17_Linux/
├── Dockerfile                    # UBI-base10 + JDK 17 image
├── pom.xml                       # Maven dependencies (Spring Boot 3.2.1)
├── MIGRATION_PLAN.md            # Detailní migrace plán
├── README.md                    # Tento soubor
├── src/
│   ├── main/
│   │   ├── java/                # Java source code
│   │   │   └── cz/jtbank/      # Migrované třídy
│   │   └── resources/
│   │       ├── application.yml  # Konfigurace (Spring Boot)
│   │       └── logback.xml      # Logging konfigurace
│   └── test/
│       ├── java/                # Unit & Integration tests
│       └── resources/
└── logs/                        # Application logs (Docker volume)
```

---

## 🚀 Quick Start

### Požadavky
- Docker 24+
- Docker Compose 2.0+
- (Optional) Maven 3.9+ pro lokální build

### Spuštění s Docker Compose

```bash
# Z root složky KIS projektu
cd /Users/radektuma/DEV/KIS

# Spustit všechny služby (Qdrant, Neo4j, Oracle, KIS App)
docker-compose up -d

# Sledovat logy aplikace
docker logs -f kis-app-java17

# Otevřít shell v kontejneru
docker exec -it kis-app-java17 bash

# Zastavit služby
docker-compose down
```

### Porty
- **8080** - KIS Application
- **1521** - Oracle Database
- **7474** - Neo4j Browser
- **6333** - Qdrant API

---

## 📊 Migrace Status

### ✅ Dokončeno (Infrastructure)
1. ✅ Nová složka `KIS_App_64bit_JAVA17_Linux/` vytvořena
2. ✅ Dockerfile s UBI-base10 + JDK 17
3. ✅ Docker Compose s Oracle 23c Free
4. ✅ Maven POM s Spring Boot 3.2.1
5. ✅ Application.yml konfigurace
6. ✅ Maven struktura složek (`src/main/java`, `src/test/java`)

### ✅ Dokončeno (Analysis)
1. ✅ Analýza Java 1.4 kódu (386+ problémů identifikováno)
   - Viz: `../analýza_20251127/JAVA14_TO_JAVA17_DETAILED_CODE_ANALYSIS.md`
2. ✅ Analýza Windows závislostí (430+ hardcoded paths)
   - Viz: `../analýza_20251127/WINDOWS_TO_LINUX_MIGRATION_ANALYSIS.md`
3. ✅ Analýza custom knihoven (222 dependencies)
   - Viz: `../analýza_20251127/DEPENDENCY_ANALYSIS.md`

### 🚧 In Progress
1. 🔄 Maven build konfigurace
2. ⏳ Log4j → SLF4J migrace (PRIORITA #1 - CVE kritické!)
3. ⏳ Javax → Jakarta migrace
4. ⏳ Hardcoded Windows paths → Platform-independent

### ⏳ Plánováno
1. Static SimpleDateFormat → DateTimeFormatter (2 soubory)
2. Raw types → Generics (7 souborů)
3. Apache POI HSSF → XSSF (211 importů)
4. Manual resource management → Try-with-resources (175+ výskytů)
5. Unit & Integration testy
6. Oracle ADF → Spring Boot migration (dlouhodobý projekt)

---

## 🔥 Kritické Priority

### P1 - OKAMŽITĚ (1-2 týdny)
#### 1. Log4j 1.x → SLF4J + Logback
**Proč:** CVE-2021-44228 (Log4Shell) - kritická bezpečnostní zranitelnost
**Soubory:** 101 importů v aplikaci
**Úsilí:** 80-120 hodin

**Postup:**
```java
// PŘED
import org.apache.log4j.Logger;
private static Logger logger = Logger.getLogger(MyClass.class);
logger.info("Message");

// PO
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
private static final Logger logger = LoggerFactory.getLogger(MyClass.class);
logger.info("Message");
```

#### 2. Static SimpleDateFormat → DateTimeFormatter
**Proč:** Thread-safety issue (race conditions)
**Soubory:** 2 soubory (Utils.java, GenerateAll.java)
**Úsilí:** 16 hodin

```java
// PŘED (thread-unsafe!)
private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

// PO (thread-safe)
private static final DateTimeFormatter formatter =
    DateTimeFormatter.ofPattern("dd.MM.yyyy");
```

### P2 - VYSOKÁ (2-4 týdny)
1. Javax → Jakarta packages (12 importů)
2. Hardcoded Windows paths (84 souborů, 430 výskytů)
3. Raw types → Generics (7 souborů)

### P3 - STŘEDNÍ (1-2 měsíce)
1. Apache POI HSSF → XSSF (211 importů)
2. Manual resource management → Try-with-resources (175+ výskytů)
3. StringBuffer → StringBuilder (20+ souborů)

---

## 📖 Dokumentace

### Migrace Dokumenty
- **MIGRATION_PLAN.md** - Hlavní migrace plán (tento projekt)
- **../analýza_20251127/DEPENDENCY_README.md** - Index všech analýz
- **../analýza_20251127/DEPENDENCY_SUMMARY.txt** - Executive summary
- **../analýza_20251127/MIGRATION_QUICK_REFERENCE.md** - Developer guide

### Technické Analýzy
- **JAVA14_TO_JAVA17_DETAILED_CODE_ANALYSIS.md** - Java změny
- **WINDOWS_TO_LINUX_MIGRATION_ANALYSIS.md** - Platform změny
- **DEPENDENCY_ANALYSIS.md** - Knihovny a závislosti

### Build & Deploy
- **pom.xml** - Maven konfigurace
- **Dockerfile** - Container image definition
- **../docker-compose.yml** - Multi-container setup

---

## 🧪 Testing Strategy

### Unit Tests
```bash
mvn test
```

### Integration Tests (s Testcontainers)
```bash
mvn verify -Pintegration-tests
```

### Docker Tests
```bash
docker-compose -f docker-compose.test.yml up --abort-on-container-exit
```

---

## 📈 Metriky & Odhady

### Kód Statistiky
- **Java třídy:** 1,043
- **JSP stránky:** 656
- **Celkem řádky:** ~300,000+

### Identifikované Problémy
| Kategorie | Výskyty | Priorita |
|-----------|---------|----------|
| Log4j CVE | 101 | 🔥 P1 |
| Windows paths | 430 | 🔥 P1 |
| SimpleDateFormat | 2 | 🔥 P1 |
| Raw types | 7+ | ⚠️ P2 |
| Deprecated Date API | 100+ | ⚠️ P2 |
| Manual close() | 175+ | 📋 P3 |

### Timeline
- **S AI asistencí:** 13 týdnů (€52k)
- **Bez AI:** 30-35 týdnů (€120k-€140k)
- **Úspora:** 65-70% času a nákladů

---

## 🛠️ Development Tools

### Doporučené IDE
- IntelliJ IDEA 2024+ (Java 17 support)
- VS Code + Java Extension Pack

### Build Tools
- Maven 3.9+
- Docker 24+

### AI Assistants
- Claude Code (tento projekt!)
- GitHub Copilot

---

## 🤝 Contributing

### Code Style
- Java 17 syntax (records, switch expressions, text blocks)
- UTF-8 encoding všude
- Platform-independent paths
- Try-with-resources pro všechny closeable
- SLF4J logging

### Commit Messages
```
feat: migrate Log4j to SLF4J in common package
fix: replace hardcoded paths in export module
refactor: add generics to SchvalovakDTO
docs: update migration status in README
```

---

## 📞 Support & Contacts

Pro otázky a problémy:
- **Technická dokumentace:** Viz složka `../analýza_20251127/`
- **Build problémy:** Viz `pom.xml` a `MIGRATION_PLAN.md`
- **Docker problémy:** Viz `Dockerfile` a `../docker-compose.yml`

---

## ⚖️ License

Proprietární software - JT Bank, a.s.

---

**Poslední aktualizace:** 5. prosince 2025
**Status:** 🚧 Active Development - Infrastructure Complete, Code Migration In Progress
