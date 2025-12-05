# Quick Refactoring Guide
## Try-With-Resources Pattern - Developer Reference

**For:** KIS Application Developers
**Purpose:** Quick reference for refactoring resource management

---

## Quick Decision Tree

```
Do you have a resource that needs closing?
│
├─ YES → Is it AutoCloseable/Closeable?
│        │
│        ├─ YES → Does it live only in ONE method?
│        │        │
│        │        ├─ YES → ✅ USE TRY-WITH-RESOURCES
│        │        │
│        │        └─ NO → ⚠️ SEE "Instance Fields" section
│        │
│        └─ NO → ❌ Cannot use try-with-resources
│
└─ NO → Not applicable
```

---

## Common Patterns - Copy & Paste Ready

### 1. Single JDBC Statement

**BEFORE:**
```java
Statement st = null;
try {
    st = connection.createStatement();
    st.execute("UPDATE ...");
} catch (SQLException e) {
    throw new KisException("Error", e);
} finally {
    if (st != null) try { st.close(); } catch (SQLException e) {}
}
```

**AFTER:**
```java
try (Statement st = connection.createStatement()) {
    st.execute("UPDATE ...");
} catch (SQLException e) {
    throw new KisException("Error", e);
}
```

---

### 2. JDBC Query (Connection + Statement + ResultSet)

**BEFORE:**
```java
Connection con = null;
Statement st = null;
ResultSet rs = null;
try {
    con = DriverManager.getConnection(url, user, pass);
    st = con.createStatement();
    rs = st.executeQuery("SELECT ...");
    while (rs.next()) {
        // process
    }
} catch (SQLException e) {
    throw new KisException("Error", e);
} finally {
    if (rs != null) try { rs.close(); } catch (SQLException e) {}
    if (st != null) try { st.close(); } catch (SQLException e) {}
    if (con != null) try { con.close(); } catch (SQLException e) {}
}
```

**AFTER:**
```java
try (Connection con = DriverManager.getConnection(url, user, pass);
     Statement st = con.createStatement();
     ResultSet rs = st.executeQuery("SELECT ...")) {
    while (rs.next()) {
        // process
    }
} catch (SQLException e) {
    throw new KisException("Error", e);
}
```

**💡 TIP:** Resources close in REVERSE order: rs → st → con

---

### 3. File Reading (FileInputStream)

**BEFORE:**
```java
FileInputStream fis = null;
try {
    fis = new FileInputStream(file);
    // read from fis
} catch (IOException e) {
    throw e;
} finally {
    if (fis != null) try { fis.close(); } catch (IOException e) {}
}
```

**AFTER:**
```java
try (FileInputStream fis = new FileInputStream(file)) {
    // read from fis
} catch (IOException e) {
    throw e;
}
```

---

### 4. File Writing (FileOutputStream)

**BEFORE:**
```java
FileOutputStream fos = null;
try {
    fos = new FileOutputStream(file);
    fos.write(data);
    fos.flush();
} finally {
    if (fos != null) try { fos.close(); } catch (IOException e) {}
}
```

**AFTER:**
```java
try (FileOutputStream fos = new FileOutputStream(file)) {
    fos.write(data);
    fos.flush(); // Optional - close() calls flush() automatically
}
```

---

### 5. BufferedReader

**BEFORE:**
```java
BufferedReader br = null;
try {
    br = new BufferedReader(new FileReader(file));
    String line;
    while ((line = br.readLine()) != null) {
        // process line
    }
} finally {
    if (br != null) try { br.close(); } catch (IOException e) {}
}
```

**AFTER:**
```java
try (BufferedReader br = new BufferedReader(new FileReader(file))) {
    String line;
    while ((line = br.readLine()) != null) {
        // process line
    }
}
```

---

### 6. Multiple Sequential Files

