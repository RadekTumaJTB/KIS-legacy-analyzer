# KIS - Analýza závislostí a knihoven

**Datum analýzy:** 2025-12-05
**Analyzovaný kód:** 1,043 Java souborů
**Celkem unikátních importů:** 222

---

## 📊 Executive Summary

Aplikace KIS má **kritické závislosti** na zastaralých a proprietárních technologiích, které **blokují upgrade na Java 17** a modernizaci:

### Kritické nálezy:

1. **Oracle ADF JBO** - 3,146 importů (60% kódu)
   - Proprietární framework vyžadující Oracle licence
   - Nekompatibilní s moderními Java frameworky
   - **BLOKÁTOR** modernizace

2. **Apache Log4j 1.x** - 101 importů
   - End of Life, známé **bezpečnostní zranitelnosti** (Log4Shell)
   - **KRITICKÉ BEZPEČNOSTNÍ RIZIKO**

3. **Javax.* packages** - Migrace na Jakarta EE nutná pro Java 17
   - javax.mail → jakarta.mail
   - javax.servlet → jakarta.servlet

4. **Windows-specific kód** - Hardcoded cesty "D:\\"
   - Nekompatibilní s Linux deployment
   - Hostname-based konfigurace

---

## 📦 Kategorie importů

### 1. Oracle ADF Business Components (JBO)
**Počet importů:** 3,146 (nejvíce používaná závislost)

#### Top importy:
```
485 × oracle.jbo.domain.Number
423 × oracle.jbo.server.ViewObjectImpl
387 × oracle.jbo.server.AttributeDefImpl
373 × oracle.jbo.server.EntityImpl
373 × oracle.jbo.server.EntityDefImpl
343 × oracle.jbo.Key
266 × oracle.jbo.domain.Date
```

#### Charakteristika:
- **Framework:** Oracle Application Development Framework (ADF)
- **Účel:** Business logic layer, ORM, view objects
- **Licence:** Proprietární Oracle
- **Java 17:** ❌ Nekompatibilní
- **Status:** Aktivní, ale proprietární

#### Migrace:
```
Oracle JBO Entity/ViewObject → Spring Data JPA Entity/Repository
Oracle JBO Transaction → Spring @Transactional
Oracle JBO ApplicationModule → Spring @Service
```

---

### 2. Apache POI (Excel processing)
**Počet importů:** 211

#### Používané moduly:
```
72 × org.apache.poi.hssf.util.HSSFColor
45 × org.apache.poi.hssf.usermodel.HSSFCellStyle
39 × org.apache.poi.hssf.usermodel.HSSFFont
38 × org.apache.poi.hssf.usermodel.*
```

#### Charakteristika:
- **Framework:** Apache POI HSSF (old .xls format)
- **Aktuální verze:** Pravděpodobně 3.x (zastaralá)
- **Java 17:** ⚠️ Starší verze mohou mít problémy
- **Status:** Aktivní, ale používá zastaralý formát

#### Migrace:
```
HSSF (binary .xls) → XSSF (XML .xlsx)
Apache POI 3.x → Apache POI 5.2.5

HSSFWorkbook → XSSFWorkbook
HSSFSheet → XSSFSheet
HSSFCell → XSSFCell
```

**Effort:** 2-4 týdny
**Risk:** Nízké - přímočará migrace API

---

### 3. Apache Log4j 1.x
**Počet importů:** 101

#### Používané třídy:
```
98 × org.apache.log4j.*
 2 × org.apache.log4j.Logger
 1 × org.apache.log4j.PropertyConfigurator
```

#### Custom wrapper:
```java
// cz.jtbank.konsolidace.common.Logging
public class Logging {
    private static Layout lay = new PatternLayout("%-5p [%d{dd.MM.yyyy,HH:mm.ss}]: %m%n");
    private static HashMap appenders = new HashMap();

    public static Appender getAppender(int type) {
        // DailyRollingFileAppender setup
    }
}
```

