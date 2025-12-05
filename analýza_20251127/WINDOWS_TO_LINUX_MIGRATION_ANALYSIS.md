# Analýza Migrace Windows Server 2003 → Linux (ubi-minimal 10)
## KIS Banking Application - Platform Migration Assessment

**Datum:** 1. prosince 2025
**Verze:** 1.0
**Autor:** Senior Full-Stack Java Developer (AI-assisted Analysis)

---

## Executive Summary

### Současný Stav
- **Platforma:** Windows Server 2003 (32-bit legacy)
- **Java Runtime:** Java 1.7
- **OS Coupling:** Aplikace má střední až silnou vazbu na Windows
- **Kód:** 1,021 Java tříd, 264,740 code chunks analyzováno

### Target Platforma
- **Cílová Platforma:** Linux (Red Hat UBI-minimal 10)
- **Architecture:** 64-bit (migrace z 32-bit)
- **Java Runtime:** Java 17 LTS (doporučeno)

### Kritické Zjištění

| Kategorie | Výskyty | Soubory | Risk Level | Migrovatelnost |
|-----------|---------|---------|------------|----------------|
| **File Paths** | 430 | 84 | ⚠️ STŘEDNÍ | ✅ Automatizovatelné |
| **Encoding** | 6,525 | 644 | ⚠️ STŘEDNÍ | ✅ Automatizovatelné |
| **JNI/Native** | 30 | 2 | ❌ VYSOKÉ | ⚠️ Vyžaduje rekompilaci |
| **Process Management** | 15 | 1 | ✅ NÍZKÉ | ✅ Jednoduché opravy |
| **Line Endings** | 5 | 1 | ✅ NÍZKÉ | ✅ Automatické |

**Celkové Risk Assessment:** ⚠️ **STŘEDNÍ** → **Aplikace JE migrovatelná na Linux s cílenými úpravami**

---

## 1. Detailní Analýza Windows Závislostí

### 1.1 Hardcoded File Paths (430 výskytů, 84 souborů)

#### Problém
Aplikace obsahuje **430 hardcoded Windows cest** ve formátu `C:\path\to\file`.

#### Příklady ze Skutečného Kódu

```java
// ESExportProjektTransakceAll.java
String outputPath = "C:\\exports\\projekt_transakce_all.xlsx";

// ESExportDoklady2011_bck.java
File backupFile = new File("C:\\backup\\doklady_2011.csv");

// ESExportDoklad2007.java
String templatePath = "C:\\templates\\doklad_2007_template.xlsx";
```

#### Dopad na Linux
- ❌ Cesty jako `C:\` neexistují na Linuxu
- ❌ Backslash `\` jako separator nefunguje (Linux používá `/`)
- ❌ Case-sensitivity: Linux rozlišuje `file.txt` vs `File.txt`

#### Řešení

**Varianta A: Refactoring na Platform-Independent Paths**
```java
// PŘED (Windows-specific)
String path = "C:\\exports\\data.xlsx";

// PO (Platform-independent)
String path = Paths.get(System.getProperty("user.home"), "exports", "data.xlsx").toString();
// nebo
String path = "/opt/kis-banking/exports/data.xlsx";  // Linux absolute path
```

**Varianta B: Configuration-driven Paths**
```java
// application.properties
export.base.path=/opt/kis-banking/exports
template.base.path=/opt/kis-banking/templates
backup.base.path=/var/backup/kis-banking

// Java code
@Value("${export.base.path}")
private String exportBasePath;

String outputPath = Paths.get(exportBasePath, "projekt_transakce_all.xlsx").toString();
```

#### Migrace Strategy
1. **Fáze 1:** Identifikovat všechny 84 souborů s hardcoded cestami
2. **Fáze 2:** Vytvořit configuration properties pro base paths
3. **Fáze 3:** Refaktorovat na `Paths.get()` nebo `File.separator`
4. **Fáze 4:** Unit testy pro path construction
5. **Fáze 5:** Integration testy na Linux prostředí

**Effort Estimate:**
- **Manual:** 84 souborů × 30 min = 42 hodin
- **With AI (Claude/Copilot):** 84 souborů × 10 min = 14 hodin
- **Savings:** 67% effort reduction

---

### 1.2 Character Encoding (6,525 výskytů, 644 souborů)

#### Problém
Aplikace masivně používá **Windows-1250** encoding v JSP souborech a některých Java třídách.

#### Příklady ze Skutečného Kódu

```jsp
<%-- DokladyDelete.jsp --%>
<%@ page contentType="text/html; charset=windows-1250" %>