**BEFORE:**
```java
FileInputStream fIn = null;
FileOutputStream fOut = null;
try {
    fIn = new FileInputStream(input);
    // read input
    fIn.close();
    fIn = null;

    fOut = new FileOutputStream(output);
    // write output
} finally {
    if (fIn != null) try { fIn.close(); } catch (IOException e) {}
    if (fOut != null) try { fOut.close(); } catch (IOException e) {}
}
```

**AFTER:**
```java
try (FileInputStream fIn = new FileInputStream(input)) {
    // read input
}

try (FileOutputStream fOut = new FileOutputStream(output)) {
    // write output
}
```

**OR nested:**
```java
try (FileInputStream fIn = new FileInputStream(input)) {
    // read input
    try (FileOutputStream fOut = new FileOutputStream(output)) {
        // write output
    }
}
```

---

### 7. PreparedStatement with Parameters

**BEFORE:**
```java
PreparedStatement ps = null;
ResultSet rs = null;
try {
    ps = connection.prepareStatement("SELECT * FROM users WHERE id = ?");
    ps.setInt(1, userId);
    rs = ps.executeQuery();
    if (rs.next()) {
        // process
    }
} finally {
    if (rs != null) try { rs.close(); } catch (SQLException e) {}
    if (ps != null) try { ps.close(); } catch (SQLException e) {}
}
```

**AFTER:**
```java
try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE id = ?")) {
    ps.setInt(1, userId);
    try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
            // process
        }
    }
}
```

---

## Common Mistakes to Avoid

### ❌ DON'T: Close manually inside try-with-resources
```java
try (FileInputStream fis = new FileInputStream(file)) {
    // ... work ...
    fis.close(); // ❌ WRONG - will close twice!
}
```

### ✅ DO: Let try-with-resources handle it
```java
try (FileInputStream fis = new FileInputStream(file)) {
    // ... work ...
} // ✅ Closes automatically
```

---

### ❌ DON'T: Assign null in try-with-resources
```java
FileInputStream fis = null;
try (fis = new FileInputStream(file)) { // ❌ Syntax error
    // ...
}
```

### ✅ DO: Declare and initialize in one line
```java
try (FileInputStream fis = new FileInputStream(file)) { // ✅ Correct
    // ...
}
```

---

### ❌ DON'T: Use try-with-resources for non-AutoCloseable
```java
try (StringBuilder sb = new StringBuilder()) { // ❌ StringBuilder is not AutoCloseable
    // ...
}
```

### ✅ DO: Only use with AutoCloseable/Closeable
```java
try (FileInputStream fis = new FileInputStream(file)) { // ✅ Correct
    // ...
}
```

---

## When NOT to Use Try-With-Resources

### 1. Resource is an Instance Field
```java
public class MyClass {
    private FileOutputStream stream; // ❌ Field - can't use try-with-resources

    public void openStream() {
        stream = new FileOutputStream(file);
    }

    public void closeStream() {
        if (stream != null) stream.close();
    }
}
```

**Solution:** Make the class implement AutoCloseable:
```java
public class MyClass implements AutoCloseable {
    private FileOutputStream stream;

    public void openStream() throws IOException {
        stream = new FileOutputStream(file);
    }

    @Override
    public void close() throws IOException {
        if (stream != null) {
            stream.close();
            stream = null;
        }
    }
}

// Usage:
try (MyClass obj = new MyClass()) {
    obj.openStream();
    // ... work ...
} // ✅ Automatically closed
```

---

### 2. Resource Returned from Method
```java
public Connection getConnection() throws SQLException {
    return DriverManager.getConnection(url); // Caller must close
}
```

**Solution:** Document it clearly:
```java
/**
 * @return Connection - caller MUST close using try-with-resources
 */
public Connection getConnection() throws SQLException {
    return DriverManager.getConnection(url);
}

// Usage:
try (Connection conn = getConnection()) {
    // use connection
}
```

---

### 3. Conditional Close Logic
```java
if (success) {
    stream.close(); // ❌ Can't do conditional close with try-with-resources
}
```