#### Charakteristika:
- **Verze:** Log4j 1.x (End of Life)
- **Java 17:** ❌ Bezpečnostní zranitelnosti (CVE-2021-44228 Log4Shell)
- **Status:** End of Life od 2015
- **Kritičnost:** **KRITICKÁ - BEZPEČNOST**

#### Migrace:
```
Log4j 1.x → SLF4J + Logback
org.apache.log4j.Logger → org.slf4j.Logger
org.apache.log4j.PatternLayout → ch.qos.logback.classic.PatternLayout

// Bridge dependency pro postupnou migraci:
log4j-over-slf4j
```

**Effort:** 1-2 týdny
**Risk:** Nízké - existují bridges
**Priorita:** 🔥 **OKAMŽITĚ**

---

### 4. Javax.mail (JavaMail API)
**Počet importů:** 10

#### Používané třídy:
```
javax.mail.Transport
javax.mail.Session
javax.mail.Message
javax.mail.internet.MimeMessage
javax.mail.internet.InternetAddress
javax.activation.*
```

#### Charakteristika:
- **Verze:** JavaMail 1.x (javax.*)
- **Java 17:** ⚠️ Vyžaduje změnu na jakarta.*
- **Status:** Přesunuto do Jakarta EE
- **Kritičnost:** Střední

#### Migrace:
```
javax.mail.* → jakarta.mail.*
javax.activation.* → jakarta.activation.*

Maven dependency:
org.eclipse.angus:jakarta.mail:2.1.1
```

**Effort:** 1 týden
**Risk:** Nízké - jednoduchá změna packages
**Priorita:** ⚠️ Střední (nutné pro Java 17)

---

### 5. Javax.servlet
**Počet importů:** 2

#### Migrace:
```
javax.servlet.http.* → jakarta.servlet.http.*
javax.servlet.http.HttpServlet → jakarta.servlet.http.HttpServlet

Maven dependency:
jakarta.servlet:jakarta.servlet-api:6.0.0
```

**Effort:** 1 týden
**Risk:** Nízké
**Priorita:** ⚠️ Střední (nutné pro Java 17)

---

### 6. Standard Java libraries
**Počet importů:** 21

#### Nejpoužívanější:
```
109 × java.util.*
102 × java.io.*
 81 × java.text.*
 27 × java.sql.*
```

**Status:** ✅ Kompatibilní s Java 17
**Akce:** Žádná nutná

---

### 7. Custom knihovny (cz.jtbank.konsolidace.*)
**Počet importů:** 522 (141 unikátních balíčků)

#### Top moduly aplikace:

| Modul | Počet importů | Účel |
|-------|---------------|------|
| common | 260 | Společné utility, logging, konstanty |
| doklady | 41 | Správa dokumentů |
| projekt | 30 | Projektové moduly |
| dokument | 19 | Dokumentace |
| evi | 19 | Evidence |
| excel | 18 | Excel export/import |
| budget | 18 | Rozpočtování |
| protistrany | 16 | Protistrany |
| users | 15 | Uživatelské moduly |
| ifrs | 15 | IFRS reporting |

#### Struktura custom kódu:
```
cz.jtbank.konsolidace/
├── common/          # Logging, Utils, Constants, Exception handling
├── doklady/         # Document management
├── projekt/         # Project management
├── budget/          # Budget planning
├── protistrany/     # Counterparties
├── excel/           # Excel operations (POI wrappers)
├── mail/            # Email functionality
└── [15+ dalších modulů]
```

#### Kritické nálezy v custom kódu:

##### Constants.java - Windows-specific:
```java
public static final String ROOT_FILES_PATH = getDisk() + "Konsolidace_JT\\";
public static final String XLS_FILES_PATH = ROOT_FILES_PATH+"data\\";
public static final String CSV_FILES_PATH = ROOT_FILES_PATH+"csv\\";

private static String getDisk() {
    String ret = "D:\\";  // HARDCODED Windows disk!
    System.out.println("Pracuji s diskem "+ret);
    return ret;
}

// Hostname-based configuration:
public static final String[] KIS_ADMINS = ("tweek".equalsIgnoreCase(getHostName())) ?
    new String[] {"stastny@jtfg.com", "smrecek@jtbank.cz", "db_admin@jtbank.cz"} :
    new String[] {"stastny@jtfg.com", "smrecek@jtbank.cz"};
```

