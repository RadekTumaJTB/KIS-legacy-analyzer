# KIS Application Migration Plan
## JAVA 1.4 (Windows 2003) → JAVA 17 (Linux UBI-base10)

**Datum:** 5. prosince 2025
**Verze:** 1.0
**Status:** In Progress

---

## Executive Summary

### Současný Stav
- **Platforma:** Windows Server 2003 (32-bit)
- **Java Runtime:** Java 1.4
- **Databáze:** Oracle
- **Kód:** 1,043 Java tříd, 656 JSP stránek

### Cílový Stav
- **Platforma:** Linux (Red Hat UBI-minimal 10) - 64-bit
- **Java Runtime:** Java 17 LTS
- **Databáze:** Oracle 23c Free
- **Container:** Docker s JDK 17

---

## Identifikované Problémy

### 1. KRITICKÉ - Java 1.4 → Java 17

| Kategorie | Výskytů | Soubory | Priority | Úsilí |
|-----------|---------|---------|----------|-------|
| **Static SimpleDateFormat** | 2 | 2 | 🔴 P1 | 2 dny |
| **Raw Types (bez generics)** | 7+ | 7 | 🔴 P1 | 3 dny |
| **Deprecated Date/Time API** | 100+ | 50+ | 🟡 P2 | 1 týden |
| **StringBuffer → StringBuilder** | 20+ | 20+ | 🟢 P3 | 2 dny |
| **Manual Resource Management** | 175+ | 20+ | 🟡 P2 | 2 týdny |

**Celkem:** 4.5 týdne s AI asistencí (vs. 13-17 týdnů manuálně)

### 2. KRITICKÉ - Windows → Linux

| Kategorie | Výskytů | Soubory | Priority | Úsilí |
|-----------|---------|---------|----------|-------|
| **Hardcoded File Paths** | 430 | 84 | 🔴 P1 | 2 týdny |
| **Encoding Issues** | 6,525 | 644 | 🟡 P2 | 1 týden |
| **JNI/Native Libraries** | 30 | 2 | 🔴 P1 | 1 týden |
| **Process Management** | 15 | 1 | 🟢 P3 | 1 den |
| **Line Endings** | 5 | 1 | 🟢 P4 | Auto |

**Celkem:** 4 týdny s AI asistencí

---

## Migrace Strategie

### Fáze 1: Příprava Infrastruktury (✅ DOKONČENO)
- ✅ Vytvoření nové složky `KIS_App_64bit_JAVA17_Linux/`
- ✅ Docker kontejner s UBI-base10 + JDK 17
- ✅ Oracle 23c Free databáze v Docker compose
- ✅ Dockerfile s best practices (non-root user, health checks)

### Fáze 2: Analýza a Dokumentace (✅ DOKONČENO)
- ✅ Analýza Java 1.4 kódu (386+ problémů identifikováno)
- ✅ Analýza Windows závislostí (430+ hardcoded paths)
- ✅ Identifikace custom knihoven
- ✅ Vytvoření migračního plánu

### Fáze 3: Identifikace Custom Knihoven (🔄 IN PROGRESS)
- 🔄 Analýza všech importů a závislostí
- ⏳ Identifikace deprecated/incompatible knihoven
- ⏳ Návrh moderních náhrad (Spring Boot, Jakarta EE)

### Fáze 4: Code Migration - JAVA 17
**Priority P1 (KRITICKÉ):**
1. Static SimpleDateFormat → DateTimeFormatter (2 soubory)
   - `Utils.java`
   - `GenerateAll.java`

2. Raw Types → Generics (7 souborů)
   - `SchvalovakDTO.java`
   - `AutoProtokolNew.java`
   - `SystemStatus.java`
   - `Logging.java`
   - další...

3. JNI/Native Libraries
   - Identifikovat Windows DLL závislosti
   - Najít Linux ekvivalenty nebo refaktorovat

**Priority P2 (VYSOKÁ):**
1. Deprecated Date/Time API → java.time.* (50+ souborů)
2. Manual Resource Management → Try-with-resources (175+ výskytů)
3. Encoding fixes (644 souborů)

**Priority P3 (STŘEDNÍ):**
1. StringBuffer → StringBuilder (20+ souborů)
2. Old-style loops → Enhanced for-loops (65+ výskytů)

### Fáze 5: Platform Migration - Windows → Linux
**Priority P1 (KRITICKÉ):**
1. Hardcoded Windows Paths (84 souborů)
   - Refactor na platform-independent paths
   - Configuration-driven paths
   - Použití `Paths.get()` a `File.separator`

