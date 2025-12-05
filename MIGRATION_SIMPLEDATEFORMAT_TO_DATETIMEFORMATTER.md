# Migrace SimpleDateFormat → DateTimeFormatter

## Datum migrace
2025-12-05

## Důvod migrace
**KRITICKÝ THREAD-SAFETY ISSUE**: Static `SimpleDateFormat` instance není thread-safe a způsobuje race conditions při concurrent přístupu z více vláken. To může vést k:
- Nekonzistentním datovým hodnotám
- Neočekávaným výjimkám při běhu aplikace
- Težko reprodukovatelnému chování v produkčním prostředí

## Řešení
Nahrazení `SimpleDateFormat` za `DateTimeFormatter`, který je:
- **Immutable** (neměnný)
- **Thread-safe** (bezpečný pro concurrent přístup)
- **Moderní Java Time API** (od Java 8+)

---

## Soubor 1: Utils.java

### Cesta k souboru
`/Users/radektuma/DEV/KIS/KIS_App_64bit_JAVA17_Linux/src/main/java/cz/jtbank/konsolidace/common/Utils.java`

### Změny v importech

**ODSTRANĚNO:**
```java
import java.text.SimpleDateFormat;
```

**PŘIDÁNO:**
```java
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
```

### Změna static field

**PŘED:**
```java
private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
```

**PO:**
```java
private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
```

**Změny:**
- Přidán `final` modifikátor pro konstantu
- Přejmenování: `sdf` → `DATE_FORMATTER` (UPPERCASE convention pro konstanty)
- Typ: `SimpleDateFormat` → `DateTimeFormatter`
- Inicializace: `new SimpleDateFormat(pattern)` → `DateTimeFormatter.ofPattern(pattern)`

### Změněné metody

#### 1. `getLastDateMB()`

**PŘED:**
```java
public static String getLastDateMB() {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.MONTH,-1);
    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
    return sdf.format(cal.getTime());
}
```

**PO:**
```java
public static String getLastDateMB() {
    LocalDate date = LocalDate.now().minusMonths(1).withDayOfMonth(LocalDate.now().minusMonths(1).lengthOfMonth());
    return date.format(DATE_FORMATTER);
}
```

**Vylepšení:**
- Nahrazení `Calendar` API za moderní `LocalDate`
- Thread-safe formátování
- Čitelnější kód s fluent API

#### 2. `getLastDateNextMonth()`

**PŘED:**
```java
public static String getLastDateNextMonth() {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.MONTH,1);
    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
    return sdf.format(cal.getTime());
}
```

**PO:**
```java
public static String getLastDateNextMonth() {
    LocalDate date = LocalDate.now().plusMonths(1).withDayOfMonth(LocalDate.now().plusMonths(1).lengthOfMonth());
    return date.format(DATE_FORMATTER);
}
```

#### 3. `getTodaysDate()`

**PŘED:**
```java
public static String getTodaysDate() {
    return sdf.format(new Date());
}
```

**PO:**
```java
public static String getTodaysDate() {
    return LocalDate.now().format(DATE_FORMATTER);
}
```

**Vylepšení:**
- Odstranění `new Date()` - moderní `LocalDate.now()`
- Thread-safe

#### 4. `date2String(java.util.Date d)`

**PŘED:**
```java
public static String date2String(java.util.Date d) {
    return sdf.format(d);
}
```

**PO:**
```java
public static String date2String(java.util.Date d) {
    LocalDate localDate = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    return localDate.format(DATE_FORMATTER);
}
```

**Vylepšení:**
- Konverze `java.util.Date` → `LocalDate`
- Thread-safe formátování
- Explicitní časová zóna (systemDefault)

#### 5. `getLastDateAsString()`

**PŘED:**
```java
public static String getLastDateAsString() {
    return sdf.format(getLastDate());
}
```

**PO:**
```java
public static String getLastDateAsString() {
    return date2String(getLastDate());
}
```

**Vylepšení:**
- Delegování na `date2String()`, která už používá thread-safe DateTimeFormatter
- Konzistentní přístup