**Problémy:**
- ❌ Hardcoded Windows cesty
- ❌ Hostname-based logic
- ❌ Nekompatibilní s Linux
- ❌ Nekompatibilní s cloud deployment

**Řešení:**
```java
// Spring Boot application.yml
file:
  root: ${KIS_FILES_ROOT:/opt/kis/data}
  xls: ${file.root}/data
  csv: ${file.root}/csv

// Or environment variables:
ROOT_FILES_PATH = System.getenv("KIS_FILES_ROOT");
```

##### Logging.java - Log4j 1.x wrapper:
```java
import org.apache.log4j.*;

public class Logging {
    private static Layout lay = new PatternLayout("%-5p [%d{dd.MM.yyyy,HH:mm.ss}]: %m%n");

    public static Appender getAppender(int type) {
        DailyRollingFileAppender ret = new DailyRollingFileAppender(
            lay, file, "'.'yyyy-MM-dd");
        return ret;
    }
}
```

**Problémy:**
- ❌ Závislost na Log4j 1.x
- ❌ Custom appender management

**Řešení:** Migrace na SLF4J s Logback

---

## 🎯 Migrace plán

### Fáze 1: OKAMŽITÉ akce (Bezpečnost)
**Časový rámec:** 1-2 týdny

#### 1.1 Migrace Log4j → SLF4J + Logback

**Kroky:**
1. Přidat dependencies:
```xml
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>2.0.9</version>
</dependency>
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.4.14</version>
</dependency>
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>log4j-over-slf4j</artifactId>
    <version>2.0.9</version>
</dependency>
```

2. Refaktorovat Logging.java:
```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Logging {
    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }
}
```

3. Replace ve všech souborech:
```bash
# Find and replace
import org.apache.log4j.* → import org.slf4j.*
Logger.getLogger() → LoggerFactory.getLogger()
```

**Risk:** Nízké
**Benefit:** Odstranění kritického bezpečnostního rizika

---

### Fáze 2: Windows → Linux kompatibilita
**Časový rámec:** 1-2 týdny

#### 2.1 Externalizace konfigurace

**Akce:**
1. Vytvořit application.properties/yml
2. Nahradit hardcoded cesty za environment variables
3. Odstranit hostname-based logic

**Příklad refaktoringu:**
```java
// BEFORE (Constants.java)
public static String ROOT_FILES_PATH = "D:\\Konsolidace_JT\\";

// AFTER
@Value("${kis.files.root}")
private String rootFilesPath;

// application.yml
kis:
  files:
    root: /opt/kis/data
    xls: ${kis.files.root}/data
    csv: ${kis.files.root}/csv
```

**Risk:** Střední
**Benefit:** Linux kompatibilita, cloud-ready

---

### Fáze 3: Java EE → Jakarta EE
**Časový rámec:** 1 týden

#### 3.1 Javax.mail → Jakarta.mail

**Maven změny:**
```xml
<!-- REMOVE -->
<dependency>
    <groupId>javax.mail</groupId>
    <artifactId>mail</artifactId>
</dependency>

<!-- ADD -->
<dependency>
    <groupId>org.eclipse.angus</groupId>
    <artifactId>jakarta.mail</artifactId>
    <version>2.1.1</version>
</dependency>
```

**Code changes:**
```bash
# Find and replace
import javax.mail. → import jakarta.mail.
import javax.activation. → import jakarta.activation.
```

#### 3.2 Javax.servlet → Jakarta.servlet

**Maven změny:**
```xml
<dependency>
    <groupId>jakarta.servlet</groupId>
    <artifactId>jakarta.servlet-api</artifactId>
    <version>6.0.0</version>
    <scope>provided</scope>
</dependency>
```

**Code changes:**
```bash
import javax.servlet. → import jakarta.servlet.
```

**Risk:** Nízké
**Benefit:** Java 17 kompatibilita