2. File System Case Sensitivity
   - Audit všech file operations
   - Unit testy na Linux

**Priority P2 (VYSOKÁ):**
1. Encoding Issues (644 souborů)
   - Windows-1250 → UTF-8
   - Explicit charset specification

### Fáze 6: Dependency Management
- Aktualizace všech knihoven na Java 17 kompatibilní verze
- Maven/Gradle build system setup
- Dependency vulnerability scan

### Fáze 7: Testing & Validation
1. Unit Tests
   - Vytvoření testů pro všechny migrované komponenty
   - Mock objekty pro Oracle databázi

2. Integration Tests
   - Testování na Linux prostředí
   - Docker integration tests

3. Performance Tests
   - Load testing
   - Memory profiling (32-bit → 64-bit)

4. Security Audit
   - OWASP dependency check
   - Code security scan

### Fáze 8: Deployment
1. CI/CD pipeline setup
2. Staging environment
3. Production deployment
4. Rollback plan

---

## Migrace Tools & Technologies

### Development Tools
- **IDE:** IntelliJ IDEA 2024+ (Java 17 support)
- **Build:** Maven 3.9+ / Gradle 8+
- **Version Control:** Git
- **AI Assistance:** Claude Code, GitHub Copilot

### Testing Tools
- **Unit Testing:** JUnit 5
- **Integration Testing:** Testcontainers
- **Performance:** JMeter, VisualVM
- **Security:** OWASP Dependency Check, SonarQube

### Deployment
- **Container:** Docker 24+
- **Orchestration:** Docker Compose (dev), Kubernetes (prod)
- **CI/CD:** GitHub Actions / Jenkins

---

## Risk Assessment

### High Risk Items
1. **JNI/Native Libraries** - Vyžadují rekompilaci pro Linux 64-bit
2. **Static SimpleDateFormat** - Thread-safety issue (CRITICAL)
3. **Encoding** - Potenciální data corruption

### Mitigation Strategy
1. Paralelní běh Windows a Linux verzí (2-3 měsíce)
2. Comprehensive testing před switchover
3. Rollback plan připraven
4. Monitoring a alerting

---

## Timeline & Effort

### S AI Asistencí (Claude Code + Copilot)
| Fáze | Úsilí | Týdny |
|------|-------|-------|
| 1. Infrastruktura | ✅ | ✅ |
| 2. Analýza | ✅ | ✅ |
| 3. Custom knihovny | 🔄 | 0.5 |
| 4. Code Migration | ⏳ | 4.5 |
| 5. Platform Migration | ⏳ | 4 |
| 6. Dependencies | ⏳ | 1 |
| 7. Testing | ⏳ | 2 |
| 8. Deployment | ⏳ | 1 |
| **TOTAL** | | **13 týdnů** |

### Bez AI (Manuálně)
- **Odhad:** 30-35 týdnů
- **Úspora s AI:** 65-70%

---

## Cost Estimation

### S AI Asistencí
- **Úsilí:** 13 týdnů × 5 dní = 65 person-days
- **Náklady:** €52,000 (@€800/den)

### Bez AI
- **Úsilí:** 30-35 týdnů = 150-175 person-days
- **Náklady:** €120,000-€140,000

**Úspora:** €68,000-€88,000 (58-63%)

---

## Success Criteria

### Functional Requirements
✅ All existing functionality works on Linux
✅ Database operations maintain compatibility
✅ No regression in features

### Non-Functional Requirements
✅ Performance: ≥ current performance (leverage 64-bit)
✅ Security: Pass OWASP security scan
✅ Stability: 99.9% uptime
✅ Scalability: Support 2x current load

### Code Quality
✅ 80%+ test coverage
✅ Zero critical security vulnerabilities
✅ Zero P1/P2 code quality issues
✅ Full Java 17 compatibility

---

## Next Steps (Immediate)

1. ✅ Docker infrastruktura připravena
2. 🔄 Identifikace custom knihoven a závislostí
3. ⏳ Vytvoření Maven/Gradle build souboru
4. ⏳ Migrace prvních P1 souborů (SimpleDateFormat)
5. ⏳ Setup CI/CD pipeline

---

## References

- Detailní Java analýza: `analýza_20251127/JAVA14_TO_JAVA17_DETAILED_CODE_ANALYSIS.md`
- Windows→Linux analýza: `analýza_20251127/WINDOWS_TO_LINUX_MIGRATION_ANALYSIS.md`
- Docker compose: `docker-compose.yml`
- Dockerfile: `KIS_App_64bit_JAVA17_Linux/Dockerfile`
