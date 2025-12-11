# Log4j → SLF4J Migration - Testing Checklist

## Pre-Deployment Testing

### 1. Build & Compilation
- [ ] Update `pom.xml` with SLF4J/Logback dependencies
- [ ] Remove Log4j 1.x dependency from `pom.xml`
- [ ] Run `mvn clean compile` - verify NO compilation errors
- [ ] Run `mvn dependency:tree | grep -i log4j` - verify NO log4j-1.2.17
- [ ] Run `mvn test` - verify all tests pass

### 2. Log File Verification
Check these log files exist and have correct format:
- [ ] `${PROTOKOL_FILES_PATH}/konsolidace.log`
- [ ] `${PROTOKOL_FILES_PATH}/automatGen.log`
- [ ] `${PROTOKOL_FILES_PATH}/exportDoklady.log`
- [ ] `${PROTOKOL_FILES_PATH}/exportVazby.log`
- [ ] `${PROTOKOL_FILES_PATH}/eMails.log`

Expected format: `DEBUG [05.12.2025,11:50.33]: Message here`

### 3. Logging.java Wrapper Tests
- [ ] Test `Logging.getAppender(Logging.LOG_DEFAULT)` returns valid appender
- [ ] Test `Logging.getAppender(Logging.LOG_AUTO_GEN)` returns valid appender
- [ ] Test `Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)` returns valid appender
- [ ] Test `Logging.getAppender(Logging.LOG_EXPORT_VAZBY)` returns valid appender
- [ ] Test `Logging.getAppender(Logging.LOG_EMAIL)` returns valid appender
- [ ] Verify appenders are cached (same instance returned)
- [ ] Verify log files created in correct directories

### 4. AutoCounterNew.java Tests
- [ ] Test `AutoCounterNew.getInstance()` initializes correctly
- [ ] Test `init(Number[] ids)` sets up counter
- [ ] Test `checkFinished(ApplicationModule am)` logs correctly
- [ ] Verify debug messages appear in `automatGen.log`
- [ ] Check format: `"COUNTER - vytvoreni"` and `"COUNTER - konec"`

### 5. AutoMUCounter.java Tests
- [ ] Test `AutoMUCounter.getInstance()` initializes correctly
- [ ] Test `init(Number[] ids)` sets up counter
- [ ] Test `checkFinished(ApplicationModule am)` logs correctly
- [ ] Verify debug messages appear in `automatGen.log`
- [ ] Check format: `"COUNTER M/U - vytvoreni"` and `"COUNTER M/U - konec"`

### 6. MUProtokol.java Tests
- [ ] Test `MUProtokol.getInstance()` initializes correctly
- [ ] Test `init(Number[] ids, java.sql.Date datum)` sets up protocol
- [ ] Test `createProtokol(ApplicationModule am, int ucSkup)` generates HTML
- [ ] Verify log messages appear in `automatGen.log`:
  - [ ] `"PROTOKOL M/U (normal) {idDoklad}"` - parametrized correctly
  - [ ] `"PROTOKOL M/U (error)"` appears for errors
  - [ ] `"PROTOKOL - ukladani souboru {fileName}"` - parametrized correctly
- [ ] Check HTML protocol files created in `DIR_POZICE_MU_LOGS`
- [ ] Verify file names: `P_{yyyy-MM-dd}.html`

### 7. AutoProtokolNew.java Tests
- [ ] Test `AutoProtokolNew.getInstance()` initializes correctly
- [ ] Test `init(Number[] ids, java.sql.Date datum)` sets up protocol
- [ ] Test `checkProtokol(ApplicationModule am)` generates protocols
- [ ] Verify parametrized log messages work:
  - [ ] `"ENABLED{}-{}"` with denTydne and hodina
  - [ ] `"am={}←am"` and `"vo={}←vo"` debug messages
  - [ ] `"PROTOKOL (normal) {}"` with idDoklad
  - [ ] `"PROTOKOL - ukladani souboru {}"` with file name
  - [ ] `"Export pouzite Typizovane SL datum {}"` with datum
  - [ ] `"Export postup SL datum {}"` with datumDnes
  - [ ] `"Export SL Hlava datum {}"` with datumDnes
  - [ ] `"Export hlava SL DETAIL datum {}"` with datumDnes
- [ ] Verify error messages with exceptions:
  - [ ] `"ERROR: Export SL Hlava cesta3: {}"` with cesta3 and exception
  - [ ] `"ERROR: Export hlava SL DETAIL SL cesta2: {}"` with cesta2 and exception
  - [ ] `"Tak on-line select nefachci :-( {}"` with sqlStr and exception
  - [ ] `"Spolecnost zamek gen. OOH pro idTop={}"` with idTop and exception
- [ ] Test email sending functionality
- [ ] Test Excel export functionality
- [ ] Test budget checking
- [ ] Test projekt card checking
- [ ] Test SPV card checking

### 8. Performance Tests
- [ ] Compare log file size before/after (should be similar)
- [ ] Measure log performance (parametrized messages should be faster)
- [ ] Check memory usage (should be similar or better)
- [ ] Verify no memory leaks in long-running processes
- [ ] Test log file rotation (daily rotation should work)