---

### Fáze 4: Apache POI modernizace
**Časový rámec:** 2-4 týdny

#### 4.1 HSSF → XSSF migrace

**Maven upgrade:**
```xml
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi</artifactId>
    <version>5.2.5</version>
</dependency>
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>5.2.5</version>
</dependency>
```

**Code migration:**
```java
// BEFORE
import org.apache.poi.hssf.usermodel.*;
HSSFWorkbook wb = new HSSFWorkbook();
HSSFSheet sheet = wb.createSheet();

// AFTER
import org.apache.poi.xssf.usermodel.*;
XSSFWorkbook wb = new XSSFWorkbook();
XSSFSheet sheet = wb.createSheet();
```

**Files affected:** ~18 files v `cz.jtbank.konsolidace.excel.*`

**Risk:** Nízké - API je velmi podobné
**Benefit:** Moderní .xlsx formát, lepší výkon

---

### Fáze 5: Oracle ADF → Spring Boot
**Časový rámec:** 6-12 měsíců

#### 5.1 Strategie migrace

**Option A: Big Bang (nedoporučeno)**
- Přepsat celou aplikaci najednou
- Risk: Velmi vysoké
- Timeline: 12+ měsíců

**Option B: Strangler Fig Pattern (doporučeno)**
- Postupná migrace modul po modulu
- Původní ADF aplikace běží paralelně
- Nové moduly v Spring Boot
- Postupné nahrazování
- Risk: Střední
- Timeline: 18-24 měsíců

#### 5.2 Mapping Oracle ADF → Spring Boot

| Oracle ADF | Spring Boot Equivalent |
|-----------|----------------------|
| EntityImpl | @Entity + JPA |
| ViewObjectImpl | @Repository + JPA Query |
| ApplicationModuleImpl | @Service |
| DBTransaction | @Transactional |
| oracle.jbo.Key | @Id / @EmbeddedId |
| oracle.jbo.domain.Date | java.time.LocalDate |
| oracle.jbo.domain.Number | BigDecimal |
| ViewRowImpl | DTO / Entity |

#### 5.3 Ukázkový refactoring

**BEFORE (Oracle ADF):**
```java
import oracle.jbo.server.EntityImpl;
import oracle.jbo.domain.Number;
import oracle.jbo.domain.Date;

public class KpParametryImpl extends EntityImpl {
    public static final int ID = 0;
    public static final int HODNOTA = 1;

    public Number getId() {
        return (Number) getAttributeInternal(ID);
    }

    public void setId(Number value) {
        setAttributeInternal(ID, value);
    }
}
```

**AFTER (Spring Boot + JPA):**
```java
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "KP_PARAMETRY")
public class KpParametry {

    @Id
    @Column(name = "ID")
    private BigDecimal id;

    @Column(name = "HODNOTA")
    private String hodnota;

    // Getters, setters
    public BigDecimal getId() { return id; }
    public void setId(BigDecimal id) { this.id = id; }
}

@Repository
public interface KpParametryRepository extends JpaRepository<KpParametry, BigDecimal> {
}

@Service
public class KpParametryService {
    @Autowired
    private KpParametryRepository repository;

    @Transactional
    public KpParametry save(KpParametry entity) {
        return repository.save(entity);
    }
}
```

**Advantages:**
- Standard JPA annotations
- Spring Boot auto-configuration
- Better testability
- Modern development practices

**Risk:** Vysoké - jádro aplikace
**Benefit:** Odstranění proprietární závislosti, modernizace

---

## 📋 Maven POM návrh

