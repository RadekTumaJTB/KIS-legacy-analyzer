# KIS Banking - Linux Deployment Checklist
## Windows → Linux Migration Validation

**Version:** 1.0
**Date:** 2025-12-05
**Migration Phase:** Core Components Complete

---

## Pre-Deployment Checklist

### 1. Directory Structure Setup

#### Create Base Directories
```bash
# Base installation directory
sudo mkdir -p /opt/kis-banking

# Main application directory
sudo mkdir -p /opt/kis-banking/Konsolidace_JT

# Data directories
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/data
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/csv
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/export
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/protokoly
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/docfiles
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/evifiles
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/archiv

# Template directories
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/sablony
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/sablony/cartesis

# Working directories (.TEMP)
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/.TEMP/adminNaklady
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/.TEMP/bilanceDetail
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/.TEMP/budPres
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/.TEMP/budSchval
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/.TEMP/dokladyDetail
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/.TEMP/eviOr
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/.TEMP/chybyMustku
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/.TEMP/kap
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/.TEMP/konsSpravce
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/.TEMP/mpop
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/.TEMP/mustky
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/.TEMP/pbKlientiA
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/.TEMP/projektKarta
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/.TEMP/projektySLDev
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/.TEMP/projektyTran
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/.TEMP/protiGroup
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/.TEMP/slOutput
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/.TEMP/slPostup
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/.TEMP/spolPredav
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/.TEMP/unifUcty
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/.TEMP/zamekGen
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/.TEMP/zmenyProtistran
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/.TEMP/podnikatelUcty
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/.TEMP/zamekProtokol
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/.TEMP/zmenaMU

# Data directories (.DATA)
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/.DATA/budget
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/.DATA/budgetMustekNaklad
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/.DATA/budgetProjekt
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/.DATA/emise
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/.DATA/ifrszmeny
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/.DATA/kamil
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/.DATA/konsolidace
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/.DATA/konszmeny
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/.DATA/majetek
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/.DATA/odbory
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/.DATA/projektDoklad
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/.DATA/projekty
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/.DATA/projektyCF
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/.DATA/projektyUvery
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/.DATA/protiOsoby
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/.DATA/spolecnosti
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/.DATA/CARTESIS
sudo mkdir -p /opt/kis-banking/Konsolidace_JT/.DATA/muProtokol

# Backup directories
sudo mkdir -p /var/backup/kis-banking/daily
sudo mkdir -p /var/backup/kis-banking/monthly
```

#### Set Permissions
```bash
# Create application user (if not exists)
sudo useradd -r -s /bin/bash -d /opt/kis-banking kisapp

# Set ownership
sudo chown -R kisapp:kisapp /opt/kis-banking
sudo chown -R kisapp:kisapp /var/backup/kis-banking

# Set permissions
sudo chmod -R 755 /opt/kis-banking
sudo chmod -R 750 /opt/kis-banking/Konsolidace_JT/.TEMP
sudo chmod -R 750 /opt/kis-banking/Konsolidace_JT/.DATA
sudo chmod -R 770 /var/backup/kis-banking

# Verify
ls -la /opt/kis-banking/
ls -la /opt/kis-banking/Konsolidace_JT/
```

**Checklist:**
- [ ] All directories created
- [ ] Application user `kisapp` created
- [ ] Ownership set correctly
- [ ] Permissions set correctly
- [ ] Directory structure verified

---

### 2. Template Files Migration

#### Copy Template Files from Windows
```bash
# On Windows server:
# Source: D:\Konsolidace_JT\sablony\

# Create archive of templates
cd D:\Konsolidace_JT
tar -czf sablony.tar.gz sablony\

# Transfer to Linux (using scp, sftp, or other method)
scp sablony.tar.gz kisapp@linux-server:/tmp/

# On Linux server:
cd /opt/kis-banking/Konsolidace_JT/
sudo -u kisapp tar -xzf /tmp/sablony.tar.gz
rm /tmp/sablony.tar.gz
```

#### Verify Template Files
```bash
# Check templates exist
ls -la /opt/kis-banking/Konsolidace_JT/sablony/
ls -la /opt/kis-banking/Konsolidace_JT/sablony/cartesis/

# Expected files (example - verify actual list):
# - Empty.xls
# - SablonaBilance2007.xls
# - SablonaBilance2010*.xls
# - SablonaBilance2011*.xls
# - etc.

# Verify file encoding (should be readable by Java)
file /opt/kis-banking/Konsolidace_JT/sablony/*.xls

# Test file permissions
sudo -u kisapp cat /opt/kis-banking/Konsolidace_JT/sablony/Empty.xls > /dev/null && echo "OK" || echo "FAIL"
```

