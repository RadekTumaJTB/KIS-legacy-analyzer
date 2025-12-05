# Problematic Cases Analysis
## Resource Management Patterns That Cannot Use Try-With-Resources

**Date:** 2025-12-05
**Project:** KIS Application - Resource Management Refactoring

---

## Overview

This document identifies patterns and cases where try-with-resources **cannot** or **should not** be applied, along with recommended approaches for each scenario.

---

## Category 1: Instance-Level Resource Fields

### Pattern Description
Resources stored as instance fields and managed across multiple method calls.

### Example: AbsCsvDoklad.java
```java
public abstract class AbsCsvDoklad {
    private FileOutputStream stream = null;

    public void createCsv() throws IOException {
        fFileOutput = new File(getFileAbsoluteName());
        stream = new FileOutputStream(fFileOutput);
    }

    public void appendCsv() throws IOException {
        stream = new FileOutputStream(fFileOutput, true);
    }

    public void csvOutput() throws IOException {
        boolean generovat = outputData();
        if (stream != null) stream.flush();
        // ... more operations ...
        stream.close();
    }

    protected void finalize() {
        try {
            if(stream != null) stream.close();
        } catch (Throwable t) {}
    }
}
```

### Why Try-With-Resources Doesn't Work
- Resource lifetime spans multiple methods
- Resource must remain open between method calls
- Try-with-resources scope is limited to single method/block

### Recommended Solutions

#### Option 1: Implement AutoCloseable Pattern (BEST)
```java
public abstract class AbsCsvDoklad implements AutoCloseable {
    private FileOutputStream stream = null;
    private boolean closed = false;

    public void createCsv() throws IOException {
        if (closed) throw new IllegalStateException("Already closed");
        fFileOutput = new File(getFileAbsoluteName());
        stream = new FileOutputStream(fFileOutput);
    }

    @Override
    public void close() throws IOException {
        if (closed) return;
        closed = true;
        if (stream != null) {
            try {
                stream.flush();
                stream.close();
            } finally {
                stream = null;
            }
        }
    }

    // Remove finalize() - it's deprecated and unreliable
}

// Usage:
try (AbsCsvDoklad csv = new MyCsvDoklad()) {
    csv.createCsv();
    csv.csvOutput();
    // automatically closed
}
```

#### Option 2: Builder Pattern with Method Chaining
```java
public class CsvBuilder implements AutoCloseable {
    private FileOutputStream stream;
    private File file;

    private CsvBuilder(File file) throws IOException {
        this.file = file;
        this.stream = new FileOutputStream(file);
    }

    public static CsvBuilder create(File file) throws IOException {
        return new CsvBuilder(file);
    }

    public CsvBuilder writeData() throws IOException {
        outputData();
        return this;
    }

    @Override
    public void close() throws IOException {
        if (stream != null) {
            stream.flush();
            stream.close();
        }
    }
}

// Usage:
try (CsvBuilder csv = CsvBuilder.create(file)) {
    csv.writeData();
}
```

#### Option 3: Current State - Improve Exception Handling (MINIMAL CHANGE)
```java
public void csvOutput() throws IOException {
    boolean generovat = false;
    try {
        generovat = outputData();
        if (stream != null) stream.flush();
    } catch(IOException t) {
        logger.error("Error generating CSV", t);
        throw t; // re-throw instead of swallowing
    } finally {
        IOException closeException = null;
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                closeException = e;
                logger.error("Error closing CSV stream", e);
            }
            stream = null;
        }
        if (!generovat && fFileOutput != null) {
            fFileOutput.delete();
        }
        if (closeException != null) {
            throw closeException;
        }
    }
}
```

### Files Affected
- `AbsCsvDoklad.java` - Abstract base class
- All CSV export implementations extending this class

---

## Category 2: Conditional Resource Closure

### Pattern Description
Resources that are conditionally closed based on business logic.

### Example
```java
FileOutputStream stream = null;
try {
    stream = new FileOutputStream(file);
    boolean success = processData();

    if (success) {
        stream.close();
        stream = null;
    } else {
        // Keep stream open for retry
        return;
    }
} finally {
    if (stream != null) stream.close();
}
```