### Kompletní pom.xml pro modernizovanou aplikaci:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>cz.jtbank</groupId>
    <artifactId>konsolidace-kis</artifactId>
    <version>2.0.0-SNAPSHOT</version>
    <packaging>jar</packaging>

    <name>KIS Konsolidace - Modernized</name>
    <description>Migration from Oracle ADF to Spring Boot</description>

    <properties>
        <java.version>17</java.version>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>

        <!-- Dependency Versions -->
        <spring-boot.version>3.2.1</spring-boot.version>
        <apache-poi.version>5.2.5</apache-poi.version>
        <logback.version>1.4.14</logback.version>
        <slf4j.version>2.0.9</slf4j.version>
        <jakarta-mail.version>2.1.1</jakarta-mail.version>
        <jakarta-servlet.version>6.0.0</jakarta-servlet.version>
        <oracle-jdbc.version>21.9.0.0</oracle-jdbc.version>
    </properties>

    <dependencies>
        <!-- ========================================== -->
        <!-- SPRING BOOT - Replacement for Oracle ADF -->
        <!-- ========================================== -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <version>${spring-boot.version}</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
            <version>${spring-boot.version}</version>
            <!-- Replaces Oracle JBO Entity/ViewObject -->
        </dependency>

        <!-- ========================================== -->
        <!-- LOGGING - Migration from Log4j 1.x       -->
        <!-- ========================================== -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>${slf4j.version}</version>
        </dependency>

        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>${logback.version}</version>
        </dependency>

        <!-- Bridge for migrating Log4j 1.x code -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>log4j-over-slf4j</artifactId>
            <version>${slf4j.version}</version>
        </dependency>

        <!-- ========================================== -->
        <!-- EXCEL - Apache POI Migration             -->
        <!-- ========================================== -->
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi</artifactId>
            <version>${apache-poi.version}</version>
        </dependency>

        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi-ooxml</artifactId>
            <version>${apache-poi.version}</version>
            <!-- For .xlsx support (XSSF) -->
        </dependency>

        <!-- ========================================== -->
        <!-- MAIL - Migration from javax.mail         -->
        <!-- ========================================== -->
        <dependency>
            <groupId>org.eclipse.angus</groupId>
            <artifactId>jakarta.mail</artifactId>
            <version>${jakarta-mail.version}</version>
        </dependency>

        <!-- ========================================== -->
        <!-- SERVLET - Migration from javax.servlet   -->
        <!-- ========================================== -->
        <dependency>
            <groupId>jakarta.servlet</groupId>
            <artifactId>jakarta.servlet-api</artifactId>
            <version>${jakarta-servlet.version}</version>
            <scope>provided</scope>
        </dependency>

        <!-- ========================================== -->
        <!-- DATABASE - Oracle JDBC                   -->
        <!-- ========================================== -->
        <dependency>
            <groupId>com.oracle.database.jdbc</groupId>
            <artifactId>ojdbc11</artifactId>
            <version>${oracle-jdbc.version}</version>
        </dependency>

        <!-- ========================================== -->
        <!-- TESTING                                  -->
        <!-- ========================================== -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <version>${spring-boot.version}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <version>${spring-boot.version}</version>
            </plugin>
        </plugins>
    </build>