**Checklist:**
- [ ] Templates copied from Windows
- [ ] Files extracted to correct location
- [ ] File permissions correct (644)
- [ ] Files readable by kisapp user
- [ ] UTF-8 encoding preserved
- [ ] All required templates present

---

### 3. Oracle Configuration

#### JAZN XML Path
```bash
# Verify Oracle installation path
ls -la /opt/oracle/j2ee10/j2ee/OC4J_app/application-deployments/kis/

# Check if jazn-data.xml exists
ls -la /opt/oracle/j2ee10/j2ee/OC4J_app/application-deployments/kis/jazn-data.xml

# If path is different, update configuration:
# Option 1: Update application-paths.properties
# kis.paths.jazn.xml=/actual/path/to/jazn-data.xml

# Option 2: Use system property at runtime
# -Dkis.paths.jazn.xml=/actual/path/to/jazn-data.xml
```

**Checklist:**
- [ ] Oracle installation located
- [ ] jazn-data.xml file found
- [ ] Path configured in application-paths.properties
- [ ] File readable by application user

---

### 4. Application Configuration

#### Verify application-paths.properties
```bash
# Check if properties file is in classpath
jar tf /path/to/kis-banking.war | grep application-paths.properties

# Expected output:
# WEB-INF/classes/application-paths.properties
```

#### Create Environment-Specific Override (Optional)
```bash
# Create production properties file
sudo -u kisapp cat > /opt/kis-banking/config/application-paths-prod.properties << 'EOF'
# Production Linux Configuration Override
kis.paths.base=/opt/kis-banking
kis.paths.jazn.xml=/opt/oracle/j2ee10/j2ee/OC4J_app/application-deployments/kis/jazn-data.xml

# Backup to separate partition
kis.paths.backup.base=/mnt/backup/kis-banking
EOF

# Use in startup script:
# -Dspring.config.location=/opt/kis-banking/config/application-paths-prod.properties
```

**Checklist:**
- [ ] application-paths.properties in WAR/JAR
- [ ] Production overrides created (if needed)
- [ ] Configuration validated

---

### 5. LibreOffice Installation (Optional)

Only needed if Excel auto-open is required (not typical for server deployments).

#### Install LibreOffice
```bash
# Debian/Ubuntu
sudo apt-get update
sudo apt-get install -y libreoffice-calc

# RHEL/CentOS
sudo yum install -y libreoffice-calc

# Verify installation
libreoffice --version
```

#### Configure Excel Path Override
```bash
# Add to startup script:
# -Dkis.excel.path="libreoffice --calc"
```

**Note:** For headless servers, Excel auto-open is automatically disabled.

**Checklist:**
- [ ] LibreOffice installed (if needed)
- [ ] Version verified
- [ ] Path configured (if needed)
- [ ] Or confirmed headless mode will skip auto-open

---

### 6. Java Environment

#### Verify Java 17
```bash
java -version
# Expected: openjdk version "17.0.x" or similar

# Verify JAVA_HOME
echo $JAVA_HOME
# Expected: /usr/lib/jvm/java-17-openjdk or similar
```

#### Check JVM Settings
```bash
# Recommended JVM settings for KIS Banking:
# -Xms2g -Xmx4g
# -XX:+UseG1GC
# -Dfile.encoding=UTF-8
# -Dkis.paths.base=/opt/kis-banking
```

**Checklist:**
- [ ] Java 17 installed
- [ ] JAVA_HOME set correctly
- [ ] UTF-8 encoding configured
- [ ] Memory settings appropriate

---

## Deployment Testing

### Test 1: PathConstants Initialization

```bash
# Run diagnostic
java -cp kis-banking.jar cz.jtbank.konsolidace.common.PathConstants

# Or add to startup logging
```

**Expected Output:**
```
=== KIS Path Configuration Diagnostics ===
OS: Linux
File Separator: /
Base Path: /opt/kis-banking
Root Path: /opt/kis-banking/Konsolidace_JT
Export Path: /opt/kis-banking/Konsolidace_JT/export
Templates Path: /opt/kis-banking/Konsolidace_JT/sablony
JAZN XML: /opt/oracle/j2ee10/j2ee/OC4J_app/application-deployments/kis/jazn-data.xml
Properties loaded: 100+ entries
==========================================
```

**Checklist:**
- [ ] PathConstants loads successfully
- [ ] All paths resolve correctly
- [ ] No exceptions thrown
- [ ] Properties count correct

---

### Test 2: Directory Access