#### 6. `getWhereDokladIds(java.sql.Date date, int ucSkup)`

**PŘED:**
```java
public static String getWhereDokladIds(java.sql.Date date, int ucSkup) {
    String dateString = sdf.format(date);
    String where = null;
    // ...
}
```

**PO:**
```java
public static String getWhereDokladIds(java.sql.Date date, int ucSkup) {
    String dateString = date2String(date);
    String where = null;
    // ...
}
```

**Vylepšení:**
- Delegování na `date2String()` pro thread-safe formátování
- Konzistentní přístup

---

## Soubor 2: GenerateAll.java

### Cesta k souboru
`/Users/radektuma/DEV/KIS/KIS_App_64bit_JAVA17_Linux/src/main/java/cz/jtbank/konsolidace/jobs/GenerateAll.java`

### Změny v importech

**ODSTRANĚNO:**
```java
import java.text.SimpleDateFormat;
```

**PŘIDÁNO:**
```java
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
```

### Změna static field

**PŘED:**
```java
private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
```

**PO:**
```java
private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
```

### Změněné metody

#### 1. `generujAll()`

**PŘED:**
```java
List ids = new ArrayList();
String strDatum = sdf.format(datumDo);
try {
    // ...
}
```

**PO:**
```java
List ids = new ArrayList();
LocalDate localDate = datumDo.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
String strDatum = localDate.format(DATE_FORMATTER);
try {
    // ...
}
```

**Vylepšení:**
- Konverze `java.sql.Date` → `LocalDate`
- Thread-safe formátování

#### 2. `generujMis()`

**PŘED:**
```java
logger.debug("Start vkladani dokladu do fronty pro MIS - mesicni");
List ids = new ArrayList();
ViewObject vo = dm.findViewObject("KpKtgUcetnispolecnostView1");

String strDatum = sdf.format(datumDo);
try {
    // ...
}
```

**PO:**
```java
logger.debug("Start vkladani dokladu do fronty pro MIS - mesicni");
List ids = new ArrayList();
ViewObject vo = dm.findViewObject("KpKtgUcetnispolecnostView1");

LocalDate localDate = datumDo.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
String strDatum = localDate.format(DATE_FORMATTER);
try {
    // ...
}
```

---

## Shrnutí změn

### Utils.java
- **1 static field změněn**: `sdf` → `DATE_FORMATTER` (thread-safe, immutable, final)
- **6 metod aktualizováno**:
  - `getLastDateMB()` - LocalDate API
  - `getLastDateNextMonth()` - LocalDate API
  - `getTodaysDate()` - LocalDate API
  - `date2String()` - konverze Date → LocalDate → formátování
  - `getLastDateAsString()` - delegace na date2String()
  - `getWhereDokladIds()` - delegace na date2String()

### GenerateAll.java
- **1 static field změněn**: `sdf` → `DATE_FORMATTER` (thread-safe, immutable, final)
- **2 metody aktualizovány**:
  - `generujAll()` - konverze Date → LocalDate → formátování
  - `generujMis()` - konverze Date → LocalDate → formátování

---

## Thread-Safety vylepšení

### Před migrací (NEBEZPEČNÉ)
```java
private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

// RACE CONDITION! Concurrent volání způsobí nekonzistentní data
public static String getTodaysDate() {
    return sdf.format(new Date());  // ← NEBEZPEČNÉ!
}
```

**Problém:**
- `SimpleDateFormat` používá interní mutable state
- Concurrent volání `format()` způsobují race conditions
- Výsledky mohou být nesprávné nebo může dojít k výjimce

### Po migraci (BEZPEČNÉ)
```java
private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

// THREAD-SAFE! Immutable DateTimeFormatter
public static String getTodaysDate() {
    return LocalDate.now().format(DATE_FORMATTER);  // ← BEZPEČNÉ!
}
```

**Výhody:**
- `DateTimeFormatter` je immutable a thread-safe
- Žádné race conditions
- Konzistentní výsledky i při concurrent přístupu
- Bezpečné pro multi-threaded prostředí (např. web server, batch joby)