</project>
```

---

## ⚠️ Problematické knihovny - Souhrn

### Kritická (OKAMŽITÁ akce)

| Knihovna | Importy | Problém | Migrace | Effort | Risk |
|----------|---------|---------|---------|--------|------|
| **Apache Log4j 1.x** | 101 | CVE-2021-44228 (Log4Shell) | SLF4J + Logback | 1-2 týdny | Nízké |
| **Oracle ADF JBO** | 3,146 | Proprietární, blokátor modernizace | Spring Boot + JPA | 6-12 měsíců | Vysoké |

### Vysoká priorita (Java 17)

| Knihovna | Importy | Problém | Migrace | Effort | Risk |
|----------|---------|---------|---------|--------|------|
| **javax.mail** | 10 | Změna na jakarta.* | Jakarta Mail 2.x | 1 týden | Nízké |
| **javax.servlet** | 2 | Změna na jakarta.* | Jakarta Servlet 6.x | 1 týden | Nízké |
| **Constants.java** | 260× | Windows paths | Externalizace config | 1-2 týdny | Střední |

### Střední priorita

| Knihovna | Importy | Problém | Migrace | Effort | Risk |
|----------|---------|---------|---------|--------|------|
| **Apache POI HSSF** | 211 | Zastaralý .xls formát | POI 5.x XSSF | 2-4 týdny | Nízké |

---

## 📈 Timeline a prioritizace

### Sprint 1-2 (Týdny 1-4): BEZPEČNOST
- ✅ Migrace Log4j → SLF4J + Logback
- ✅ Security audit a update dependencies

### Sprint 3-4 (Týdny 5-8): PLATFORM
- ✅ Windows → Linux kompatibilita
- ✅ Externalizace konfigurace
- ✅ javax.* → jakarta.* migrace

### Sprint 5-6 (Týdny 9-12): MODERNIZACE
- ✅ Apache POI HSSF → XSSF
- ✅ Setup Maven/Gradle build
- ✅ CI/CD pipeline

### Fáze 2 (Měsíce 4-12): FRAMEWORK
- ⚠️ Oracle ADF → Spring Boot migrace
- ⚠️ Strangler Fig pattern implementation
- ⚠️ Modul po modulu refactoring

---

## 🎯 Doporučení

### Okamžitá akce (do 1 měsíce):

1. **KRITICKÉ:** Migrace Log4j 1.x → SLF4J + Logback
   - Bezpečnostní riziko CVE-2021-44228
   - Nízké riziko migrace
   - Bridge dependency dostupný

2. **VYSOKÉ:** Windows → Linux kompatibilita
   - Externalizovat Constants.java
   - Použít application.properties/yml
   - Environment variables pro paths

3. **VYSOKÉ:** Javax → Jakarta migrace
   - Nutné pro Java 17
   - Jednoduchá změna importů
   - Nízké riziko

### Střednědobé (3-6 měsíců):

4. **Apache POI modernizace**
   - HSSF → XSSF
   - Upgrade na verzi 5.x
   - Moderní .xlsx formát

5. **Build system setup**
   - Maven/Gradle dependency management
   - Odstranění manuálních JAR files
   - Automatizace buildu

### Dlouhodobé (12-24 měsíců):

6. **Oracle ADF → Spring Boot**
   - Strangler Fig pattern
   - Modul po modulu
   - Paralelní běh systémů
   - Postupné nahrazování

---

## 📊 Metriky a KPI

### Současný stav:
- **Java verze:** 1.4 (2002)
- **Technologický dluh:** Kritický
- **Bezpečnostní rizika:** Vysoké (Log4Shell)
- **Vendor lock-in:** Oracle ADF proprietární
- **Cloud ready:** Ne (Windows paths)
- **Maintainability:** Nízká

### Cílový stav (po migraci):
- **Java verze:** 17 LTS (2021)
- **Technologický dluh:** Nízký
- **Bezpečnostní rizika:** Minimální
- **Vendor lock-in:** Žádný (Spring Boot)
- **Cloud ready:** Ano
- **Maintainability:** Vysoká

---

## 💰 Odhad nákladů

### Fáze 1-3 (Bezpečnost + Platform): 2-3 měsíce
- **Effort:** 300-450 člověkohodin
- **Tým:** 2 seniorní vývojáři
- **Risk:** Nízký až střední
- **ROI:** Okamžitý (bezpečnost, Java 17)

### Fáze 4-5 (Framework migrace): 12-18 měsíců
- **Effort:** 3000-4500 člověkohodin
- **Tým:** 3-4 vývojáři + architekt
- **Risk:** Vysoký
- **ROI:** Dlouhodobý (odstranění vendor lock-in)

---

## 📞 Kontakt a další kroky

**Vypracoval:** AI Code Analyzer
**Datum:** 2025-12-05

**Doporučené další kroky:**
1. Review této analýzy s technickým vedením
2. Prioritizace bezpečnostních rizik (Log4j)
3. Setup proof of concept pro Log4j migraci
4. Plánování kapacit pro Fáze 1-3
5. Strategické rozhodnutí ohledně Oracle ADF migrace

**Poznámka:** Tato analýza je založena na statické analýze importů. Pro kompletní obrázek doporučujeme:
- Dependency scanning (OWASP Dependency Check)
- Security audit
- Performance profiling
- Runtime analysis