<%-- UcSkupPredavaniProcess.jsp --%>
<%@ page contentType="text/html; charset=windows-1250" %>

<%-- IfrsNewPravidloHistProcess.jsp --%>
<%@ page contentType="text/html; charset=windows-1250" %>
```

#### Dopad na Linux
- ❌ Windows-1250 není defaultní encoding na Linuxu (obvykle UTF-8)
- ❌ České znaky (ěščřžýáíé) se mohou zobrazovat nesprávně
- ⚠️ Database connections mohou mít encoding mismatch

#### Řešení

**Globální Migrace na UTF-8**

```jsp
<!-- PŘED (Windows-1250) -->
<%@ page contentType="text/html; charset=windows-1250" %>

<!-- PO (UTF-8) -->
<%@ page contentType="text/html; charset=UTF-8" %>
```

**Java Properties**
```properties
# PŘED
file.encoding=windows-1250
jdbc.encoding=windows-1250

# PO
file.encoding=UTF-8
jdbc.encoding=UTF-8
```

**Database Connection String**
```java
// Oracle example
String url = "jdbc:oracle:thin:@localhost:1521:KISDB?oracle.jdbc.defaultNChar=true&oracle.jdbc.charset=AL32UTF8";

// Předchozí Windows-1250 data mohou vyžadovat konverzi
```

#### Migrace Strategy

1. **Fáze 1: Analýza Database Encoding**
   ```sql
   -- Zkontrolovat Oracle NLS_CHARACTERSET
   SELECT * FROM NLS_DATABASE_PARAMETERS WHERE PARAMETER = 'NLS_CHARACTERSET';

   -- Pokud je WE8MSWIN1250, je potřeba konverze
   ```

2. **Fáze 2: Konverze JSP souborů (644 souborů)**
   - Automatický sed/awk script:
   ```bash
   find /path/to/jsp -name "*.jsp" -exec sed -i 's/windows-1250/UTF-8/g' {} \;
   ```

3. **Fáze 3: Testing**
   - Manuální visual test všech 644 JSP stránek
   - Zejména kontrola českých znaků: `ěščřžýáíéůú`

4. **Fáze 4: Database Migration (pokud potřeba)**
   - Dump→Convert→Import proces
   - Nebo Oracle CSALTER DATABASE CHARACTER SET

**Effort Estimate:**
- **Automatická konverze JSP:** 2 hodiny (script development)
- **Manual testing (644 pages):** 644 × 3 min = 32 hodin
- **Database migration (worst case):** 40-80 hodin
- **Total:** 74-114 hodin

---

### 1.3 JNI / Native Libraries (30 výskytů, 2 soubory)

#### Problém
CSV export funkce používají **Java Native Interface (JNI)** pro performance.

#### Identifikované Soubory
1. `CSVExportPackage.java` - 15 JNI volání
2. `CSVExportDoklad.java` - 15 JNI volání

#### Příklad Kódu (predikce na základě analýzy)

```java
// CSVExportPackage.java
public class CSVExportPackage {
    // Native method deklarace
    private native void exportToCSVNative(String data, String filePath);

    static {
        // Load Windows DLL
        System.loadLibrary("csvexport");  // csvexport.dll na Windows
    }

    public void exportData(List<Package> packages) {
        String csvData = formatData(packages);
        exportToCSVNative(csvData, "C:\\exports\\packages.csv");
    }
}
```

#### Dopad na Linux
- ❌ Windows .DLL knihovny NEfungují na Linuxu
- ❌ Vyžaduje Linux .SO ekvivalent knihovny
- ⚠️ 32-bit → 64-bit rekompilace nutná
- ⚠️ Pokud není source kód C/C++ k dispozici, VELKÝ PROBLÉM

#### Řešení

**Varianta A: Rekompilace pro Linux**
```bash
# Pokud máme source kód
gcc -shared -fPIC -I${JAVA_HOME}/include -I${JAVA_HOME}/include/linux \
    -o libcsvexport.so csvexport.c

