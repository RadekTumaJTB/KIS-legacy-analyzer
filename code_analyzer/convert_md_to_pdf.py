#!/usr/bin/env python3
"""
Konverze Markdown dokumentu na PDF pomocí markdown2 a weasyprint.
"""

import sys
import markdown2
from weasyprint import HTML, CSS
from pathlib import Path

def convert_md_to_pdf(md_file: str, pdf_file: str = None):
    """
    Konvertuje Markdown soubor na PDF.

    Args:
        md_file: Cesta k markdown souboru
        pdf_file: Cesta k výstupnímu PDF (volitelné)
    """
    md_path = Path(md_file)

    if not md_path.exists():
        print(f"❌ Soubor nenalezen: {md_file}")
        sys.exit(1)

    # Určit výstupní PDF soubor
    if pdf_file is None:
        pdf_file = md_path.with_suffix('.pdf')

    pdf_path = Path(pdf_file)

    print(f"📄 Čtu markdown: {md_path}")

    # Přečíst markdown
    with open(md_path, 'r', encoding='utf-8') as f:
        md_content = f.read()

    print(f"📝 Konvertuji na HTML...")

    # Konvertovat markdown na HTML s rozšířeními
    html_content = markdown2.markdown(
        md_content,
        extras=[
            'tables',           # Podpora tabulek
            'fenced-code-blocks',  # Code blocks
            'strike',           # ~~strikethrough~~
            'task_list',        # [ ] task lists
            'header-ids',       # Auto ID pro headings
            'toc',             # Table of contents
        ]
    )

    # CSS styly pro PDF
    css_styles = """
    @page {
        size: A4;
        margin: 2cm;
        @top-center {
            content: "Analýza Rychlé Modernizace - KIS Banking Application";
            font-size: 9pt;
            color: #666;
        }
        @bottom-right {
            content: "Strana " counter(page) " z " counter(pages);
            font-size: 9pt;
            color: #666;
        }
    }

    body {
        font-family: 'DejaVu Sans', Arial, sans-serif;
        font-size: 10pt;
        line-height: 1.6;
        color: #333;
    }

    h1 {
        color: #2c3e50;
        font-size: 24pt;
        margin-top: 20pt;
        margin-bottom: 10pt;
        border-bottom: 2pt solid #3498db;
        padding-bottom: 5pt;
        page-break-before: always;
    }

    h1:first-of-type {
        page-break-before: avoid;
    }

    h2 {
        color: #34495e;
        font-size: 18pt;
        margin-top: 15pt;
        margin-bottom: 8pt;
        border-bottom: 1pt solid #bdc3c7;
        padding-bottom: 3pt;
    }

    h3 {
        color: #2c3e50;
        font-size: 14pt;
        margin-top: 12pt;
        margin-bottom: 6pt;
    }

    h4 {
        color: #34495e;
        font-size: 12pt;
        margin-top: 10pt;
        margin-bottom: 5pt;
        font-weight: bold;
    }

    h5 {
        color: #7f8c8d;
        font-size: 11pt;
        margin-top: 8pt;
        margin-bottom: 4pt;
        font-weight: bold;
    }

    p {
        margin-bottom: 8pt;
        text-align: justify;
    }

    table {
        width: 100%;
        border-collapse: collapse;
        margin: 10pt 0;
        font-size: 9pt;
        page-break-inside: avoid;
    }

    th {
        background-color: #3498db;
        color: white;
        padding: 8pt;
        text-align: left;
        font-weight: bold;
        border: 1pt solid #2980b9;
    }

    td {
        padding: 6pt;
        border: 1pt solid #bdc3c7;
    }

    tr:nth-child(even) {
        background-color: #ecf0f1;
    }

    code {
        background-color: #f8f9fa;
        padding: 2pt 4pt;
        border-radius: 3pt;
        font-family: 'DejaVu Sans Mono', 'Courier New', monospace;
        font-size: 9pt;
        color: #e74c3c;
    }

    pre {
        background-color: #2c3e50;
        color: #ecf0f1;
        padding: 10pt;
        border-radius: 5pt;
        overflow-x: auto;
        font-family: 'DejaVu Sans Mono', 'Courier New', monospace;
        font-size: 8pt;
        line-height: 1.4;
        page-break-inside: avoid;
    }

    pre code {
        background-color: transparent;
        color: #ecf0f1;
        padding: 0;
    }

    ul, ol {
        margin-left: 20pt;
        margin-bottom: 8pt;
    }

    li {
        margin-bottom: 4pt;
    }

    blockquote {
        border-left: 4pt solid #3498db;
        padding-left: 15pt;
        margin-left: 0;
        color: #555;
        font-style: italic;
        background-color: #f8f9fa;
        padding: 10pt 10pt 10pt 15pt;
        margin: 10pt 0;
    }

    hr {
        border: none;
        border-top: 2pt solid #bdc3c7;
        margin: 20pt 0;
    }

    strong {
        color: #2c3e50;
        font-weight: bold;
    }

    em {
        font-style: italic;
        color: #555;
    }

    a {
        color: #3498db;
        text-decoration: none;
    }

    a:hover {
        text-decoration: underline;
    }

    /* Emoji a speciální znaky */
    .emoji {
        font-family: 'Apple Color Emoji', 'Segoe UI Emoji', sans-serif;
    }

    /* Page breaks */
    .page-break {
        page-break-after: always;
    }

    /* Avoid breaks in certain elements */
    h1, h2, h3, h4, h5, h6 {
        page-break-after: avoid;
    }

    ul, ol, table, pre {
        page-break-inside: avoid;
    }
    """

    # Kompletní HTML dokument
    full_html = f"""
    <!DOCTYPE html>
    <html lang="cs">
    <head>
        <meta charset="UTF-8">
        <title>Analýza Rychlé Modernizace - KIS Banking Application</title>
    </head>
    <body>
        {html_content}
    </body>
    </html>
    """

    print(f"🎨 Aplikuji CSS styly...")

    # Konvertovat HTML na PDF
    try:
        HTML(string=full_html).write_pdf(
            pdf_path,
            stylesheets=[CSS(string=css_styles)]
        )

        print(f"✅ PDF vytvořeno: {pdf_path}")
        print(f"📊 Velikost: {pdf_path.stat().st_size / 1024:.1f} KB")

    except Exception as e:
        print(f"❌ Chyba při vytváření PDF: {e}")
        import traceback
        traceback.print_exc()
        sys.exit(1)


if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Použití: python convert_md_to_pdf.py <markdown_soubor> [pdf_soubor]")
        print("Příklad: python convert_md_to_pdf.py analysis.md analysis.pdf")
        sys.exit(1)

    md_file = sys.argv[1]
    pdf_file = sys.argv[2] if len(sys.argv) > 2 else None

    convert_md_to_pdf(md_file, pdf_file)