### Why Try-With-Resources Doesn't Work
- Try-with-resources **always** closes resources
- No way to conditionally prevent closure
- Business logic requires resource to remain open in some cases

### Recommended Solutions

#### Option 1: Restructure Logic (BEST)
```java
// Separate "should we keep stream" decision from resource management
try (FileOutputStream stream = new FileOutputStream(file)) {
    boolean success = processData();
    if (!success) {
        throw new ProcessingException("Retry needed");
    }
    // Stream automatically closed on success
}
// Handle retry logic at higher level
```

#### Option 2: Transfer Ownership
```java
public FileOutputStream createStream() throws IOException {
    return new FileOutputStream(file);
}

// Caller manages resource
try (FileOutputStream stream = createStream()) {
    if (processData(stream)) {
        // close happens automatically
    }
}
```

### Files Potentially Affected
- Classes with retry logic
- Streaming operations that may be suspended

---

## Category 3: Resources Returned from Methods

### Pattern Description
Methods that create and return a resource for caller to manage.

### Example
```java
public Connection getConnection() throws SQLException {
    Connection con = null;
    try {
        con = DriverManager.getConnection(url, user, pass);
        con.setAutoCommit(false);
        return con;
    } catch (SQLException e) {
        if (con != null) con.close();
        throw e;
    }
}
```

### Why Try-With-Resources is Tricky
- Resource lifetime extends beyond method scope
- Caller must close the resource
- Method may need to close on error before return

### Recommended Solutions

#### Option 1: Document Ownership (ACCEPTABLE)
```java
/**
 * Creates a new database connection.
 * <p>
 * <b>IMPORTANT:</b> Caller is responsible for closing the returned connection.
 * Use try-with-resources:
 * <pre>
 * try (Connection conn = getConnection()) {
 *     // use connection
 * }
 * </pre>
 *
 * @return new Connection that must be closed by caller
 * @throws SQLException if connection fails
 */
public Connection getConnection() throws SQLException {
    Connection con = null;
    try {
        con = DriverManager.getConnection(url, user, pass);
        con.setAutoCommit(false);
        return con;
    } catch (SQLException e) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException closeEx) {
                e.addSuppressed(closeEx);
            }
        }
        throw e;
    }
}
```

#### Option 2: Callback Pattern (ADVANCED)
```java
public <T> T withConnection(ConnectionCallback<T> callback) throws SQLException {
    try (Connection con = DriverManager.getConnection(url, user, pass)) {
        con.setAutoCommit(false);
        return callback.execute(con);
    }
}

// Usage:
String result = withConnection(conn -> {
    // use connection
    return processData(conn);
});
```

---

## Category 4: Resources in Loops

### Pattern Description
Resources created in loops where early exit may occur.

### Example
```java
for (File file : files) {
    FileInputStream fis = null;
    try {
        fis = new FileInputStream(file);
        if (processFile(fis)) {
            break; // Early exit - what about cleanup?
        }
    } finally {
        if (fis != null) fis.close();
    }
}
```

### Solution: Try-With-Resources Works Great Here!
```java
for (File file : files) {
    try (FileInputStream fis = new FileInputStream(file)) {
        if (processFile(fis)) {
            break; // fis is automatically closed before break
        }
    }
    // Each iteration's resource is properly closed
}
```

**This is actually a GOOD case for try-with-resources!**

---

## Category 5: Multiple Resources with Complex Dependencies

### Pattern Description
Resources where closure order is critical and non-standard.

### Example
```java
Connection con = null;
Statement st = null;
ResultSet rs = null;
try {
    con = getConnection();
    st = con.createStatement();
    rs = st.executeQuery(sql);

    // Must keep connection alive for cursor
    registerCursor(rs, con);
    return rs; // Caller closes rs and con later
} catch (SQLException e) {
    if (rs != null) try { rs.close(); } catch (SQLException ex) {}
    if (st != null) try { st.close(); } catch (SQLException ex) {}
    if (con != null) try { con.close(); } catch (SQLException ex) {}
    throw e;
}
```

### Why Try-With-Resources May Not Work
- Resources have unusual lifecycle
- Ownership transfer to caller
- Non-standard close order requirements

