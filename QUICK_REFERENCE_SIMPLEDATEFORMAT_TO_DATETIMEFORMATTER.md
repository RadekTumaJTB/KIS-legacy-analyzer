# Quick Reference: SimpleDateFormat → DateTimeFormatter Migration

## Rychlý start pro vývojáře

### Proč migrovat?
```java
// ❌ NEBEZPEČNÉ - Thread-unsafe, race conditions!
private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

// ✅ BEZPEČNÉ - Thread-safe, immutable
private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
```

---

## Základní migrace patterns

### 1. Static field deklarace

```java
// ❌ PŘED
private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

// ✅ PO
private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
```

### 2. Formátování aktuálního data

```java
// ❌ PŘED
String today = sdf.format(new Date());

// ✅ PO
String today = LocalDate.now().format(DATE_FORMATTER);
```

### 3. Formátování java.util.Date

```java
// ❌ PŘED
String dateString = sdf.format(date);

// ✅ PO
LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
String dateString = localDate.format(DATE_FORMATTER);
```

### 4. Formátování java.sql.Date

```java
// ❌ PŘED
String dateString = sdf.format(sqlDate);

// ✅ PO
LocalDate localDate = sqlDate.toLocalDate();  // java.sql.Date má direct konverzi
String dateString = localDate.format(DATE_FORMATTER);
```

### 5. Parsing date stringů

```java
// ❌ PŘED
Date date = sdf.parse(dateString);

// ✅ PO
LocalDate localDate = LocalDate.parse(dateString, DATE_FORMATTER);

// Pro konverzi zpět na Date:
Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
```

---

## Časté operace s daty

### Získání aktuálního data

```java
// ❌ PŘED
Date now = new Date();
String today = sdf.format(now);

// ✅ PO
String today = LocalDate.now().format(DATE_FORMATTER);
```

### Minulý měsíc (poslední den)

```java
// ❌ PŘED
Calendar cal = Calendar.getInstance();
cal.add(Calendar.MONTH, -1);
cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
String lastMonth = sdf.format(cal.getTime());

// ✅ PO
LocalDate lastMonth = LocalDate.now()
                               .minusMonths(1)
                               .withDayOfMonth(LocalDate.now()
                                                       .minusMonths(1)
                                                       .lengthOfMonth());
String lastMonthStr = lastMonth.format(DATE_FORMATTER);
```

### Příští měsíc (poslední den)

```java
// ❌ PŘED
Calendar cal = Calendar.getInstance();
cal.add(Calendar.MONTH, 1);
cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
String nextMonth = sdf.format(cal.getTime());

// ✅ PO
LocalDate nextMonth = LocalDate.now()
                               .plusMonths(1)
                               .withDayOfMonth(LocalDate.now()
                                                       .plusMonths(1)
                                                       .lengthOfMonth());
String nextMonthStr = nextMonth.format(DATE_FORMATTER);
```

### Včera

```java
// ❌ PŘED
Calendar cal = Calendar.getInstance();
cal.add(Calendar.DAY_OF_YEAR, -1);
String yesterday = sdf.format(cal.getTime());

// ✅ PO
String yesterday = LocalDate.now().minusDays(1).format(DATE_FORMATTER);
```

### Zítra

```java
// ❌ PŘED
Calendar cal = Calendar.getInstance();
cal.add(Calendar.DAY_OF_YEAR, 1);
String tomorrow = sdf.format(cal.getTime());

// ✅ PO
String tomorrow = LocalDate.now().plusDays(1).format(DATE_FORMATTER);
```

---

## Importy

### Odstraň

```java
import java.text.SimpleDateFormat;
```

### Přidej

```java
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
```

---

## Konverze mezi Date a LocalDate

### java.util.Date → LocalDate

```java
java.util.Date date = new Date();
LocalDate localDate = date.toInstant()
                          .atZone(ZoneId.systemDefault())
                          .toLocalDate();
```

### LocalDate → java.util.Date

```java
LocalDate localDate = LocalDate.now();
java.util.Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
```

### java.sql.Date → LocalDate

```java
java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
LocalDate localDate = sqlDate.toLocalDate();  // Direct method!
```

### LocalDate → java.sql.Date

