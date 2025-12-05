# KIS Migration Quick Reference Guide

**Pro vývojáře:** Rychlý průvodce migrací jednotlivých závislostí

---

## 📋 Index

1. [Log4j → SLF4J Migration](#log4j--slf4j-migration)
2. [Javax → Jakarta Migration](#javax--jakarta-migration)
3. [Apache POI HSSF → XSSF](#apache-poi-hssf--xssf)
4. [Oracle ADF → Spring Boot](#oracle-adf--spring-boot)
5. [Windows Paths → Cross-platform](#windows-paths--cross-platform)

---

## 1. Log4j → SLF4J Migration

### Step 1: Add Dependencies

```xml
<!-- pom.xml -->
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

<!-- Bridge for gradual migration -->
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>log4j-over-slf4j</artifactId>
    <version>2.0.9</version>
</dependency>
```

### Step 2: Replace Imports

**Before:**
```java
import org.apache.log4j.Logger;
import org.apache.log4j.*;

private static final Logger logger = Logger.getLogger(MyClass.class);
```

**After:**
```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

private static final Logger logger = LoggerFactory.getLogger(MyClass.class);
```

### Step 3: Update Logging Calls

**Before:**
```java
logger.debug("Debug message");
logger.info("Info message");
logger.warn("Warning message");
logger.error("Error message", exception);
```

**After:**
```java
// Same API - no changes needed!
logger.debug("Debug message");
logger.info("Info message");
logger.warn("Warning message");
logger.error("Error message", exception);
```

### Step 4: Replace Custom Logging Class

**Before (cz.jtbank.konsolidace.common.Logging.java):**
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

**After:**
```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Logging {
    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    public static Logger getLogger(String name) {
        return LoggerFactory.getLogger(name);
    }
}
```

### Step 5: Create logback.xml

```xml
<!-- src/main/resources/logback.xml -->
<configuration>
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${KIS_LOG_PATH}/konsolidace.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${KIS_LOG_PATH}/konsolidace.%d{yyyy-MM-dd}.log</fileNamePattern>
            <maxHistory>30</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>%-5level [%d{dd.MM.yyyy,HH:mm:ss}]: %msg%n</pattern>
        </encoder>
    </appender>

    <root level="INFO">
        <appender-ref ref="FILE" />
    </root>
</configuration>
```

### Find & Replace Commands

```bash
# Find all Log4j imports
grep -r "import org.apache.log4j" sources/JAVA/src/

# Replace imports (sed on macOS)
find sources/JAVA/src/ -name "*.java" -exec sed -i '' \
  's/import org.apache.log4j.Logger;/import org.slf4j.Logger;\nimport org.slf4j.LoggerFactory;/g' {} +

# Replace Logger.getLogger()
find sources/JAVA/src/ -name "*.java" -exec sed -i '' \
  's/Logger\.getLogger(\([^)]*\))/LoggerFactory.getLogger(\1)/g' {} +
```

**Effort:** 80-120 hours
**Risk:** Low (bridge available)
**Priority:** 🔥 CRITICAL - Security vulnerability

---

## 2. Javax → Jakarta Migration

### Step 1: Update Dependencies

```xml
<!-- REMOVE old javax dependencies -->
<!-- javax.mail -->
<!-- javax.servlet -->

<!-- ADD Jakarta dependencies -->
<dependency>
    <groupId>org.eclipse.angus</groupId>
    <artifactId>jakarta.mail</artifactId>
    <version>2.1.1</version>
</dependency>

<dependency>
    <groupId>jakarta.servlet</groupId>
    <artifactId>jakarta.servlet-api</artifactId>
    <version>6.0.0</version>
    <scope>provided</scope>
</dependency>
```

### Step 2: Replace Imports

#### Mail API:

**Before:**
```java
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

Session session = Session.getInstance(props);
Message message = new MimeMessage(session);
```

**After:**
```java
import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.activation.*;

Session session = Session.getInstance(props);
Message message = new MimeMessage(session);
```

#### Servlet API:

**Before:**
```java
import javax.servlet.http.*;

public class MyServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        // ...
    }
}
```

**After:**
```java
import jakarta.servlet.http.*;

public class MyServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        // ...
    }
}
```

### Find & Replace Commands

```bash
# Find all javax.mail imports
grep -r "import javax.mail" sources/JAVA/src/

# Replace javax.mail → jakarta.mail
find sources/JAVA/src/ -name "*.java" -exec sed -i '' \
  's/import javax\.mail\./import jakarta.mail./g' {} +

# Replace javax.activation → jakarta.activation
find sources/JAVA/src/ -name "*.java" -exec sed -i '' \
  's/import javax\.activation\./import jakarta.activation./g' {} +

# Replace javax.servlet → jakarta.servlet
find sources/JAVA/src/ -name "*.java" -exec sed -i '' \
  's/import javax\.servlet\./import jakarta.servlet./g' {} +
```

**Effort:** 40-60 hours
**Risk:** Low (straightforward package change)
**Priority:** ⚠️ High - Required for Java 17

---

## 3. Apache POI HSSF → XSSF

### Step 1: Update Dependencies

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

### Step 2: Replace Imports and Classes

**Before (HSSF - old .xls format):**
```java
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;

// Create workbook
HSSFWorkbook workbook = new HSSFWorkbook();
HSSFSheet sheet = workbook.createSheet("Sheet1");
HSSFRow row = sheet.createRow(0);
HSSFCell cell = row.createCell(0);
cell.setCellValue("Hello");

// Styling
HSSFCellStyle style = workbook.createCellStyle();
HSSFFont font = workbook.createFont();
font.setColor(HSSFColor.RED.index);

// Save
FileOutputStream out = new FileOutputStream("file.xls");
workbook.write(out);
```

**After (XSSF - new .xlsx format):**
```java
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;

// Create workbook
XSSFWorkbook workbook = new XSSFWorkbook();
XSSFSheet sheet = workbook.createSheet("Sheet1");
XSSFRow row = sheet.createRow(0);
XSSFCell cell = row.createCell(0);
cell.setCellValue("Hello");

// Styling
XSSFCellStyle style = workbook.createCellStyle();
XSSFFont font = workbook.createFont();
font.setColor(IndexedColors.RED.getIndex());

// Save
FileOutputStream out = new FileOutputStream("file.xlsx");
workbook.write(out);
```

### Migration Pattern

| HSSF (old) | XSSF (new) |
|-----------|-----------|
| HSSFWorkbook | XSSFWorkbook |
| HSSFSheet | XSSFSheet |
| HSSFRow | XSSFRow |
| HSSFCell | XSSFCell |
| HSSFCellStyle | XSSFCellStyle |
| HSSFFont | XSSFFont |
| HSSFColor | IndexedColors |
| .xls | .xlsx |

### Find & Replace Commands

```bash
# Find HSSF usage
grep -r "import org.apache.poi.hssf" sources/JAVA/src/

# Replace imports
find sources/JAVA/src/ -name "*.java" -exec sed -i '' \
  's/import org\.apache\.poi\.hssf\.usermodel\./import org.apache.poi.xssf.usermodel./g' {} +

# Replace class names (requires manual review!)
# HSSF → XSSF for each class
```

**Note:** Doporučuje se manuální review každého souboru, API je velmi podobné ale ne 100% identické.

**Effort:** 120-160 hours
**Risk:** Low-Medium (API very similar)
**Priority:** 📋 Medium - Functional but outdated

**Files affected:** ~18 files in `cz.jtbank.konsolidace.excel.*`

---

## 4. Oracle ADF → Spring Boot

### Overview

This is the **most complex** migration requiring 6-12 months.

### Mapping

| Oracle ADF | Spring Boot |
|-----------|-------------|
| EntityImpl | @Entity |
| ViewObjectImpl | @Repository + JPQL |
| ApplicationModuleImpl | @Service |
| DBTransaction | @Transactional |
| oracle.jbo.Key | @Id / @EmbeddedId |
| oracle.jbo.domain.Date | LocalDate |
| oracle.jbo.domain.Number | BigDecimal |

### Example: Entity Migration

**Before (Oracle ADF EntityImpl):**
```java
package cz.jtbank.konsolidace.doklady;

import oracle.jbo.server.EntityImpl;
import oracle.jbo.domain.Number;
import oracle.jbo.domain.Date;
import oracle.jbo.Key;

public class KpParametryImpl extends EntityImpl {
    public static final int ID = 0;
    public static final int HODNOTA = 1;
    public static final int DATUM = 2;

    public Number getId() {
        return (Number) getAttributeInternal(ID);
    }

    public void setId(Number value) {
        setAttributeInternal(ID, value);
    }

    public String getHodnota() {
        return (String) getAttributeInternal(HODNOTA);
    }

    public void setHodnota(String value) {
        setAttributeInternal(HODNOTA, value);
    }

    public Date getDatum() {
        return (Date) getAttributeInternal(DATUM);
    }

    public void setDatum(Date value) {
        setAttributeInternal(DATUM, value);
    }

    protected void doDML(int operation, TransactionEvent e) {
        // Custom logic before/after DML
        super.doDML(operation, e);
    }
}
```

**After (Spring Boot JPA Entity):**
```java
package cz.jtbank.konsolidace.doklady.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "KP_PARAMETRY")
public class KpParametry {

    @Id
    @Column(name = "ID")
    private BigDecimal id;

    @Column(name = "HODNOTA")
    private String hodnota;

    @Column(name = "DATUM")
    private LocalDate datum;

    // Standard getters/setters
    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getHodnota() {
        return hodnota;
    }

    public void setHodnota(String hodnota) {
        this.hodnota = hodnota;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    // JPA lifecycle hooks (if needed)
    @PrePersist
    @PreUpdate
    private void beforeSave() {
        // Custom logic before save
    }
}
```

### Example: Repository Migration

**Before (Oracle ADF ViewObjectImpl):**
```java
package cz.jtbank.konsolidace.doklady;

import oracle.jbo.server.ViewObjectImpl;
import oracle.jbo.Row;

public class KpParametryViewImpl extends ViewObjectImpl {

    public KpParametryViewImpl() {
    }

    public Row[] getAllParametry() {
        this.executeQuery();
        return this.getAllRowsInRange();
    }

    public Row findParametrById(Number id) {
        setWhereClause("ID = :1");
        defineNamedWhereClauseParam("1", id, null);
        executeQuery();
        return first();
    }
}
```

**After (Spring Data JPA Repository):**
```java
package cz.jtbank.konsolidace.doklady.repository;

import cz.jtbank.konsolidace.doklady.entity.KpParametry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface KpParametryRepository extends JpaRepository<KpParametry, BigDecimal> {

    // Spring Data generates implementation automatically!
    List<KpParametry> findAll();

    Optional<KpParametry> findById(BigDecimal id);

    // Custom queries
    @Query("SELECT p FROM KpParametry p WHERE p.hodnota = :hodnota")
    List<KpParametry> findByHodnota(String hodnota);
}
```

### Example: Service Migration

**Before (Oracle ADF ApplicationModuleImpl):**
```java
package cz.jtbank.konsolidace.doklady.common;

import oracle.jbo.server.ApplicationModuleImpl;
import oracle.jbo.server.DBTransaction;

public class DokladyModule extends ApplicationModuleImpl {

    public void saveParametr(Number id, String hodnota) {
        DBTransaction trans = getDBTransaction();
        try {
            KpParametryViewImpl view = getKpParametryView();
            Row row = view.createRow();
            row.setAttribute("Id", id);
            row.setAttribute("Hodnota", hodnota);
            trans.commit();
        } catch (Exception e) {
            trans.rollback();
            throw new KisException("Error saving parametr", e);
        }
    }
}
```

**After (Spring Boot Service):**
```java
package cz.jtbank.konsolidace.doklady.service;

import cz.jtbank.konsolidace.doklady.entity.KpParametry;
import cz.jtbank.konsolidace.doklady.repository.KpParametryRepository;
import cz.jtbank.konsolidace.common.KisException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class DokladyService {

    @Autowired
    private KpParametryRepository repository;

    @Transactional
    public KpParametry saveParametr(BigDecimal id, String hodnota) {
        try {
            KpParametry parametr = new KpParametry();
            parametr.setId(id);
            parametr.setHodnota(hodnota);
            return repository.save(parametr);
        } catch (Exception e) {
            throw new KisException("Error saving parametr", e);
        }
    }

    @Transactional(readOnly = true)
    public List<KpParametry> getAllParametry() {
        return repository.findAll();
    }
}
```

### Data Type Mapping

| Oracle ADF | Spring Boot | Notes |
|-----------|------------|-------|
| oracle.jbo.domain.Number | BigDecimal | Standard Java |
| oracle.jbo.domain.Date | LocalDate | java.time API |
| oracle.jbo.Key | @Id / @EmbeddedId | JPA primary key |
| oracle.jbo.RowID | String or Long | Depends on DB |

**Effort:** 3000-4500 hours
**Risk:** High - core application
**Priority:** 🔥 Critical - Long-term
**Strategy:** Strangler Fig Pattern (module by module)

---

## 5. Windows Paths → Cross-platform

### Problem

**Current (Constants.java):**
```java
private static String getDisk() {
    String ret = "D:\\";  // HARDCODED!
    return ret;
}

public static String ROOT_FILES_PATH = getDisk() + "Konsolidace_JT\\";
public static String XLS_FILES_PATH = ROOT_FILES_PATH + "data\\";
public static String CSV_FILES_PATH = ROOT_FILES_PATH + "csv\\";
```

### Solution 1: Spring Boot application.yml

**Create: src/main/resources/application.yml**
```yaml
kis:
  files:
    root: ${KIS_FILES_ROOT:/opt/kis/data}
    xls: ${kis.files.root}/data
    csv: ${kis.files.root}/csv
    export: ${kis.files.root}/export
    protokol: ${kis.files.root}/protokoly
    sablony: ${kis.files.root}/sablony
    doc: ${kis.files.root}/docfiles
    evi: ${kis.files.root}/evifiles
    archiv: ${kis.files.root}/archiv
```

**Refactor Constants.java:**
```java
package cz.jtbank.konsolidace.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "kis.files")
public class FilePathConfig {

    private String root;
    private String xls;
    private String csv;
    private String export;
    private String protokol;
    private String sablony;
    private String doc;
    private String evi;
    private String archiv;

    // Getters and setters
    public String getRoot() { return root; }
    public void setRoot(String root) { this.root = root; }

    public String getXls() { return xls; }
    public void setXls(String xls) { this.xls = xls; }

    // ... other getters/setters
}
```

**Usage:**
```java
@Service
public class MyService {

    @Autowired
    private FilePathConfig filePaths;

    public void exportToExcel() {
        String xlsPath = filePaths.getXls();
        File file = new File(xlsPath, "export.xlsx");
        // ...
    }
}
```

### Solution 2: Environment Variables

**Set environment variables:**
```bash
# Linux/Mac
export KIS_FILES_ROOT=/opt/kis/data

# Windows
set KIS_FILES_ROOT=D:\Konsolidace_JT
```

**Use in code:**
```java
public class Constants {
    public static final String ROOT_FILES_PATH =
        System.getenv().getOrDefault("KIS_FILES_ROOT", "/opt/kis/data");

    public static final String XLS_FILES_PATH =
        ROOT_FILES_PATH + File.separator + "data";

    public static final String CSV_FILES_PATH =
        ROOT_FILES_PATH + File.separator + "csv";
}
```

### Solution 3: Use Path instead of String

```java
import java.nio.file.Path;
import java.nio.file.Paths;

public class Constants {
    private static final Path ROOT_PATH =
        Paths.get(System.getenv().getOrDefault("KIS_FILES_ROOT", "/opt/kis/data"));

    public static final Path XLS_PATH = ROOT_PATH.resolve("data");
    public static final Path CSV_PATH = ROOT_PATH.resolve("csv");
    public static final Path EXPORT_PATH = ROOT_PATH.resolve("export");

    // Usage:
    // Path file = Constants.XLS_PATH.resolve("export.xlsx");
    // File javaFile = file.toFile();
}
```

**Effort:** 80-120 hours
**Risk:** Medium (many files reference Constants.java)
**Priority:** ⚠️ High - Linux deployment blocker

---

## 🛠️ Tools and Scripts

### Dependency Check

```bash
# Check for CVE vulnerabilities
mvn org.owasp:dependency-check-maven:check

# Update dependencies
mvn versions:display-dependency-updates
```

### Code Analysis

```bash
# Find all Oracle ADF imports
grep -r "import oracle.jbo" sources/JAVA/src/ | wc -l

# Find Windows-specific paths
grep -r "\\\\\\|D:\\\\" sources/JAVA/src/

# Find Log4j usage
grep -r "org.apache.log4j" sources/JAVA/src/
```

### Automated Refactoring (OpenRewrite)

Consider using OpenRewrite for automated migrations:

```xml
<plugin>
    <groupId>org.openrewrite.maven</groupId>
    <artifactId>rewrite-maven-plugin</artifactId>
    <version>5.3.0</version>
    <configuration>
        <activeRecipes>
            <recipe>org.openrewrite.java.migrate.JavaxMigrationToJakarta</recipe>
            <recipe>org.openrewrite.java.logging.slf4j.Log4jToSlf4j</recipe>
        </activeRecipes>
    </configuration>
</plugin>
```

Run:
```bash
mvn rewrite:run
```

---

## 📊 Migration Checklist

### Phase 1: Security & Platform (Quick Wins)

- [ ] Setup Maven/Gradle build system
- [ ] Add SLF4J + Logback dependencies
- [ ] Migrate Log4j imports to SLF4J
- [ ] Update Logging.java wrapper
- [ ] Create logback.xml configuration
- [ ] Test logging functionality
- [ ] Remove Log4j dependencies
- [ ] Run security scan (OWASP)
- [ ] Externalize Windows paths
- [ ] Create application.yml
- [ ] Migrate javax.mail → jakarta.mail
- [ ] Migrate javax.servlet → jakarta.servlet
- [ ] Test on Linux environment

### Phase 2: Library Modernization

- [ ] Upgrade Apache POI to 5.x
- [ ] Replace HSSF → XSSF imports
- [ ] Update HSSFWorkbook → XSSFWorkbook
- [ ] Change .xls → .xlsx file extensions
- [ ] Test Excel generation
- [ ] Update JDBC driver to ojdbc11
- [ ] Test database connectivity

### Phase 3: Framework Migration (Long-term)

- [ ] Design Spring Boot architecture
- [ ] Choose migration strategy (Strangler Fig)
- [ ] Create proof of concept
- [ ] Migrate first module (e.g., users)
- [ ] Setup parallel execution
- [ ] Migrate remaining modules one by one
- [ ] Deprecate Oracle ADF
- [ ] Final cutover

---

## 🆘 Common Issues

### Issue: "ClassNotFoundException: org.apache.log4j.Logger"

**Solution:** Added log4j-over-slf4j bridge:
```xml
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>log4j-over-slf4j</artifactId>
</dependency>
```

### Issue: "Package javax.mail does not exist"

**Solution:** Change to Jakarta Mail:
```xml
<dependency>
    <groupId>org.eclipse.angus</groupId>
    <artifactId>jakarta.mail</artifactId>
</dependency>
```

### Issue: "oracle.jbo.* not found"

**Solution:** This is expected during ADF → Spring migration. Requires full refactoring of affected classes.

---

## 📞 Support

**Documentation:**
- `/Users/radektuma/DEV/KIS/analýza_20251127/DEPENDENCY_ANALYSIS.md`
- `/Users/radektuma/DEV/KIS/analýza_20251127/DEPENDENCY_SUMMARY.txt`
- `/Users/radektuma/DEV/KIS/analýza_20251127/pom.xml.proposal`

**Generated:** 2025-12-05
**Version:** 1.0
