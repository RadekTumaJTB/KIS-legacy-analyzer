#!/bin/bash
# Convert all Java source files from Windows-1250 to UTF-8
# Usage: ./convert_encoding_to_utf8.sh

set -e

GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo "=================================================="
echo "Converting Java files from Windows-1250 to UTF-8"
echo "=================================================="

SRC_DIR="/Users/radektuma/DEV/KIS/KIS_App_64bit_JAVA17_Linux/src/main/java"

if [ ! -d "$SRC_DIR" ]; then
    echo "Error: Source directory not found: $SRC_DIR"
    exit 1
fi

total_files=0
converted_files=0
failed_files=0

# Create backup
BACKUP_DIR="/Users/radektuma/DEV/KIS/encoding_backup_$(date +%Y%m%d_%H%M%S)"
echo "Creating backup: $BACKUP_DIR"
mkdir -p "$BACKUP_DIR"
cp -r "$SRC_DIR" "$BACKUP_DIR/"
echo -e "${GREEN}✓ Backup created${NC}\n"

# Find all .java files and convert
echo "Converting files..."
while IFS= read -r file; do
    ((total_files++))

    # Try to detect encoding and convert
    if iconv -f windows-1250 -t utf-8 "$file" > "$file.utf8" 2>/dev/null; then
        mv "$file.utf8" "$file"
        ((converted_files++))
        echo "✓ $file"
    else
        # Try ISO-8859-2
        if iconv -f iso-8859-2 -t utf-8 "$file" > "$file.utf8" 2>/dev/null; then
            mv "$file.utf8" "$file"
            ((converted_files++))
            echo "✓ $file (ISO-8859-2)"
        else
            # File might already be UTF-8 or have different encoding
            if file "$file" | grep -q "UTF-8"; then
                echo "- $file (already UTF-8)"
            else
                echo "✗ $file (failed)"
                ((failed_files++))
            fi
            rm -f "$file.utf8" 2>/dev/null || true
        fi
    fi
done < <(find "$SRC_DIR" -type f -name "*.java")

echo ""
echo "=================================================="
echo "CONVERSION SUMMARY"
echo "=================================================="
echo "Total files:          $total_files"
echo -e "Converted:            ${GREEN}$converted_files${NC}"
if [ "$failed_files" -gt 0 ]; then
    echo -e "Failed:               ${YELLOW}$failed_files${NC}"
else
    echo "Failed:               0"
fi
echo "Backup location:      $BACKUP_DIR"
echo "=================================================="

if [ "$failed_files" -gt 0 ]; then
    echo -e "\n${YELLOW}⚠ Some files failed to convert.${NC}"
    exit 1
else
    echo -e "\n${GREEN}✅ All files converted successfully!${NC}"
    exit 0
fi