### Recommended Solutions

#### Option 1: Wrapper Object
```java
public class CursorHandle implements AutoCloseable {
    private final ResultSet rs;
    private final Statement st;
    private final Connection con;

    public CursorHandle(Connection con, Statement st, ResultSet rs) {
        this.con = con;
        this.st = st;
        this.rs = rs;
    }

    public ResultSet getResultSet() { return rs; }

    @Override
    public void close() throws SQLException {
        SQLException exception = null;
        try { if (rs != null) rs.close(); } catch (SQLException e) { exception = e; }
        try { if (st != null) st.close(); } catch (SQLException e) {
            if (exception != null) exception.addSuppressed(e);
            else exception = e;
        }
        try { if (con != null) con.close(); } catch (SQLException e) {
            if (exception != null) exception.addSuppressed(e);
            else exception = e;
        }
        if (exception != null) throw exception;
    }
}

// Usage:
try (CursorHandle cursor = createCursor()) {
    ResultSet rs = cursor.getResultSet();
    // use rs
}
```

---

## Category 6: finalize() Methods

### Pattern Found
```java
protected void finalize() {
    try {
        if (stream != null) stream.close();
    } catch (Throwable t) {}
}
```

### Why This is WRONG
- `finalize()` is **deprecated** in Java 9+
- **Unreliable** - may never be called
- **Unpredictable** - runs on GC thread at random time
- **Performance impact** - delays garbage collection
- **Should never be used for resource cleanup**

### Recommended Solutions

#### Solution 1: Remove finalize() Entirely (BEST)
```java
// Delete the finalize() method completely
// Use try-with-resources or explicit close() instead
```

#### Solution 2: If Really Needed - Use Cleaner API (Java 9+)
```java
import java.lang.ref.Cleaner;

public class MyResource {
    private static final Cleaner cleaner = Cleaner.create();

    private FileOutputStream stream;
    private final Cleaner.Cleanable cleanable;

    public MyResource() {
        this.cleanable = cleaner.register(this, new CloseAction(stream));
    }

    private static class CloseAction implements Runnable {
        private FileOutputStream stream;
        CloseAction(FileOutputStream stream) { this.stream = stream; }
        @Override
        public void run() {
            if (stream != null) {
                try { stream.close(); } catch (IOException e) {}
            }
        }
    }

    public void close() {
        cleanable.clean(); // Manual cleanup
    }
}
```

**But really, just use try-with-resources or explicit close()!**

---

## Summary Table

| Category | Can Use Try-With-Resources? | Recommended Approach |
|----------|---------------------------|---------------------|
| Instance fields | ❌ No | Implement AutoCloseable |
| Conditional close | ❌ No | Restructure logic |
| Returned resources | ⚠️ Partial | Document + error handling |
| Loop resources | ✅ Yes | Perfect use case! |
| Complex dependencies | ⚠️ Depends | Wrapper object |
| finalize() | ❌ Never | Remove or use Cleaner |

---

## Action Items for KIS Project

### Immediate Actions
1. ✅ **Remove all finalize() methods** - They're harmful
2. ⚠️ **Review AbsCsvDoklad pattern** - Consider implementing AutoCloseable
3. ✅ **Document resource-returning methods** - Clear ownership rules

### Medium-Term Refactoring
1. **Convert instance-level streams to method-local where possible**
2. **Implement AutoCloseable for classes with resource lifecycle**
3. **Use builder pattern for complex resource initialization**

### Low Priority
1. Review all conditional close patterns
2. Consider callback pattern for connection management
3. Wrap complex resource dependencies in AutoCloseable wrappers

---

## Testing Checklist for Problematic Cases

When refactoring these patterns, verify:

- [ ] Resource always closed on exception
- [ ] Resource closed on normal return
- [ ] Resource closed on early exit (break, continue, return)
- [ ] Multiple exceptions handled correctly
- [ ] Thread safety (if applicable)
- [ ] No resource leaks under any circumstances
- [ ] Suppressed exceptions preserved
- [ ] Memory profiling shows no leaks

---

**Document Version:** 1.0
**Last Updated:** 2025-12-05
**Status:** Analysis Complete
