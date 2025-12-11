# Excel Template Files Conversion Checklist

**Migration:** Apache POI HSSF (.xls) → XSSF (.xlsx)
**Date:** 2025-12-05
**Status:** PENDING CONVERSION

---

## Template Directory Locations

Based on `Constants.java`:

### Primary Template Directory
- **Linux:** `/opt/kis-banking/Konsolidace_JT/sablony/`
- **Windows:** `D:\Konsolidace_JT\sablony\`
- **Constant:** `SABLONY_FILES_PATH`

### Cartesis Template Directory
- **Linux:** `/opt/kis-banking/Konsolidace_JT/sablony/cartesis/`
- **Windows:** `D:\Konsolidace_JT\sablony\cartesis\`
- **Constant:** `SABLONY_CARTESIS`

---

## Required Template Files to Convert

### Main Templates (SABLONY_FILES_PATH)

- [ ] **Empty.xlsx** (formerly Empty.xls)
  - **Priority:** CRITICAL ⚠️
  - **Used by:** 50+ ESExport classes
  - **Purpose:** Base empty workbook template

- [ ] **SablonaBilance.xlsx**
  - **Used by:** Balance sheet exports
  - **Purpose:** Balance report template

- [ ] **SablonaBudgetMustek.xlsx**
  - **Used by:** Budget bridge exports
  - **Purpose:** Budget bridge template

- [ ] **SablonaCashFlow.xlsx**
  - **Used by:** Cash flow exports
  - **Purpose:** Cash flow report template

- [ ] **SablonaMenove.xlsx**
  - **Used by:** Currency exports
  - **Purpose:** Currency report template

- [ ] **SablonaMustek.xlsx**
  - **Used by:** Bridge exports
  - **Purpose:** General bridge template

- [ ] **SablonaMustek2007.xlsx**
  - **Used by:** Bridge exports (2007 format)
  - **Purpose:** 2007-specific bridge template

- [ ] **SablonaMustekPodnikatel.xlsx**
  - **Used by:** Entrepreneur bridge exports
  - **Purpose:** Entrepreneur-specific bridge template

- [ ] **SablonaMustekUnif.xlsx**
  - **Used by:** Unified bridge exports
  - **Purpose:** Unified chart of accounts bridge

- [ ] **SablonaPrehledZamek.xlsx**
  - **Used by:** Lock overview exports
  - **Purpose:** Lock status overview template

- [ ] **SablonaSubkons6Vazby.xlsx**
  - **Used by:** Sub-consolidation link exports
  - **Purpose:** Sub-consolidation links template

- [ ] **SablonaSubkonsolidace.xlsx**
  - **Used by:** Sub-consolidation exports
  - **Purpose:** Sub-consolidation template

- [ ] **SablonaSubkonsolidace2.xlsx**
  - **Used by:** Sub-consolidation exports (variant 2)
  - **Purpose:** Alternative sub-consolidation template

- [ ] **SablonaSubkonsolidaceClen2.xlsx**
  - **Used by:** Sub-consolidation member exports
  - **Purpose:** Member-specific sub-consolidation

- [ ] **SablonaSubkonsolidaceKamil.xlsx**
  - **Used by:** Kamil's sub-consolidation exports
  - **Purpose:** Custom sub-consolidation template

- [ ] **SablonaUverove.xlsx**
  - **Used by:** Loan exports
  - **Purpose:** Loan report template

- [ ] **SablonaVV.xlsx**
  - **Used by:** VV exports
  - **Purpose:** VV report template

### Cartesis Templates (SABLONY_CARTESIS)

- [ ] **SablonaKurzy.xlsx**
  - **Used by:** ESExportKurzy.java
  - **Purpose:** Exchange rates export

- [ ] **SablonaOsoby.xlsx**
  - **Used by:** ESExportOsoby.java
  - **Purpose:** Persons export

- [ ] **SablonaProtistrany.xlsx**
  - **Used by:** ESExportProtistrany.java
  - **Purpose:** Counterparties export

---

## Conversion Methods

### Method 1: Microsoft Excel (Windows/Mac)

```
1. Open the .xls file in Microsoft Excel
2. Click File → Save As
3. In "Save as type" dropdown, select: "Excel Workbook (*.xlsx)"
4. Keep the same filename, just change extension to .xlsx
5. Click Save
6. Repeat for all template files
```

### Method 2: LibreOffice Calc (Windows/Mac/Linux)

```
1. Open the .xls file in LibreOffice Calc
2. Click File → Save As
3. In "File type" dropdown, select: "Excel 2007-365 (.xlsx)"
4. Keep the same filename, just change extension to .xlsx
5. Click Save
6. Repeat for all template files
```

### Method 3: Batch Conversion (Linux/Mac Command Line)

```bash
#!/bin/bash
# Requires LibreOffice installed

# Navigate to template directory
cd /opt/kis-banking/Konsolidace_JT/sablony/

# Convert all .xls files to .xlsx
for file in *.xls; do
    echo "Converting $file..."
    libreoffice --headless --convert-to xlsx "$file"

    # Verify conversion succeeded
    if [ -f "${file%.xls}.xlsx" ]; then
        echo "✓ Successfully converted: ${file%.xls}.xlsx"
        # Optional: backup original
        # mv "$file" "${file}.bak"
    else
        echo "✗ Failed to convert: $file"
    fi
done

# Also convert Cartesis templates
cd cartesis/
for file in *.xls; do
    echo "Converting cartesis/$file..."
    libreoffice --headless --convert-to xlsx "$file"

    if [ -f "${file%.xls}.xlsx" ]; then
        echo "✓ Successfully converted: cartesis/${file%.xls}.xlsx"
    else
        echo "✗ Failed to convert: cartesis/$file"
    fi