```bash
# Test write access to TEMP directory
sudo -u kisapp touch /opt/kis-banking/Konsolidace_JT/.TEMP/test.txt
sudo -u kisapp rm /opt/kis-banking/Konsolidace_JT/.TEMP/test.txt

# Test write access to DATA directory
sudo -u kisapp touch /opt/kis-banking/Konsolidace_JT/.DATA/test.txt
sudo -u kisapp rm /opt/kis-banking/Konsolidace_JT/.DATA/test.txt

# Test write access to export directory
sudo -u kisapp touch /opt/kis-banking/Konsolidace_JT/export/test.xls
sudo -u kisapp rm /opt/kis-banking/Konsolidace_JT/export/test.xls
```

**Checklist:**
- [ ] TEMP directory writable
- [ ] DATA directory writable
- [ ] Export directory writable
- [ ] No permission errors

---

### Test 3: Template Loading

Create test class or use existing export module:

```java
// Test template loading
String templatePath = PathConstants.getSablonyPath();
File templateFile = new File(templatePath, "Empty.xls");
System.out.println("Template exists: " + templateFile.exists());
System.out.println("Template readable: " + templateFile.canRead());
System.out.println("Template path: " + templateFile.getAbsolutePath());
```

**Expected:**
```
Template exists: true
Template readable: true
Template path: /opt/kis-banking/Konsolidace_JT/sablony/Empty.xls
```

**Checklist:**
- [ ] Templates load successfully
- [ ] No FileNotFoundException
- [ ] Correct paths logged
- [ ] Files readable

---

### Test 4: Export Functionality

#### Test ESExportProjektTransakceAll
```bash
# Run export test (adapt to your testing framework)
# This should create a file in the export directory

# After export, verify:
ls -la /opt/kis-banking/Konsolidace_JT/.DATA/projekty/

# Expected: ProjektTran_YYYY-MM-DD.xls file created
```

**Checklist:**
- [ ] Export completes without errors
- [ ] Output file created in correct location
- [ ] File has correct permissions (644)
- [ ] File is valid Excel format
- [ ] No Excel auto-open attempted (headless)
- [ ] Logs show correct paths

---

### Test 5: Platform Detection

```java
// Verify platform detection
System.out.println("Is Windows: " + PathConstants.isWindows());
System.out.println("Is Linux: " + PathConstants.isLinux());
System.out.println("OS Name: " + System.getProperty("os.name"));
System.out.println("File Separator: " + PathConstants.getFileSeparator());
```

**Expected on Linux:**
```
Is Windows: false
Is Linux: true
OS Name: Linux
File Separator: /
```

**Checklist:**
- [ ] Platform detected as Linux
- [ ] File separator is /
- [ ] No Windows-specific code executed

---

### Test 6: Constants Migration Verification

```java
// Verify Constants.java uses PathConstants
System.out.println("ROOT_FILES_PATH: " + Constants.ROOT_FILES_PATH);
System.out.println("XLS_FILES_PATH: " + Constants.XLS_FILES_PATH);
System.out.println("EXPORT_FILES_PATH: " + Constants.EXPORT_FILES_PATH);
System.out.println("SABLONY_FILES_PATH: " + Constants.SABLONY_FILES_PATH);
System.out.println("JAZN_XML_FILE: " + Constants.JAZN_XML_FILE);
```

**Expected:**
```
ROOT_FILES_PATH: /opt/kis-banking/Konsolidace_JT/
XLS_FILES_PATH: /opt/kis-banking/Konsolidace_JT/data/
EXPORT_FILES_PATH: /opt/kis-banking/Konsolidace_JT/export/
SABLONY_FILES_PATH: /opt/kis-banking/Konsolidace_JT/sablony/
JAZN_XML_FILE: /opt/oracle/j2ee10/j2ee/OC4J_app/application-deployments/kis/jazn-data.xml
```

**Checklist:**
- [ ] All paths use forward slashes
- [ ] No D:\ or C:\ in output
- [ ] Paths end with / separator
- [ ] JAZN XML path correct

---

### Test 7: File Operations

```bash
# Test file creation and deletion
# Run existing export that uses deleteAllOriginalFiles()

# Monitor logs for:
# - Correct file separator used
# - Files found and deleted
# - No path-related exceptions
```

**Checklist:**
- [ ] File listing works
- [ ] File deletion works
- [ ] lastIndexOf(File.separator) works correctly
- [ ] No backslash-related errors

---

## Production Deployment

### Deployment Steps

1. **Backup Current Production (Windows)**
   ```bash
   # On Windows server
   xcopy /E /I D:\Konsolidace_JT D:\Konsolidace_JT_backup_%DATE%
   ```