```java
LocalDate localDate = LocalDate.now();
java.sql.Date sqlDate = java.sql.Date.valueOf(localDate);  // Direct method!
```

---

## Common patterns v projektu KIS

### Utils.getTodaysDate()

```java
// ❌ PŘED
public static String getTodaysDate() {
    return sdf.format(new Date());
}

// ✅ PO
public static String getTodaysDate() {
    return LocalDate.now().format(DATE_FORMATTER);
}
```

### Utils.date2String()

```java
// ❌ PŘED
public static String date2String(java.util.Date d) {
    return sdf.format(d);
}

// ✅ PO
public static String date2String(java.util.Date d) {
    LocalDate localDate = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    return localDate.format(DATE_FORMATTER);
}
```

### GenerateAll formátování

```java
// ❌ PŘED
String strDatum = sdf.format(datumDo);

// ✅ PO
LocalDate localDate = datumDo.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
String strDatum = localDate.format(DATE_FORMATTER);
```

---

## Časové formáty

### Různé formáty DateTimeFormatter

```java
// dd.MM.yyyy
DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd.MM.yyyy");

// yyyy-MM-dd
DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");

// dd/MM/yyyy HH:mm:ss
DateTimeFormatter formatter3 = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

// ISO format
DateTimeFormatter formatter4 = DateTimeFormatter.ISO_LOCAL_DATE;
```

### LocalDateTime (date + time)

```java
// Pokud potřebuješ i čas
LocalDateTime now = LocalDateTime.now();
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
String formatted = now.format(formatter);
```

---

## Checklist pro migraci

- [ ] Nahraď `SimpleDateFormat` → `DateTimeFormatter`
- [ ] Přidej `final` do static field
- [ ] Změň naming na UPPERCASE (konstantní konvence)
- [ ] Aktualizuj importy
- [ ] Nahraď `new Date()` → `LocalDate.now()`
- [ ] Konvertuj `Calendar` operace → `LocalDate` fluent API
- [ ] Pro existující `Date` objekty použij konverzi
- [ ] Otestuj thread-safety
- [ ] Otestuj formát výstupu

---

## Testování

### Manuální test

```java
public static void main(String[] args) {
    // Test formátování
    System.out.println("Today: " + LocalDate.now().format(DATE_FORMATTER));

    // Test konverze
    Date date = new Date();
    LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    System.out.println("Converted: " + localDate.format(DATE_FORMATTER));

    // Thread-safety test
    for (int i = 0; i < 10; i++) {
        new Thread(() -> {
            for (int j = 0; j < 1000; j++) {
                LocalDate.now().format(DATE_FORMATTER);
            }
        }).start();
    }
}
```

---

## Časté chyby

### 1. Zapomenutí konverze Date → LocalDate

```java
// ❌ CHYBA - nelze přímo formátovat Date pomocí DateTimeFormatter
String result = DATE_FORMATTER.format(date);  // CompileError!

// ✅ SPRÁVNĚ
LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
String result = localDate.format(DATE_FORMATTER);
```

### 2. Nepoužití final pro constant

```java
// ❌ ŠPATNĚ - není to konstanta
private static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

// ✅ SPRÁVNĚ - final constant
private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
```

### 3. Zapomenutí import statements

```java
// ❌ CHYBA - chybí importy
LocalDate date = LocalDate.now();  // CompileError!

// ✅ SPRÁVNĚ
import java.time.LocalDate;
LocalDate date = LocalDate.now();
```

---

## Výhody DateTimeFormatter

1. **Thread-safe** - žádné race conditions
2. **Immutable** - bezpečnější kód
3. **Modern API** - lepší než Date/Calendar
4. **Intuitivní** - čitelnější kód
5. **Performance** - optimalizováno pro concurrent použití

---

## Zdroje

- [Java Time API Documentation](https://docs.oracle.com/javase/8/docs/api/java/time/package-summary.html)
- [DateTimeFormatter JavaDoc](https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html)
- [LocalDate JavaDoc](https://docs.oracle.com/javase/8/docs/api/java/time/LocalDate.html)

---

**Vytvořeno**: 2025-12-05
**Pro projekt**: KIS Application Java 17 Migration
**Status**: Production Ready ✅
