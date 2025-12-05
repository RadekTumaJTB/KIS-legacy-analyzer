#!/bin/bash
# Convert Excel .xls templates to .xlsx using LibreOffice
# Requires: LibreOffice installed (apt-get install libreoffice)

set -e  # Exit on error

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo "=================================================="
echo "Excel Template Conversion Script (.xls → .xlsx)"
echo "=================================================="

# Check if LibreOffice is installed
if ! command -v libreoffice &> /dev/null; then
    echo -e "${RED}✗ LibreOffice not found!${NC}"
    echo "  Install with: sudo apt-get install libreoffice"
    exit 1
fi

# Template directories
MAIN_DIR="/opt/kis-banking/Konsolidace_JT/sablony"
CARTESIS_DIR="/opt/kis-banking/Konsolidace_JT/sablony/cartesis"

total_files=0
converted_files=0
failed_files=0

# Function to convert files in a directory
convert_directory() {
    local dir=$1
    local dir_name=$2

    if [ ! -d "$dir" ]; then
        echo -e "${YELLOW}⚠ Directory not found: $dir${NC}"
        return
    fi

    echo ""
    echo "Processing: $dir_name"
    echo "--------------------------------------------------"

    cd "$dir"

    # Count .xls files
    local xls_count=$(find . -maxdepth 1 -name "*.xls" | wc -l)

    if [ "$xls_count" -eq 0 ]; then
        echo -e "${YELLOW}  No .xls files found${NC}"
        return
    fi

    # Convert each .xls file
    for file in *.xls; do
        if [ ! -f "$file" ]; then
            continue
        fi

        ((total_files++))

        echo -n "  Converting: $file ... "

        # Convert using LibreOffice headless mode
        if libreoffice --headless --convert-to xlsx "$file" &> /dev/null; then
            local xlsx_file="${file%.xls}.xlsx"

            if [ -f "$xlsx_file" ]; then
                echo -e "${GREEN}✓${NC}"
                ((converted_files++))
            else
                echo -e "${RED}✗ (output file not created)${NC}"
                ((failed_files++))
            fi
        else
            echo -e "${RED}✗ (conversion failed)${NC}"
            ((failed_files++))
        fi
    done
}

# Backup original files
echo ""
echo "Step 1: Creating backup of original .xls files"
echo "--------------------------------------------------"
BACKUP_DIR="/opt/kis-banking/Konsolidace_JT/sablony_backup_$(date +%Y%m%d_%H%M%S)"
if [ -d "$MAIN_DIR" ]; then
    mkdir -p "$BACKUP_DIR"
    find "$MAIN_DIR" -name "*.xls" -exec cp {} "$BACKUP_DIR/" \;
    echo -e "${GREEN}✓ Backup created: $BACKUP_DIR${NC}"
else
    echo -e "${YELLOW}⚠ Main directory not found, skipping backup${NC}"
fi

# Convert main templates
echo ""
echo "Step 2: Converting main templates"
convert_directory "$MAIN_DIR" "Main Templates"

# Convert Cartesis templates
echo ""
echo "Step 3: Converting Cartesis templates"
convert_directory "$CARTESIS_DIR" "Cartesis Templates"

# Summary
echo ""
echo "=================================================="
echo "CONVERSION SUMMARY"
echo "=================================================="
echo "Total files found:        $total_files"
echo -e "Successfully converted:   ${GREEN}$converted_files${NC}"
if [ "$failed_files" -gt 0 ]; then
    echo -e "Failed:                   ${RED}$failed_files${NC}"
else
    echo "Failed:                   0"
fi
echo "=================================================="

if [ "$total_files" -eq 0 ]; then
    echo ""
    echo -e "${YELLOW}⚠ No .xls files found. This script should be run on Linux deployment server.${NC}"
    echo "   Expected directories:"
    echo "   - $MAIN_DIR"
    echo "   - $CARTESIS_DIR"
    exit 2
elif [ "$failed_files" -gt 0 ]; then
    echo ""
    echo -e "${RED}⚠ $failed_files files failed to convert. Check errors above.${NC}"
    echo "   Backup location: $BACKUP_DIR"
    exit 1
else
    echo ""
    echo -e "${GREEN}✅ All template files converted successfully!${NC}"
    echo "   Backup location: $BACKUP_DIR"
    exit 0
fi