done

echo "Conversion complete!"
```

### Method 4: Python Batch Conversion (Using openpyxl)

```python
#!/usr/bin/env python3
"""
Convert Excel .xls templates to .xlsx using openpyxl
Requires: pip install openpyxl xlrd
"""
import os
from pathlib import Path
import xlrd
from openpyxl import Workbook
from openpyxl.utils import get_column_letter

def convert_xls_to_xlsx(xls_path, xlsx_path):
    """Convert .xls to .xlsx preserving basic formatting"""
    # Read .xls file
    xls_book = xlrd.open_workbook(xls_path, formatting_info=True)

    # Create new .xlsx workbook
    xlsx_book = Workbook()
    xlsx_book.remove(xlsx_book.active)  # Remove default sheet

    # Copy each sheet
    for sheet_index in range(xls_book.nsheets):
        xls_sheet = xls_book.sheet_by_index(sheet_index)
        xlsx_sheet = xlsx_book.create_sheet(title=xls_sheet.name)

        # Copy cell values
        for row in range(xls_sheet.nrows):
            for col in range(xls_sheet.ncols):
                cell_value = xls_sheet.cell_value(row, col)
                xlsx_sheet.cell(row=row+1, column=col+1, value=cell_value)

    # Save .xlsx file
    xlsx_book.save(xlsx_path)
    print(f"✓ Converted: {xls_path.name} → {xlsx_path.name}")

def main():
    # Template directories
    dirs = [
        Path("/opt/kis-banking/Konsolidace_JT/sablony/"),
        Path("/opt/kis-banking/Konsolidace_JT/sablony/cartesis/")
    ]

    for template_dir in dirs:
        if not template_dir.exists():
            print(f"⚠ Directory not found: {template_dir}")
            continue

        print(f"\nProcessing directory: {template_dir}")

        for xls_file in template_dir.glob("*.xls"):
            xlsx_file = xls_file.with_suffix(".xlsx")

            try:
                convert_xls_to_xlsx(xls_file, xlsx_file)
            except Exception as e:
                print(f"✗ Error converting {xls_file.name}: {e}")

if __name__ == "__main__":
    main()
```

---

## Verification Steps

After converting each template file:

1. **Open in Excel/LibreOffice**
   - Verify the file opens without errors
   - Check that all sheets are present
   - Verify formatting is preserved (fonts, colors, borders)

2. **Check File Size**
   - .xlsx files are typically 30-50% smaller than .xls
   - If file is larger, formatting might be corrupted

3. **Test with Application**
   - Run a test export using the converted template
   - Verify generated Excel file is correct
   - Check data formatting and layout

4. **Backup Original Files**
   ```bash
   # Before deletion, backup .xls files
   mkdir /opt/kis-banking/Konsolidace_JT/sablony_backup_xls/
   cp -r /opt/kis-banking/Konsolidace_JT/sablony/*.xls \
         /opt/kis-banking/Konsolidace_JT/sablony_backup_xls/
   ```

---

## Conversion Priority

### Phase 1: Critical Templates (Convert First)
1. Empty.xlsx - Used by most exports
2. SablonaMustek.xlsx - High usage
3. SablonaBilance.xlsx - Financial reports

### Phase 2: High-Usage Templates
4. SablonaCashFlow.xlsx
5. SablonaSubkonsolidace.xlsx
6. SablonaUverove.xlsx

### Phase 3: Specialized Templates
7. All remaining SABLONY_FILES_PATH templates
8. All SABLONY_CARTESIS templates

---

## Known Issues & Solutions

### Issue 1: Formatting Lost
**Problem:** Colors or borders don't convert properly
**Solution:** Manually recreate formatting in .xlsx file after conversion

### Issue 2: Formulas Break
**Problem:** Excel formulas don't work after conversion
**Solution:** Use LibreOffice for conversion (better formula compatibility)

### Issue 3: Large Files
**Problem:** .xlsx file is unexpectedly large
**Solution:** Re-save in Excel to optimize file size

### Issue 4: Encoding Issues
**Problem:** Czech characters (ě, š, č, ř, ž) display incorrectly
**Solution:** Ensure UTF-8 encoding during conversion

---

## Post-Conversion Checklist

After converting all templates:

- [ ] All .xls files backed up to safe location
- [ ] All .xlsx files tested by opening in Excel/LibreOffice
- [ ] At least 3 different export types tested with new templates
- [ ] File sizes verified (should be smaller than .xls)
- [ ] Czech characters display correctly
- [ ] All formulas still work (if any)
- [ ] Application builds without errors
- [ ] Integration tests pass
- [ ] User acceptance testing completed

---

## Rollback Plan

If converted templates cause issues:

1. **Immediate Rollback:**
   ```bash
   # Restore from backup
   cp /opt/kis-banking/Konsolidace_JT/sablony_backup_xls/*.xls \
      /opt/kis-banking/Konsolidace_JT/sablony/
   ```

2. **Code Rollback:**
   ```bash
   # Revert Java code changes
   git checkout HEAD -- src/main/java/cz/jtbank/konsolidace/excel/
   ```

3. **Dual-Format Support:**
   - Modify code to support both .xls and .xlsx templates
   - Keep both template versions during transition period

---

## Timeline Estimate

- **Manual conversion:** 2-3 hours (19 templates)
- **Batch conversion:** 15-30 minutes (automated)
- **Testing:** 2-4 hours
- **Total:** 3-7 hours

---

## Contact & Support

For issues during conversion:
- Check LibreOffice documentation: https://www.libreoffice.org/
- Check Apache POI documentation: https://poi.apache.org/
- Test each template individually before full deployment

---

**Prepared by:** Migration Script
**Date:** 2025-12-05
**Version:** 1.0
