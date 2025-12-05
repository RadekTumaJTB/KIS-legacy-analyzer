#!/usr/bin/env python3
"""
Migrate Apache POI HSSF to XSSF/SS interfaces
"""
import os
import re
import sys

def migrate_file(filepath):
    """Migrate a single Java file from HSSF to XSSF"""
    with open(filepath, 'r', encoding='utf-8', errors='ignore') as f:
        content = f.read()

    original_content = content

    # Replace HSSF imports with SS interface imports
    replacements = [
        # Import replacements
        (r'import org\.apache\.poi\.poifs\.filesystem\.POIFSFileSystem;', ''),
        (r'import org\.apache\.poi\.hssf\.record\.\*;', ''),
        (r'import org\.apache\.poi\.hssf\.model\.\*;', ''),
        (r'import org\.apache\.poi\.hssf\.usermodel\.HSSFWorkbook;', 'import org.apache.poi.ss.usermodel.Workbook;\nimport org.apache.poi.xssf.usermodel.XSSFWorkbook;'),
        (r'import org\.apache\.poi\.hssf\.usermodel\.HSSFSheet;', 'import org.apache.poi.ss.usermodel.Sheet;'),
        (r'import org\.apache\.poi\.hssf\.usermodel\.HSSFRow;', 'import org.apache.poi.ss.usermodel.Row;'),
        (r'import org\.apache\.poi\.hssf\.usermodel\.HSSFCell;', 'import org.apache.poi.ss.usermodel.Cell;'),
        (r'import org\.apache\.poi\.hssf\.usermodel\.HSSFCellStyle;', 'import org.apache.poi.ss.usermodel.CellStyle;'),
        (r'import org\.apache\.poi\.hssf\.usermodel\.HSSFFont;', 'import org.apache.poi.ss.usermodel.Font;'),
        (r'import org\.apache\.poi\.hssf\.usermodel\.HSSFDataFormat;', 'import org.apache.poi.ss.usermodel.DataFormat;'),
        (r'import org\.apache\.poi\.hssf\.util\.HSSFColor;', 'import org.apache.poi.ss.usermodel.IndexedColors;\nimport org.apache.poi.ss.usermodel.FillPatternType;'),
        (r'import org\.apache\.poi\.hssf\.util\.\*;', 'import org.apache.poi.ss.usermodel.*;'),
        (r'import org\.apache\.poi\.hssf\.usermodel\.\*;', 'import org.apache.poi.ss.usermodel.*;'),

        # Type replacements
        (r'\bHSSFWorkbook\b', 'Workbook'),
        (r'\bHSSFSheet\b', 'Sheet'),
        (r'\bHSSFRow\b', 'Row'),
        (r'\bHSSFCell\b', 'Cell'),
        (r'\bHSSFCellStyle\b', 'CellStyle'),
        (r'\bHSSFFont\b', 'Font'),
        (r'\bHSSFDataFormat\b', 'DataFormat'),

        # Font methods
        (r'\.setBoldweight\(HSSFFont\.BOLDWEIGHT_BOLD\)', '.setBold(true)'),
        (r'\.setBoldweight\(Font\.BOLDWEIGHT_BOLD\)', '.setBold(true)'),

        # Cell type constants
        (r'HSSFCell\.ENCODING_UTF_16', '// UTF-16 is default in XSSF'),
        (r'\.setEncoding\(\s*HSSFCell\.ENCODING_UTF_16\s*\);', '// UTF-16 encoding is automatic in XSSF'),
        (r'HSSFCell\.CELL_TYPE_STRING', 'CellType.STRING'),
        (r'HSSFCell\.CELL_TYPE_NUMERIC', 'CellType.NUMERIC'),
        (r'HSSFCell\.CELL_TYPE_FORMULA', 'CellType.FORMULA'),
        (r'HSSFCell\.CELL_TYPE_BLANK', 'CellType.BLANK'),
        (r'HSSFCell\.CELL_TYPE_BOOLEAN', 'CellType.BOOLEAN'),
        (r'HSSFCell\.CELL_TYPE_ERROR', 'CellType.ERROR'),

        # Color replacements
        (r'HSSFColor\.(\w+)\.index', r'IndexedColors.\1.getIndex()'),

        # Fill pattern replacements
        (r'HSSFCellStyle\.SOLID_FOREGROUND', 'FillPatternType.SOLID_FOREGROUND'),
        (r'CellStyle\.SOLID_FOREGROUND', 'FillPatternType.SOLID_FOREGROUND'),

        # Workbook instantiation
        (r'new HSSFWorkbook\(fs\)', 'new XSSFWorkbook(fIn)'),
        (r'new HSSFWorkbook\(\s*\)', 'new XSSFWorkbook()'),

        # POIFSFileSystem removal
        (r'POIFSFileSystem fs = new POIFSFileSystem\(fIn\);\s*\n\s*wb = new XSSFWorkbook\(fIn\);',
         'wb = new XSSFWorkbook(fIn);'),
    ]

    for pattern, replacement in replacements:
        content = re.sub(pattern, replacement, content)

    # Remove empty import lines
    content = re.sub(r'\nimport\s*;\s*\n', '\n', content)

    # Check if we need CellType import
    if 'CellType.' in content and 'import org.apache.poi.ss.usermodel.CellType;' not in content:
        # Add CellType import after other ss.usermodel imports
        content = re.sub(
            r'(import org\.apache\.poi\.ss\.usermodel\.\*;)',
            r'\1\nimport org.apache.poi.ss.usermodel.CellType;',
            content,
            count=1
        )

    # Only write if content changed
    if content != original_content:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(content)
        return True
    return False

def main():
    excel_dir = '/Users/radektuma/DEV/KIS/KIS_App_64bit_JAVA17_Linux/src/main/java/cz/jtbank/konsolidace/excel'

    updated_files = []
    for root, dirs, files in os.walk(excel_dir):
        for filename in files:
            if filename.endswith('.java'):
                filepath = os.path.join(root, filename)
                try:
                    if migrate_file(filepath):
                        updated_files.append(filepath)
                        print(f"✓ Updated: {filename}")
                except Exception as e:
                    print(f"✗ Error in {filename}: {e}")

    print(f"\n{len(updated_files)} files updated successfully")

if __name__ == '__main__':
    main()