# Java side
System.loadLibrary("csvexport");  // Načte libcsvexport.so na Linuxu
```

**Varianta B: Pure Java Replacement**
```java
// Nahradit JNI čistě Java knihovnou
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class CSVExportPackage {
    public void exportData(List<Package> packages) {
        try (CSVPrinter printer = new CSVPrinter(
            new FileWriter("/opt/kis-banking/exports/packages.csv"),
            CSVFormat.DEFAULT)) {

            for (Package pkg : packages) {
                printer.printRecord(pkg.getId(), pkg.getName(), pkg.getValue());
            }
        }
    }
}
```

**Varianta C: Investigate Why JNI Was Used**
- Pokud byl JNI použit jen pro performance → benchmark Pure Java
- Modern Java (17) má mnohem lepší performance než Java 7
- Apache Commons CSV je velmi rychlá

#### Critical Questions
1. **Máme source kód C/C++ pro JNI knihovnu?**
   - ANO → Rekompilace pro Linux
   - NE → Pure Java replacement (Apache Commons CSV)

2. **Kolik dat se exportuje?**
   - < 1M řádků → Pure Java je OK
   - > 1M řádků → Potřeba performance testing

3. **Jak často se volá export?**
   - Denně → Performance kritická
   - Měsíčně → Performance nekritická

#### Migrace Strategy

**Doporučení: Pure Java Replacement (Varianta B)**

Důvody:
- ✅ Platform-independent
- ✅ Žádná rekompilace potřeba
- ✅ Žádné DLL/SO hell
- ✅ Apache Commons CSV je battle-tested
- ✅ Jednodušší maintenance

**Effort Estimate:**
- **Analýza current JNI funkčnosti:** 8 hodin
- **Implementace Pure Java (Apache Commons CSV):** 16 hodin
- **Testing & Performance benchmarking:** 16 hodin
- **Total:** 40 hodin

---

### 1.4 Process Management (15 výskytů, 1 soubor)

#### Problém
Oracle Job scheduler volá `.CMD` batch soubory.

#### Identifikovaný Soubor
- `MY_JOB.sql` - Oracle PL/SQL scheduler s referencemi na `.CMD`

#### Příklad (predikce)

```sql
-- MY_JOB.sql
BEGIN
    DBMS_SCHEDULER.create_job (
        job_name        => 'EXPORT_DAILY_REPORT',
        job_type        => 'EXECUTABLE',
        job_action      => 'C:\kis-banking\scripts\export_report.CMD',  -- ❌ Windows
        start_date      => SYSTIMESTAMP,
        repeat_interval => 'FREQ=DAILY; BYHOUR=23',
        enabled         => TRUE
    );
END;
```

#### Dopad na Linux
- ❌ `.CMD` soubory jsou Windows batch files
- ❌ Path `C:\` neexistuje
- ❌ Batch syntax nefunguje na Linux (např. `SET`, `IF ERRORLEVEL`, `GOTO`)

#### Řešení

**Konverze na Bash Scripts**

```bash
#!/bin/bash
# export_report.sh

# PŘED (export_report.CMD)
@ECHO OFF
SET EXPORT_DIR=C:\exports
IF ERRORLEVEL 1 GOTO ERROR
java -jar export.jar %EXPORT_DIR%
GOTO END
:ERROR
ECHO Export failed
EXIT /B 1
:END

# PO (export_report.sh)
#!/bin/bash
set -e  # Exit on error

EXPORT_DIR=/opt/kis-banking/exports

java -jar /opt/kis-banking/lib/export.jar "$EXPORT_DIR" || {
    echo "Export failed" >&2
    exit 1
}

echo "Export completed successfully"
```

**Oracle Job Update**

```sql
-- MY_JOB.sql (Linux version)
BEGIN
    DBMS_SCHEDULER.create_job (
        job_name        => 'EXPORT_DAILY_REPORT',
        job_type        => 'EXECUTABLE',
        job_action      => '/opt/kis-banking/scripts/export_report.sh',  -- ✅ Linux
        start_date      => SYSTIMESTAMP,
        repeat_interval => 'FREQ=DAILY; BYHOUR=23',
        enabled         => TRUE
    );