2. **Deploy Application**
   ```bash
   # Copy WAR/JAR to Linux server
   scp kis-banking.war kisapp@linux-server:/opt/kis-banking/

   # Deploy to application server (Tomcat/OC4J/etc.)
   # (Steps vary by application server)
   ```

3. **Start Application**
   ```bash
   # Example for Tomcat
   sudo systemctl start tomcat

   # Example for OC4J
   sudo -u kisapp /opt/oracle/j2ee10/bin/oc4j -start
   ```

4. **Monitor Logs**
   ```bash
   # Watch application logs
   tail -f /var/log/kis-banking/application.log

   # Look for:
   # - "Loaded path configuration from application-paths.properties"
   # - "Using platform-independent base path: /opt/kis-banking/..."
   # - No FileNotFoundException
   # - No permission errors
   ```

**Checklist:**
- [ ] Application deployed
- [ ] Application starts successfully
- [ ] No path-related errors in logs
- [ ] PathConstants initialization logged
- [ ] All paths resolved correctly

---

### Post-Deployment Validation

#### Run Smoke Tests
- [ ] Login functionality works
- [ ] Basic navigation works
- [ ] Export module accessible
- [ ] No JavaScript console errors

#### Run Export Tests
- [ ] ESExportProjektTransakceAll works
- [ ] File created in correct location
- [ ] File downloadable via web interface
- [ ] No errors in logs

#### Verify Logs
```bash
# Check for migration markers
grep "MIGRATED:" /var/log/kis-banking/application.log

# Check for path diagnostics
grep "KIS Path Configuration" /var/log/kis-banking/application.log

# Check for errors
grep -i "error\|exception" /var/log/kis-banking/application.log | grep -i "path\|file"
```

**Checklist:**
- [ ] No path-related errors
- [ ] Migration markers visible in code execution
- [ ] PathConstants diagnostics logged
- [ ] Export operations successful

---

## Rollback Plan

### If Issues Occur

1. **Immediate Rollback to Windows**
   - Revert to backup Windows deployment
   - Investigate issues offline

2. **Common Issues and Fixes**

   **Issue: Templates not found**
   ```bash
   # Verify template path
   ls -la /opt/kis-banking/Konsolidace_JT/sablony/

   # Fix permissions if needed
   sudo chown -R kisapp:kisapp /opt/kis-banking/Konsolidace_JT/sablony/
   sudo chmod -R 644 /opt/kis-banking/Konsolidace_JT/sablony/*
   ```

   **Issue: Permission denied**
   ```bash
   # Fix directory permissions
   sudo chown -R kisapp:kisapp /opt/kis-banking/
   sudo chmod -R 755 /opt/kis-banking/Konsolidace_JT/
   ```

   **Issue: JAZN XML not found**
   ```bash
   # Update path in properties or system property
   # -Dkis.paths.jazn.xml=/correct/path/to/jazn-data.xml
   ```

   **Issue: Export files in wrong location**
   ```bash
   # Check PathConstants output
   # Verify OUT_DIR in AbsExcelDoklad
   # Check for hardcoded paths in logs
   ```

---

## Maintenance

### Log Rotation
```bash
# Add to /etc/logrotate.d/kis-banking
/var/log/kis-banking/*.log {
    daily
    rotate 30
    compress
    delaycompress
    missingok
    notifempty
    create 644 kisapp kisapp
}
```

### Backup Schedule
```bash
# Add to crontab
# Daily backup of export/data directories
0 2 * * * /opt/kis-banking/scripts/backup-daily.sh

# Monthly backup to archive
0 3 1 * * /opt/kis-banking/scripts/backup-monthly.sh
```

### Monitoring
```bash
# Disk space monitoring
df -h /opt/kis-banking
df -h /var/backup/kis-banking

# Directory size monitoring
du -sh /opt/kis-banking/Konsolidace_JT/.TEMP/*
du -sh /opt/kis-banking/Konsolidace_JT/.DATA/*
```

---

## Success Criteria

### Deployment is successful when:

✅ All directories created and accessible
✅ Template files migrated and readable
✅ PathConstants loads without errors
✅ Platform detected correctly as Linux
✅ Export operations complete successfully
✅ Files created in correct Linux paths
✅ No hardcoded Windows paths in logs
✅ No FileNotFoundException errors
✅ No permission errors
✅ Application performs as expected
✅ No Excel auto-open attempts on headless server

---

## Support Contacts

**Migration Team:** KIS Migration Team
**Date:** 2025-12-05
**Version:** 1.0

**For Issues:**
- Check logs first: `/var/log/kis-banking/`
- Review PathConstants diagnostics
- Verify directory permissions
- Check template file locations

---

**END OF CHECKLIST**