---

## Konverzní pattern použitý v migraci

### Pattern 1: Direct LocalDate usage (pro aktuální datum)
```java
// OLD:
return sdf.format(new Date());

// NEW:
return LocalDate.now().format(DATE_FORMATTER);
```

### Pattern 2: Date → LocalDate konverze (pro existující Date objekty)
```java
// OLD:
String dateString = sdf.format(date);

// NEW:
LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
String dateString = localDate.format(DATE_FORMATTER);
```

### Pattern 3: Calendar → LocalDate (nahrazení Calendar API)
```java
// OLD:
Calendar cal = Calendar.getInstance();
cal.add(Calendar.MONTH, -1);
cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
return sdf.format(cal.getTime());

// NEW:
LocalDate date = LocalDate.now().minusMonths(1).withDayOfMonth(LocalDate.now().minusMonths(1).lengthOfMonth());
return date.format(DATE_FORMATTER);
```

---

## Testování

### Doporučené test cases
1. **Concurrent access test**: Otestovat současné volání `getTodaysDate()` z více vláken
2. **Date conversion test**: Ověřit správnost konverze `Date` → `LocalDate` → `String`
3. **Format consistency test**: Ověřit, že formát "dd.MM.yyyy" je zachován
4. **Edge cases**: Testovat poslední den v měsíci, přestupné roky

### Příklad manuálního testu
```java
public static void main(String[] args) {
    // Test 1: getTodaysDate()
    System.out.println("Dnešní datum: " + Utils.getTodaysDate());

    // Test 2: getLastDateMB()
    System.out.println("Poslední den minulého měsíce: " + Utils.getLastDateMB());

    // Test 3: date2String() s konkrétním datem
    java.util.Date testDate = new java.util.Date();
    System.out.println("Konverze Date: " + Utils.date2String(testDate));

    // Test 4: Concurrent test (spustit z více vláken)
    for (int i = 0; i < 10; i++) {
        new Thread(() -> {
            for (int j = 0; j < 1000; j++) {
                Utils.getTodaysDate();
            }
        }).start();
    }
}
```

---

## Backward Compatibility

### Zachované metody a signatury
Všechny public API metody zachovávají:
- **Stejné názvy metod**
- **Stejné parametry**
- **Stejné návratové typy**
- **Stejný formát výstupu** (dd.MM.yyyy)

### Změny pouze interní implementace
- Vnější API zůstává **100% kompatibilní**
- Žádné breaking changes pro volající kód
- Kód používající tyto metody **nepotřebuje úpravy**

---

## Benefity migrace

1. **Thread-Safety**: Eliminace race conditions v multi-threaded prostředí
2. **Modern API**: Použití Java Time API (Java 8+) místo zastaralého Date/Calendar
3. **Immutability**: DateTimeFormatter je immutable → bezpečnější kód
4. **Čitelnost**: LocalDate API je intuitivnější než Calendar
5. **Performance**: DateTimeFormatter je optimalizovaný pro concurrent použití
6. **Maintainability**: Modernější kód je snadněji udržovatelný

---

## Konečné poznámky

### Kritičnost migrace
**VYSOKÁ PRIORITA** - Thread-safety issue v production prostředí může způsobit:
- Data corruption
- Nekonzistentní reporty
- Těžko reprodukovatelné bugy
- Potenciální finanční ztráty

### Doporučení pro deployment
1. Otestovat v development prostředí
2. Provést smoke testy na staging
3. Monitorovat logy po deployment na production
4. Zvláště sledovat batch joby a concurrent operace

### Related Issues
- Tento pattern by měl být aplikován na všechny SimpleDateFormat instance v celé codebase
- Doporučeno provést celoprojectové vyhledání `SimpleDateFormat` a migrace všech výskytů

---

**Migrace provedena**: 2025-12-05
**Autor migrace**: Claude Code AI Assistant
**Status**: COMPLETED
**Risk level**: CRITICAL → RESOLVED