**Solution:** Refactor logic:
```java
try (FileOutputStream stream = new FileOutputStream(file)) {
    if (!doWork(stream)) {
        throw new ProcessingException("Failed");
    }
} // Always closes
```

---

## IntelliJ IDEA Quick Refactoring

### Step 1: Select the try-finally block
```java
FileInputStream fis = null;   ← Select from here
try {
    fis = new FileInputStream(file);
    // work
} finally {
    if (fis != null) fis.close();
}                              ← to here
```

### Step 2: Press Alt+Enter (Windows/Linux) or ⌥⏎ (Mac)

### Step 3: Select "Replace with try-with-resources"

### Step 4: Done! ✅

---

## Testing Your Refactoring

### Checklist:
```java
// Test 1: Normal flow
try (Resource r = new Resource()) {
    r.doWork();
} // ✅ Resource closed?

// Test 2: Exception during work
try (Resource r = new Resource()) {
    throw new RuntimeException("Error!");
} // ✅ Resource still closed?

// Test 3: Exception during resource creation
try (Resource r = new Resource()) { // Throws exception here
    r.doWork();
} // ✅ No resource leak?

// Test 4: Multiple resources
try (R1 r1 = new R1();
     R2 r2 = new R2()) {
    // work
} // ✅ Both closed in reverse order?
```

---

## Quick Reference: AutoCloseable Resources

### JDBC (java.sql.*)
- ✅ Connection
- ✅ Statement
- ✅ PreparedStatement
- ✅ CallableStatement
- ✅ ResultSet

### I/O (java.io.*)
- ✅ InputStream (all types)
- ✅ OutputStream (all types)
- ✅ Reader (all types)
- ✅ Writer (all types)
- ✅ RandomAccessFile

### NIO (java.nio.*)
- ✅ FileChannel
- ✅ SocketChannel
- ✅ ServerSocketChannel

### Other
- ✅ Scanner
- ✅ Formatter
- ✅ ZipFile
- ✅ JarFile

---

## Performance Notes

### Try-with-resources is:
- ✅ **Fast** - No performance overhead
- ✅ **Safe** - Compiler-verified
- ✅ **Predictable** - Deterministic cleanup
- ✅ **Better than finalize()** - finalize() is deprecated and unreliable

### Myth Busting:
- ❌ "Try-with-resources is slower" - FALSE
- ❌ "Manual close is more efficient" - FALSE
- ❌ "finalize() is a good backup" - FALSE (it's harmful!)

---

## Need Help?

### Common Compile Errors:

**Error:** "Incompatible types: X cannot be converted to AutoCloseable"
```java
try (StringBuilder sb = new StringBuilder()) { } // ❌ Not AutoCloseable
```
**Fix:** Only use with AutoCloseable/Closeable resources

---

**Error:** "Variable 'x' might not have been initialized"
```java
FileInputStream fis;
try (fis = new FileInputStream(file)) { } // ❌ Not initialized
```
**Fix:** Initialize in try-with-resources declaration:
```java
try (FileInputStream fis = new FileInputStream(file)) { } // ✅
```

---

**Error:** "Cannot assign a value to final variable 'x'"
```java
final FileInputStream fis;
try (fis = new FileInputStream(file)) { } // ❌ Final variable
```
**Fix:** Don't declare as final - try-with-resources handles it:
```java
try (FileInputStream fis = new FileInputStream(file)) { } // ✅
```

---

## Summary

### ✅ DO:
- Use try-with-resources for ALL closeable resources in method scope
- Close resources in reverse order (automatic)
- Let exceptions propagate naturally
- Remove finalize() methods

### ❌ DON'T:
- Close resources manually inside try-with-resources
- Use for non-AutoCloseable types
- Use for instance-level resources (without implementing AutoCloseable)
- Swallow exceptions silently

---

**Last Updated:** 2025-12-05
**Java Version:** 7+ (KIS uses Java 17 ✅)
**Quick Start:** Select try-finally block → Alt+Enter → "Replace with try-with-resources"