END;
```

#### Migrace Strategy

1. **Fáze 1: Inventory všech .CMD souborů**
   ```bash
   find C:\kis-banking -name "*.cmd" -o -name "*.bat"
   ```

2. **Fáze 2: Manual Conversion**
   - 1 CMD file → 1 bash script
   - Estimate: 15 CMD × 2 hours = 30 hodin

3. **Fáze 3: Permission Setup**
   ```bash
   chmod +x /opt/kis-banking/scripts/*.sh
   ```

4. **Fáze 4: Update Oracle Jobs**
   - Replace všech C:\ paths → /opt/kis-banking/
   - Estimate: 2 hodiny

**Effort Estimate:** 32 hodin celkem

---

### 1.5 Line Endings (5 výskytů, 1 soubor)

#### Problém
`\r\n` (Windows CRLF) line endings v některých JSP souborech.

#### Řešení

**Automatická Konverze**
```bash
# Konverze všech JSP souborů na LF
find /path/to/jsp -name "*.jsp" -exec dos2unix {} \;
```

**Git Configuration (prevence)**
```gitattributes
*.jsp text eol=lf
*.java text eol=lf
*.sql text eol=lf
*.sh text eol=lf
```

**Effort Estimate:** 1 hodina

---

## 2. 32-bit → 64-bit Migration

### Potenciální Problémy

#### 2.1 JVM Memory Model
```java
// 32-bit: Max heap ~1.5-2GB
java -Xmx1536m -jar app.jar

// 64-bit: Může využít veškerou RAM
java -Xmx8g -jar app.jar  // Doporučeno pro Linux
```

#### 2.2 Native Pointers
- JNI pointer size se mění (32-bit → 64-bit)
- Pokud JNI kód používá pointer arithmetic, potřeba update

#### 2.3 Performance
- 64-bit JVM má o ~10-30% větší memory footprint (wider pointers)
- Ale lepší performance pro large datasets

**Effort Estimate:** 4 hodiny testování

---

## 3. Linux-Specific Considerations

### 3.1 File System Differences

| Aspect | Windows | Linux | Impact |
|--------|---------|-------|--------|
| **Path Separator** | `\` | `/` | ⚠️ HIGH |
| **Case Sensitivity** | NO | YES | ⚠️ HIGH |
| **Root** | `C:\`, `D:\` | `/` | ⚠️ HIGH |
| **Line Endings** | CRLF (`\r\n`) | LF (`\n`) | ✅ LOW |
| **Max Path Length** | 260 chars | 4096 chars | ✅ BENEFIT |
| **File Permissions** | ACLs | POSIX | ⚠️ MEDIUM |

### 3.2 Oracle Database Connectivity

**Windows:**
```properties
jdbc.url=jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=db-server)(PORT=1521))(CONNECT_DATA=(SERVICE_NAME=KISDB)))
```

**Linux:** (stejné, ale může vyžadovat tnsnames.ora konfiguraci)
```bash
# /opt/oracle/network/admin/tnsnames.ora
KISDB =
  (DESCRIPTION =
    (ADDRESS = (PROTOCOL = TCP)(HOST = db-server)(PORT = 1521))
    (CONNECT_DATA =
      (SERVER = DEDICATED)
      (SERVICE_NAME = KISDB)
    )
  )
```

### 3.3 Red Hat UBI-minimal 10 Specifics

**Java Installation:**
```bash
# Install OpenJDK 17
dnf install java-17-openjdk-devel

# Verify
java -version
# openjdk version "17.0.x"
```

**Required Libraries:**
```bash
# Oracle JDBC dependencies
dnf install glibc.i686  # 32-bit support if needed

# UTF-8 locale
localectl set-locale LANG=en_US.UTF-8
```

---

## 4. Migration Plan & Timeline

### Phase 1: Preparation & Analysis (2 týdny)

**Week 1:**
- ✅ Complete code analysis (DONE - this document)
- Inventory all Windows dependencies
- Setup Linux development environment (RHEL UBI-minimal 10)
- Install Java 17, Oracle client, development tools

**Week 2:**
- Create migration test plan
- Prepare development database (clone of production)
- Setup CI/CD for Linux builds

**Deliverables:**
- ✅ Migration analysis report (this document)
- Linux dev environment ready
- Test plan document

---

### Phase 2: Code Migration (6-8 týdnů)

#### Week 3-4: Path & Encoding Migration
| Task | Effort | Priority |
|------|--------|----------|
| Refactor 84 files with hardcoded paths | 14h | HIGH |
| Convert 644 JSP files to UTF-8 | 34h | HIGH |
| Update configuration files | 4h | HIGH |
| Unit tests for path handling | 8h | HIGH |
| **Subtotal** | **60h** | |

#### Week 5-6: JNI Replacement
| Task | Effort | Priority |
|------|--------|----------|
| Analyze current JNI functionality | 8h | CRITICAL |
| Implement Pure Java CSV export | 16h | CRITICAL |
| Performance benchmark testing | 16h | HIGH |
| Integration tests | 8h | HIGH |
| **Subtotal** | **48h** | |

#### Week 7: Process Management & Scripts
| Task | Effort | Priority |
|------|--------|----------|
| Convert CMD scripts to bash | 30h | MEDIUM |
| Update Oracle scheduler jobs | 2h | MEDIUM |
| Test script execution on Linux | 8h | MEDIUM |
| **Subtotal** | **40h** | |

#### Week 8: Testing & Fixes
| Task | Effort | Priority |
|------|--------|----------|
| Integration testing | 40h | CRITICAL |
| Bug fixes from testing | 40h | CRITICAL |
| Performance tuning | 20h | HIGH |
| **Subtotal** | **100h** | |

**Phase 2 Total:** 248 hodin (31 dev-days @ 8h/day)

---

### Phase 3: Testing & Validation (3 týdny)

**Week 9-10: Functional Testing**
- Complete end-to-end testing
- Database connectivity testing
- File I/O testing (exports, imports)
- Character encoding validation (Czech characters)

**Week 11: Performance & Load Testing**
- Baseline performance metrics
- Load testing (concurrent users)
- Memory profiling
- Database connection pool tuning

**Deliverables:**
- Test reports
- Performance benchmarks
- Bug fixes

---

### Phase 4: Deployment & Go-Live (2 týdny)

**Week 12: Staging Deployment**
- Deploy to staging environment (Linux)
- User Acceptance Testing (UAT)
- Documentation updates

**Week 13: Production Deployment**
- Production deployment planning
- Go-live execution
- Post-deployment monitoring
- Rollback plan ready

---

## 5. Effort & Cost Estimates

### Development Effort

| Phase | Effort (hours) | Dev-Days @ 8h | Duration (weeks) |
|-------|----------------|---------------|------------------|
| **Phase 1: Preparation** | 80 | 10 | 2 |
| **Phase 2: Code Migration** | 248 | 31 | 6-8 |
| **Phase 3: Testing** | 200 | 25 | 3 |
| **Phase 4: Deployment** | 80 | 10 | 2 |
| **Total** | **608** | **76** | **13-15** |

### Cost Estimate (Based on €800/day Senior Developer Rate)

| Scenario | Effort (days) | Cost (€) | Duration |
|----------|---------------|----------|----------|
| **Manual (No AI)** | 76 | €60,800 | 15 weeks |
| **With AI (Claude/Copilot)** | 50 | €40,000 | 10 weeks |
| **Savings** | 26 days | €20,800 | 5 weeks |

**ROI Note:** AI-assisted development provides:
- ✅ 34% effort reduction
- ✅ 34% cost savings
- ✅ 33% faster delivery

---

## 6. Risk Assessment & Mitigation

### High Risks

#### Risk 1: JNI Library Source Unavailable
**Impact:** CRITICAL
**Probability:** MEDIUM
**Mitigation:**
- ✅ Pure Java replacement (Apache Commons CSV)
- Fallback plan ready
- No vendor lock-in

#### Risk 2: Database Encoding Issues
**Impact:** HIGH
**Probability:** MEDIUM
**Mitigation:**
- Thorough encoding testing
- Backup/restore plan
- Character set validation scripts
- Test with production data clone

#### Risk 3: Performance Degradation
**Impact:** MEDIUM
**Probability:** LOW
**Mitigation:**
- Baseline performance metrics
- Load testing before go-live
- Performance tuning budget
- Rollback plan

### Medium Risks

#### Risk 4: Hidden Windows Dependencies
**Impact:** MEDIUM
**Probability:** LOW
**Mitigation:**
- Thorough code review
- Extended testing phase
- Gradual rollout approach

#### Risk 5: Third-Party Library Incompatibility
**Impact:** MEDIUM
**Probability:** LOW
**Mitigation:**
- Library audit
- Compatibility matrix
- Alternative library research

---

## 7. Recommendations

### Short-Term (Immediate)

1. **✅ Approve Migration** - Application IS migrable to Linux
2. **Setup Linux Dev Environment** - RHEL UBI-minimal 10 + Java 17
3. **Start with Phase 1** - 2-week preparation & analysis
4. **Assign Development Team** - 2 senior developers + 1 QA

### Medium-Term (3-6 months)

5. **Execute Phases 2-3** - Code migration & testing
6. **Parallel Run** - Windows + Linux side-by-side for 1 month
7. **Gradual Migration** - Start with non-critical services

### Long-Term (6-12 months)

8. **Complete Migration** - All services on Linux
9. **Decommission Windows** - Cost savings
10. **Modernization** - Consider containerization (Docker/Kubernetes)

---

## 8. Success Metrics

### Technical KPIs

| Metric | Target | Measurement |
|--------|--------|-------------|
| **Uptime** | 99.9% | Production monitoring |
| **Performance** | ≥ Windows baseline | Response time metrics |
| **Encoding Issues** | 0 critical bugs | Issue tracker |
| **Failed Jobs** | < 0.1% | Job scheduler logs |

### Business KPIs

| Metric | Target | Measurement |
|--------|--------|-------------|
| **Migration Duration** | 13-15 weeks | Project timeline |
| **Budget Adherence** | ±10% | Financial tracking |
| **User Satisfaction** | ≥ 95% | User surveys |
| **Zero Downtime** | Yes | Deployment logs |

---

## 9. Conclusion

### Migration Feasibility: ✅ **DOPORUČENO**

Aplikace KIS Banking **JE migrovatelná** z Windows Server 2003 na Linux (UBI-minimal 10) s následujícími klíčovými poznatky:

#### Pozitivní Faktory
- ✅ **Žádné Registry Dependencies** - Aplikace nepoužívá Windows Registry
- ✅ **Žádný Real COM/ActiveX** - False positive v analýze
- ✅ **Pure Java Architecture** - Většina kódu je platform-independent
- ✅ **Moderní Linux Support** - Red Hat UBI poskytuje enterprise support

#### Kritické Úkoly
- ⚠️ **430 Hardcoded Paths** - Vyžaduje refactoring (14 hodin s AI)
- ⚠️ **6,525 Encoding Issues** - UTF-8 konverze (34 hodin)
- ⚠️ **JNI Replacement** - Pure Java CSV library (48 hodin)

#### Business Value
- 💰 **Úspora nákladů:** Linux licensing vs Windows Server
- 🚀 **Performance:** 64-bit JVM + modern Linux kernel
- 🔒 **Security:** Red Hat UBI s enterprise security updates
- 🏗️ **Scalability:** Better container support (Docker/Kubernetes ready)

### Next Steps

1. **Management Approval** - Review this analysis
2. **Budget Allocation** - €40,000-€60,000 (depends on AI usage)
3. **Team Assignment** - 2 senior devs + 1 QA
4. **Kickoff** - Week of [INSERT DATE]

---

## Appendix A: Tools & Technologies

### Development Tools
- **IDE:** IntelliJ IDEA (Linux version)
- **Build:** Maven/Gradle
- **CI/CD:** Jenkins/GitLab CI on Linux
- **Version Control:** Git

### Testing Tools
- **Unit Testing:** JUnit 5
- **Integration Testing:** TestContainers (Linux-first)
- **Performance:** JMeter, VisualVM
- **Encoding Testing:** Custom validation scripts

### Linux Tools
- **OS:** Red Hat UBI-minimal 10
- **Java:** OpenJDK 17 LTS
- **Database Client:** Oracle Instant Client (Linux)
- **Monitoring:** Prometheus + Grafana

---

## Appendix B: Contact & Support

### Project Team
- **Project Manager:** [INSERT NAME]
- **Lead Developer:** [INSERT NAME]
- **DevOps Engineer:** [INSERT NAME]
- **QA Lead:** [INSERT NAME]

### Vendor Support
- **Red Hat Support:** enterprise.redhat.com
- **Oracle Support:** support.oracle.com
- **Java Support:** OpenJDK community

---

**Document Version:** 1.0
**Last Updated:** 1. prosince 2025
**Next Review:** Po Phase 1 completion

---

© 2025 KIS Banking Application - Windows → Linux Migration Analysis