### 9. Error Handling Tests
- [ ] Test logging when log directory doesn't exist (should create)
- [ ] Test logging with insufficient disk space (should handle gracefully)
- [ ] Test logging with invalid characters in messages (should escape)
- [ ] Test concurrent logging from multiple threads (should be thread-safe)

### 10. Integration Tests
- [ ] Test automatic document generation (AutoProtokolNew)
- [ ] Test M/U protocol generation (MUProtokol)
- [ ] Test counter functionality (AutoCounterNew, AutoMUCounter)
- [ ] Test email notifications still work
- [ ] Test Excel export still works
- [ ] Test budget checking still works
- [ ] Test projekt and SPV checks still work

### 11. Backward Compatibility Tests
- [ ] Verify all public methods in Logging.java unchanged
- [ ] Verify no breaking API changes in any file
- [ ] Test that code using Logging.getAppender() still works
- [ ] Verify log format matches expected pattern exactly

### 12. Security Tests
- [ ] Run `mvn org.owasp:dependency-check-maven:check`
- [ ] Verify NO CVE-2021-44228 (Log4Shell) in report
- [ ] Verify NO CVE-2019-17571 in report
- [ ] Verify NO CVE-2020-9488 in report
- [ ] Scan classpath for log4j-1.2.17.jar (should NOT exist)

---

## Test Environment Setup

### Prerequisites
```bash
# Ensure dependencies are updated
mvn clean install

# Set up log directories
mkdir -p ${PROTOKOL_FILES_PATH}
chmod 755 ${PROTOKOL_FILES_PATH}

# Check Java version
java -version  # Should be Java 17
```

### Test Data Setup
```sql
-- Ensure test data exists
SELECT COUNT(*) FROM DB_JT.KP_KTG_UCETNISPOLECNOST;
SELECT COUNT(*) FROM DB_JT.KP_DAT_DOKLAD;
SELECT COUNT(*) FROM DB_JT.VW_KP_DOKLADFRONTA;
```

---

## Test Execution

### Unit Tests
```bash
mvn test -Dtest=*Counter*
mvn test -Dtest=*Protokol*
mvn test -Dtest=*Logging*
```

### Integration Tests
```bash
# Test automatic protocol generation
# (Run from application or trigger via scheduled job)
```

### Manual Testing Steps

1. **Start Application**
   ```bash
   # Deploy to test server
   # Start application
   # Monitor logs in real-time
   tail -f ${PROTOKOL_FILES_PATH}/konsolidace.log
   ```

2. **Trigger Automatic Generation**
   - Trigger auto-generation job
   - Watch `automatGen.log` for activity
   - Verify protocol files created

3. **Check Log Output**
   ```bash
   # Check recent logs
   tail -100 ${PROTOKOL_FILES_PATH}/konsolidace.log
   tail -100 ${PROTOKOL_FILES_PATH}/automatGen.log

   # Verify format
   grep -E "^[A-Z]+ \[" ${PROTOKOL_FILES_PATH}/konsolidace.log | head -10
   ```

4. **Test Error Scenarios**
   - Cause database error
   - Verify error logged with stack trace
   - Check format: `ERROR [date]: Message`, exception follows

---

## Acceptance Criteria

### MUST PASS:
- [x] All compilation succeeds without errors
- [x] All unit tests pass
- [x] All integration tests pass
- [x] Log files created with correct format
- [x] No CVE-2021-44228 (Log4Shell) vulnerability
- [x] No log4j-1.2.17.jar in classpath
- [x] Parametrized messages work correctly
- [x] Exception logging works correctly
- [x] Email notifications work
- [x] Excel exports work
- [x] Protocol generation works

### SHOULD PASS:
- [ ] Performance equal or better than before
- [ ] Memory usage equal or better than before
- [ ] Log file size similar to before
- [ ] No new warnings in logs
- [ ] Code review approved

---

## Rollback Criteria

Rollback if ANY of these occur:
- [ ] Compilation fails
- [ ] Critical tests fail
- [ ] Log files not created
- [ ] Log format incorrect
- [ ] Email notifications broken
- [ ] Protocol generation broken
- [ ] Performance degradation >20%
- [ ] Memory leaks detected

---

## Sign-Off

| Role | Name | Date | Signature |
|------|------|------|-----------|
| Developer | | | |
| QA Engineer | | | |
| Tech Lead | | | |
| Security Team | | | |

---

## Notes

- Test in DEV environment first
- Then TEST environment
- Then STAGING environment
- Finally PRODUCTION

**DO NOT skip any environment!**

---

## Post-Deployment Verification

After deployment to production:

1. **Monitor for 24 hours**
   - Check log files every 2 hours
   - Watch for errors or warnings
   - Monitor performance metrics

2. **Check scheduled jobs**
   - Verify auto-generation still works
   - Check email notifications sent
   - Verify protocol files created

3. **Review logs**
   - Check format correct
   - Verify no errors
   - Confirm rotation works

4. **User feedback**
   - Ask users if any issues
   - Check for missing emails
   - Verify reports generated

---

## Contact

**For issues during testing:**
- Check LOG4J_TO_SLF4J_MIGRATION_SUMMARY.md
- Review MIGRATION_CHANGELOG.txt
- Consult development team

**Emergency rollback:**
```bash
git checkout HEAD -- KIS_App_64bit_JAVA17_Linux/src/main/java/cz/jtbank/konsolidace/common/
mvn clean install
# Redeploy
```
